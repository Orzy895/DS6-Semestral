package com.example.semestral;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.semestral.Adapters.GlosarioAdapter;
import com.example.semestral.Entidades.Paises;
import com.example.semestral.Services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Glosario extends AppCompatActivity {

    ListView glosario;
    MediaPlayer mp;
    List<Paises> paises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glosario);
        mp = MediaPlayer.create(this, R.raw.bgm2);
        mp.setLooping(true);
        mp.start();
        glosario = (ListView)findViewById(R.id.lstGlosario);
        getPaises();
    }

    public void getPaises(){
        Call<List<Paises>> caller = ApiService.getApiService().getPaises();
        caller.enqueue(new Callback<List<Paises>>() {
            @Override
            public void onResponse(Call<List<Paises>> call, Response<List<Paises>> response) {
                if (response.isSuccessful()) {
                    List<Paises> paisesList = response.body();
                    if (paisesList != null) {
                        paises = paisesList;
                        GlosarioAdapter ad = new GlosarioAdapter(Glosario.this, paises);
                        glosario.setAdapter(ad);
                    } else {
                        Log.e("Admin", "No hay datos de los paises");
                    }
                } else {
                    Log.e("Admin", "Error en Api " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Paises>> call, Throwable t) {
                Log.e("Admin", "Fallo en llamada de API");
            }
        });
    }

    public void backHome(View view) {
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