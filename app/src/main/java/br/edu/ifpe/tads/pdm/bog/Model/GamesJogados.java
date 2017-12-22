package br.edu.ifpe.tads.pdm.bog.Model;

import java.io.Serializable;

/**
 * Created by LucasPC on 21/12/2017.
 */

public class GamesJogados implements Serializable {

    private String status;
    private Games game;
    private float avaliacao;

    public GamesJogados()
    {

    }

    public GamesJogados(String horasJogadas,String status,Games game,float avaliacao)
    {
        this.status=status;
        this.game=game;
        this.avaliacao=avaliacao;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Games getGame() {
        return game;
    }

    public void setGame(Games games) {
        this.game = games;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }
}
