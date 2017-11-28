package br.edu.ifpe.tads.pdm.bog.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

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
    private final String desenvolvedor;
    private final List<String> linguagem;
    private final List<String> num_jogadores;
    private final String plataforma;


    public Games(){
        this.nome=null;
        this.categoria=null;
        this.descricao=null;
        this.desenvolvedor=null;
        this.num_jogadores=null;
        this.linguagem=null;
        this.plataforma=null;
    }

    public Games(String nome, float ratingBar, String categoria, String descricao, String desenvolvedor, List<String> num_jogadores, String plataforma, List<String> linguagem) {
        this.nome = nome;
        this.ratingBar = ratingBar;
        this.categoria = categoria;
        this.descricao = descricao;
        this.desenvolvedor = desenvolvedor;
        this.num_jogadores = num_jogadores;
        this.plataforma = plataforma;
        this.linguagem = linguagem;
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

    public String getDesenvolvedor() { return desenvolvedor; }

    public List<String> getLinguagem() { return linguagem; }

    public List<String> getNum_jogadores() { return num_jogadores; }

    public String getPlataforma() { return plataforma; }
}