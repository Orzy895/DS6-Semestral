package com.example.semestral;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    String currentUser = "";
    LinearLayout admin;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InicializarControladores();
        currentUser = getUser();
        if (!(currentUser.contains("admin"))){
            admin.setVisibility(View.GONE);
        }
        mp = MediaPlayer.create(this, R.raw.bgm1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.auth, menu);

        MenuItem loginItem = menu.findItem(R.id.menu_login);
        MenuItem registerItem = menu.findItem(R.id.menu_register);
        MenuItem logoutItem = menu.findItem(R.id.menu_logout);
        Log.d(TAG, "Probando");
        if (currentUser.equals("")) {
            loginItem.setVisible(true);
            registerItem.setVisible(true);
            logoutItem.setVisible(false);
            Log.d(TAG, "No hay usuario");
        } else {
            loginItem.setVisible(false);
            registerItem.setVisible(false);
            logoutItem.setVisible(true);
            Log.d(TAG, "Hay Usuario");
        }
        return true;
    }

    public void InicializarControladores(){
        admin = (LinearLayout) findViewById(R.id.adminButton);
    }

    public String getUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        return sharedPreferences.getString("user", "");
    }

    public void showMenu(View view){
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.auth, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.menu_login){
                login(view);
                return true;
            }
            if(item.getItemId() == R.id.menu_register){
                register(view);
                return true;
            }
            if(item.getItemId() == R.id.menu_logout){
                logout();
            }
            return false;
        });
        popupMenu.show();
    }

    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user");
        editor.apply();
        recreate();
    }


    public void login(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void register(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void quizPaises(View view){
        Intent intent = new Intent(this, Quiz.class);
        intent.putExtra("quiz", "banderas");
        startActivity(intent);
    }

    public void quizMapa(View view){
        Intent intent = new Intent(this, QuizMapa.class);
        intent.putExtra("quiz", "continente-mapas");
        startActivity(intent);
    }

    public void irAyuda(View view){
        Intent i = new Intent (this, Ayuda.class);
        startActivity(i);
        finish();
    }

    public void irGlosario(View view){
        Intent in = new Intent(this, Glosario.class);
        startActivity(in);
        finish();
    }

    public void irAdmin(View view){
        Intent i = new Intent(this, Admin.class);
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
