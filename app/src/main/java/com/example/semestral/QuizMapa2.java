package com.example.semestral;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.semestral.Entidades.Paises;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizMapa2 extends AppCompatActivity {

    String tipoQuiz;
    int ronda, puntuacion;
    ImageView img1, img2;
    Random random = new Random();
    EditText respuesta;
    MediaPlayer mp;
    Paises paisActual;
    List<Paises> paises;
    TextView quizRonda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mapa2);
        InicializarControladores();

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

    public void InicializarControladores(){
        img1 = (ImageView)findViewById(R.id.quizMapaImg1);
        img2 = (ImageView)findViewById(R.id.quizMapaImg2);
        respuesta = (EditText)findViewById(R.id.txtQuizMapa2);
        quizRonda = (TextView)findViewById(R.id.quizRonda);
        Intent intent = getIntent();
        if (intent != null) {
            tipoQuiz = intent.getStringExtra("quiz");
            ronda = intent.getIntExtra("ronda", 1);
            puntuacion = intent.getIntExtra("puntuacion", 0);
            paises = intent.getParcelableArrayListExtra("paises");
        }
        quizRonda.setText("Quiz: " + ronda + "/10");
        setearDatos(paises);
    }

    public void setearDatos(List<Paises> paises) {
        paisActual = paises.get(ronda - 1);
        Picasso.get().load(paisActual.getMapa()).into(img1);
        Picasso.get().load(paisActual.getLocalizacion()).into(img2);
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

    public void siguiente(View view) {
        String respuestaUsuario = respuesta.getText().toString();
        if(TextUtils.isEmpty(respuestaUsuario)){
            respuesta.setError("Introduzca la respuesta");
        }
        if(respuestaUsuario.equalsIgnoreCase(paisActual.getPais())){
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