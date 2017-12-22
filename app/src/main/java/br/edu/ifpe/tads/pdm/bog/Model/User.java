package br.edu.ifpe.tads.pdm.bog.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucasPC on 25/11/2017.
 */

@IgnoreExtraProperties
public class User {
    private String name;
    private String email;
    private List<GamesJogados> gamesJogados;

    public User() {
        gamesJogados = new ArrayList();
    }

    public User(String name, String email,List<GamesJogados> jogados) {
        this.gamesJogados = jogados;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<GamesJogados> getGamesJogados() {
        return gamesJogados;
    }

    public void addGame(GamesJogados gameJogado) {

        gamesJogados.add(gameJogado);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGamesJogados(List<GamesJogados> gamesJogados) {
        this.gamesJogados = gamesJogados;
    }
}