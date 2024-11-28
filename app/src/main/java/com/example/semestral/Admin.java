package com.example.semestral;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.semestral.Adapters.AdminAdapter;
import com.example.semestral.Entidades.Paises;
import com.example.semestral.Services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin extends AppCompatActivity {

    MediaPlayer mp;
    List<Paises> paises;
    ListView lstAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mp = MediaPlayer.create(this, R.raw.bgm2);
        mp.setLooping(true);
        mp.start();
        lstAdmin = (ListView) findViewById(R.id.lstAdmin);
        getPaises();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing to disable the back button
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
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
                        AdminAdapter ad = new AdminAdapter(Admin.this, paises);
                        lstAdmin.setAdapter(ad);
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

    public void createCountry(View view){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.customdialogcreate);
        AppCompatButton crear = dialog.findViewById(R.id.btnAdminCrearCrear);
        AppCompatButton cancelar = dialog.findViewById(R.id.btnAdminCrearCancelar);
        EditText pais = dialog.findViewById(R.id.txtAdminCrearPais);
        EditText capital = dialog.findViewById(R.id.txtAdminCrearCapital);
        EditText bandera = dialog.findViewById(R.id.txtAdminCrearBandera);
        EditText mapa = dialog.findViewById(R.id.txtAdminCrearMapa);
        EditText local = dialog.findViewById(R.id.txtAdminCrearLocal);
        crear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (pais.getText().toString().isEmpty() || capital.getText().toString().isEmpty() ||
                        bandera.getText().toString().isEmpty() || mapa.getText().toString().isEmpty() ||
                        local.getText().toString().isEmpty()) {
                    Toast.makeText(Admin.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }
                Paises paisNuevo = new Paises();
                paisNuevo.setPais(pais.getText().toString());
                paisNuevo.setCapital(capital.getText().toString());
                paisNuevo.setBandera(bandera.getText().toString());
                paisNuevo.setMapa(mapa.getText().toString());
                paisNuevo.setLocalizacion(local.getText().toString());
                Call<Paises> caller = ApiService.getApiService().createCountry(paisNuevo);
                caller.enqueue(new Callback<Paises>() {
                    @Override
                    public void onResponse(Call<Paises> call, Response<Paises> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Admin.this, "País creado exitosamente", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Log.e("Error al crear país", "Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Paises> call, Throwable t) {
                        Toast.makeText(Admin.this, "Fallo al crear país", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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