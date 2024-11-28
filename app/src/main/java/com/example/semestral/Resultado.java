package com.example.semestral;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;


import com.example.semestral.Adapters.RankingAdapter;
import com.example.semestral.Entidades.Ranking;
import com.example.semestral.Services.ApiService;

import java.util.ArrayList;
import java.util.List;

public class Resultado extends AppCompatActivity {

    TextView resultadoPun, resultadoTot;
    ListView ranking;
    String quiz, user;
    MediaPlayer mp;
    int puntaje;
    List<Ranking> rankings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        InicializarControladores();
        if(!(user.equals(""))){
            agregarPuntajeRanking(quiz, puntaje, user);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (quiz.equals("banderas")) {
                    getRankingPais();
                } else {
                    getRankingMapa();
                }
            }
        }, 5000);
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
        resultadoPun = (TextView) findViewById(R.id.resultadoPuntaje);
        resultadoTot = (TextView) findViewById(R.id.resultadoTotal);
        ranking = (ListView) findViewById(R.id.resultadoListview);
        Bundle datos = getIntent().getExtras();
        if(datos!=null){
            quiz = datos.getString("quiz");
            puntaje = datos.getInt("puntuacion");
        }
        resultadoPun.setText("Puntaje: "+puntaje+"0%");
        resultadoTot.setText("Has respondido "+puntaje+"/10 preguntas correctamente");
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        user = sharedPreferences.getString("user", "");
    }

    public void agregarPuntajeRanking(String modo, int puntaje, String user){
        Ranking ranking = new Ranking(user, puntaje+"0/100", modo);
        Call<Ranking> caller = ApiService.getApiService().createRanking(ranking);
        caller.enqueue(new Callback<Ranking>() {
            @Override
            public void onResponse(Call<Ranking> call, Response<Ranking> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Resultado.this, "Ranking saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Resultado.this, "Failed to save ranking", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Ranking> call, Throwable t) {

            }
        });
    }

    public void getRankingPais(){
        Call<List<Ranking>> caller = ApiService.getApiService().paises();
        caller.enqueue(new Callback<List<Ranking>>() {
            @Override
            public void onResponse(Call<List<Ranking>> call, Response<List<Ranking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rankings = response.body();
                    RankingAdapter ad = new RankingAdapter(Resultado.this, rankings);
                    ranking.setAdapter(ad);
                } else {
                    Toast.makeText(Resultado.this, "Failed to retrieve rankings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ranking>> call, Throwable t) {

            }
        });
    }

    public void getRankingMapa(){
        Call<List<Ranking>> caller = ApiService.getApiService().mapas();
        caller.enqueue(new Callback<List<Ranking>>() {
            @Override
            public void onResponse(Call<List<Ranking>> call, Response<List<Ranking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rankings = response.body();
                    RankingAdapter ad = new RankingAdapter(Resultado.this, rankings);
                    ranking.setAdapter(ad);
                } else {
                    Toast.makeText(Resultado.this, "Failed to retrieve rankings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ranking>> call, Throwable t) {

            }
        });
    }

    public void goHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
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
}