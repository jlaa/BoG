package br.edu.ifpe.tads.pdm.bog.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by LucasPC on 25/12/2017.
 */
@IgnoreExtraProperties
public class Comentario implements Serializable {
    private String texto;
    private String idGame;
    private String idUsuario;

    public Comentario()
    {

    }

    public Comentario(String texto,String idGame,String idUsuario)
    {
        this.texto = texto;
        this.idGame = idGame;
        this.idUsuario = idUsuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
