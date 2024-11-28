package com.example.semestral;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.semestral.Entidades.User;
import com.example.semestral.Services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    EditText txtUsername, txtPassword, txtConfirmPassword;
    String username, password, confirmPassword;
    TextView botonRegistro;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InicializarControladores();
        mp = MediaPlayer.create(this, R.raw.bgm1);
        mp.setLooping(true);
        mp.start();

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();
                confirmPassword = txtConfirmPassword.getText().toString();
                if(verificarCampos(username, password, confirmPassword)){
                    registro(username, password, confirmPassword);
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing to disable the back button
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void InicializarControladores(){
        txtUsername = (EditText) findViewById(R.id.txtRegisterUsername);
        txtPassword = (EditText) findViewById(R.id.txtRegisterPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtRegisterCPassword);
        botonRegistro = (TextView)findViewById(R.id.registerBtn);
    }

    public void registro(String username, String password, String confirmPassword){
        User user = new User(username, password, password,"");
        Call<User> call = ApiService.getApiService().signup(user);
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
                        Toast.makeText(Register.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Register.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else{
                    Toast.makeText(Register.this, "Contraseñas repetidas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Register.this, "Error en registro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean verificarCampos(String nombre, String pass, String cPass){
        if(TextUtils.isEmpty(nombre)){
            txtUsername.setError("Introduzca el nombre de usuario");
            return false;
        }if(TextUtils.isEmpty(pass)){
            txtPassword.setError("Introduzca la contraseña");
            return false;
        }if(TextUtils.isEmpty(cPass)){
            txtConfirmPassword.setError("Introduzca la contraseña");
            return false;
        }
        return true;
    }

    public void irLogin(View view) {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }

    public void backHome(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}