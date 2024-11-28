package com.example.semestral;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.example.semestral.Entidades.Paises;
import com.example.semestral.Services.ApiService;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizMapa extends AppCompatActivity {

    String tipoQuiz;
    int ronda, puntuacion;
    ImageView quizImg, mapaFondo;
    AppCompatButton quizOpc1, quizOpc2, quizOpc3, quizOpc4, selectedButton;
    TextView quizRonda;
    Random random = new Random();
    int[] options = new int[4];
    Paises paisActual;
    List<Paises> paises;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizmapa);
        InicializarControladores();

        Intent intent = getIntent();
        if (intent != null) {
            tipoQuiz = intent.getStringExtra("quiz");
            ronda = intent.getIntExtra("ronda", 1);
            puntuacion = intent.getIntExtra("puntuacion", 0);
            paises = intent.getParcelableArrayListExtra("paises");
        }

        if (paises == null || paises.isEmpty()) {
            empezarJuego();
        } else {
            setearDatos(paises);
        }

        quizRonda.setText("Quiz: " + ronda + "/10");

        mp = MediaPlayer.create(this, R.raw.bgm2);
        mp.setLooping(true);
        mp.start();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing to disable the back button
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void InicializarControladores() {
        quizImg = (ImageView) findViewById(R.id.quizImg);
        quizOpc1 = (AppCompatButton) findViewById(R.id.quizOpc1);
        quizOpc2 = (AppCompatButton) findViewById(R.id.quizOpc2);
        quizOpc3 = (AppCompatButton) findViewById(R.id.quizOpc3);
        quizOpc4 = (AppCompatButton) findViewById(R.id.quizOpc4);
        quizRonda = (TextView) findViewById(R.id.quizRonda);
        quizOpc1.setOnClickListener(v -> handleButtonClick(quizOpc1));
        quizOpc2.setOnClickListener(v -> handleButtonClick(quizOpc2));
        quizOpc3.setOnClickListener(v -> handleButtonClick(quizOpc3));
        quizOpc4.setOnClickListener(v -> handleButtonClick(quizOpc4));
        mapaFondo = (ImageView)findViewById(R.id.mapaFondo);
        int fondo = random.nextInt(3);
        if(fondo == 0){
            mapaFondo.setImageResource(R.drawable.quizbg1);
        }else if(fondo == 1){
            mapaFondo.setImageResource(R.drawable.quizbg2);
        }else{
            mapaFondo.setImageResource(R.drawable.quizbg3);
        }
    }

    public void empezarJuego() {
        Call<List<Paises>> caller = ApiService.getApiService().quizMapas();
        caller.enqueue(new Callback<List<Paises>>() {
            @Override
            public void onResponse(Call<List<Paises>> call, Response<List<Paises>> response) {
                if (response.isSuccessful()) {
                    List<Paises> paisesList = response.body();
                    if (paisesList != null) {
                        paises = paisesList;
                        setearDatos(paisesList);
                    } else {
                        Log.e("Quiz", "No hay datos");
                    }
                } else {
                    Log.e("Quiz", "Error en respuesta de API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Paises>> call, Throwable t) {
                Log.e("Quiz", "Problema obteniendo API", t);
            }
        });
    }

    public void setearDatos(List<Paises> paises) {
        paisActual = paises.get(ronda - 1);
        int tipoDato = random.nextInt(2);
        if(tipoDato == 0){
            Picasso.get().load(paisActual.getMapa()).into(quizImg);
        }else{
            Picasso.get().load(paisActual.getLocalizacion()).into(quizImg);
        }
        //ensure all options are unique
        Set<Integer> usedIndices = new HashSet<>();
        usedIndices.add(ronda - 1);
        for (int x = 0; x < 4; x++) {
            if (x == 0) {
                options[x] = ronda - 1;
            } else {
                int randomIndex;
                do {
                    randomIndex = random.nextInt(paises.size());
                } while (usedIndices.contains(randomIndex));
                options[x] = randomIndex;
                usedIndices.add(randomIndex);
            }
        }
        randomizar();
        quizOpc1.setText(paises.get(options[0]).getPais());
        quizOpc2.setText(paises.get(options[1]).getPais());
        quizOpc3.setText(paises.get(options[2]).getPais());
        quizOpc4.setText(paises.get(options[3]).getPais());
    }

    private void randomizar() {
        for (int i = options.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = options[index];
            options[index] = options[i];
            options[i] = temp;
        }
    }

    private void handleButtonClick(AppCompatButton button) {
        if (selectedButton != null) {
            selectedButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.botongris, null));
        }
        selectedButton = button;
        selectedButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.botonnaranja, null));
    }

    public void siguiente(View view) {
        if (selectedButton == null) {
            Toast.makeText(this, "Seleccione una respuesta", Toast.LENGTH_LONG).show();
        } else {
            if ((selectedButton.getText().toString().equals(paisActual.getPais())) || (selectedButton.getText().toString().equals(paisActual.getCapital()))) {
                puntuacion++;
            }
            if (ronda < 10) {
                ronda++;
                Intent intent;
                int modoJuego = random.nextInt(2);
                if(modoJuego==0){
                    intent = new Intent(this, QuizMapa.class);
                }else{
                    intent = new Intent(this, QuizMapa2.class);
                }
                intent.putExtra("quiz", tipoQuiz);
                intent.putExtra("ronda", ronda);
                intent.putExtra("puntuacion", puntuacion);

                if (paises != null) {
                    intent.putParcelableArrayListExtra("paises", new ArrayList<>(paises));
                }

                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, Resultado.class);
                intent.putExtra("quiz", tipoQuiz);
                intent.putExtra("puntuacion", puntuacion);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null && mp.isPlaying()) {
            mp.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp != null && !mp.isPlaying()) {
            mp.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    public void backHome(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}