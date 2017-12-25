package br.edu.ifpe.tads.pdm.bog.Model;

/**
 * Created by isabella on 20/12/2017.
 */

public class Comentario {

    private final String comentario;
    private final String id_user;
    private final String id_jogo;

    public Comentario(String comentario, String id_user, String id_jogo){
        this.comentario = comentario;
        this.id_user = id_user;
        this.id_jogo = id_jogo;
    }

    public String getComentario() {
        return comentario;
    }

    public String getId_user() {
        return id_user;
    }

    public String getId_jogo() {
        return id_jogo;
    }
}
