package br.edu.ifpe.tads.pdm.bog.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by isabella on 12/10/2017.
 */
@IgnoreExtraProperties
public class Games implements Serializable {

    private final String nome;
    private final String categoria;
    private float ratingBar;
    //private float imagem;
    private final String descricao;

    public Games(){
        this.nome=null;
        this.categoria=null;
        this.descricao=null;
    }

    public Games(String nome, float ratingBar, String categoria, String descricao) {
        this.nome = nome;
        this.ratingBar = ratingBar;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public float getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(float ratingBar) {
        this.ratingBar = ratingBar;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

}