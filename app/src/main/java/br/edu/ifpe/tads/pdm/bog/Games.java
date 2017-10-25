package br.edu.ifpe.tads.pdm.bog;

/**
 * Created by isabella on 12/10/2017.
 */

public class Games {

    private String nome;
    //private String categoria;
    private float ratingBar;

    public Games(String nome,float ratingBar)
    {
        this.nome=nome;
        this.ratingBar=ratingBar;
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

    /* public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }*/
}
