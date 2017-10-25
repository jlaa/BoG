package br.edu.ifpe.tads.pdm.bog;

import java.io.Serializable;

/**
 * Created by isabella on 12/10/2017.
 */

public class Games implements Serializable {

    private String nome;
    private String categoria;
    private float ratingBar;
    //private float imagem;
    private String descricao;

    public Games(String nome, float ratingBar, String categoria, String descricao) {
        this.nome = nome;
        this.ratingBar = ratingBar;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}