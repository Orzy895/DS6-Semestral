package com.example.semestral.Entidades;

import android.os.Bundle;

public class User {
    public String username, password, confirmPassword;
    String id;

    public User(String username, String password, String confirmPassword, String id) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bundle toBundle(){
        Bundle b = new Bundle();
        b.putString("username", getUsername());
        return b;
    }
}
