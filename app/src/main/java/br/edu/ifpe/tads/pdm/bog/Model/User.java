package br.edu.ifpe.tads.pdm.bog.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by LucasPC on 25/11/2017.
 */

@IgnoreExtraProperties
public class User {
    private String name;
    private String email;
    private ArrayList<Games> gamesJogados;

    public User() {
        gamesJogados=new ArrayList<>();
    }

    public User(String name, String email) {
        gamesJogados=new ArrayList<>();
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void addGame(Games game)
    {

        gamesJogados.add(game);
    }


}