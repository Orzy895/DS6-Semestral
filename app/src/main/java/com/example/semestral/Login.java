package com.example.semestral;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.semestral.Entidades.User;
import com.example.semestral.Services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    EditText password, username;
    TextView login;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = (EditText) findViewById(R.id.txtPassword);
        username = (EditText) findViewById(R.id.txtUsername);
        login = (TextView) findViewById(R.id.btnLogin);
        mp = MediaPlayer.create(this, R.raw.bgm2);
        mp.setLooping(true);
        mp.start();
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String usuario = username.getText().toString();
                String pass = password.getText().toString();
                if(verificarCampos(usuario, pass)){
                    login(usuario, pass);
                }
            }
        });
    }

    public void login(String username, String password){
        User user = new User(username, password, password,"");
        Call<User> call = ApiService.getApiService().login(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User loggedUser = response.body();
                    if(loggedUser!=null){
                        String user = loggedUser.getUsername();
                        SharedPreferences pref = getSharedPreferences("app", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("user", user);
                        edit.apply();
                        Toast.makeText(Login.this, "Inicio de sesión exitosamente", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else{
                    Toast.makeText(Login.this, "Contraseña o Usuario incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Login.this, "Error en login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean verificarCampos(String nombre, String pass){
        if(TextUtils.isEmpty(nombre)){
            username.setError("Introduzca el nombre de usuario");
            return false;
        }if(TextUtils.isEmpty(pass)){
            password.setError("Introduzca la contraseña");
            return false;
        }
        return true;
    }

    public void backHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void irRegistro(View view) {
        Intent i = new Intent(this, Register.class);
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