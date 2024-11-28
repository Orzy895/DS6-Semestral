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
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.semestral.Entidades.Paises;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Quiz2 extends AppCompatActivity {

    String tipoQuiz;
    int ronda, puntuacion;
    ImageView quiz2Img;
    TextView quizRonda;
    EditText respuesta;
    Random random = new Random();
    Paises paisActual;
    List<Paises> paises;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);
        InicializarControladores();
        setearDatos(paises);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing to disable the back button
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void InicializarControladores(){
        quiz2Img = (ImageView)findViewById(R.id.quiz2Img);
        respuesta = (EditText)findViewById(R.id.txtQuiz2Res);
        quizRonda = (TextView)findViewById(R.id.quizRonda);
        Intent intent = getIntent();
        if (intent != null) {
            tipoQuiz = intent.getStringExtra("quiz");
            ronda = intent.getIntExtra("ronda", 1);
            puntuacion = intent.getIntExtra("puntuacion", 0);
            paises = intent.getParcelableArrayListExtra("paises");
        }

        quizRonda.setText("Quiz: " + ronda + "/10");

        mp = MediaPlayer.create(this, R.raw.bgm1);
        mp.setLooping(true);
        mp.start();

    }

    public void setearDatos(List<Paises> paises) {
        paisActual = paises.get(ronda - 1);
        Picasso.get().load(paisActual.getBandera()).into(quiz2Img);
    }


    public void siguiente(View view) {
        String resp = respuesta.getText().toString();
        if(TextUtils.isEmpty(resp)){
            respuesta.setError("Introduzca la respuesta");
        }
        if(resp.equalsIgnoreCase(paisActual.getPais())){
            puntuacion++;
        }
        if (ronda < 10) {
            ronda++;
            Intent intent;
            int modo = random.nextInt(2);
            if(modo==0){
               intent = new Intent(this, Quiz.class);
            }else{
                intent = new Intent(this, Quiz2.class);
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