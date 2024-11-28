package com.example.semestral.Entidades;

public class Ranking {

    String username, scores, game_mode;

    public Ranking(String username, String scores, String game_mode) {
        this.username = username;
        this.scores = scores;
        this.game_mode = game_mode;
    }

    public String getGame_mode() {
        return game_mode;
    }

    public void setGame_mode(String game_mode) {
        this.game_mode = game_mode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }
}
