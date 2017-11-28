package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.tads.pdm.bog.Model.Games;

public class AddGameActivity extends AppCompatActivity {

    DatabaseReference drGames;
    Button multidata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
    }

    public void adicionarJogo(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        drGames = fbDB.getReference("games");

        final CheckBox checkBoxSingle = (CheckBox) findViewById(R.id.checkbox_SINGLE);
        final CheckBox checkBoxMulti = (CheckBox) findViewById(R.id.checkbox_MULTIPLAYER);
        final CheckBox checkBoxOnline = (CheckBox) findViewById(R.id.checkbox_ONLINE);
        final CheckBox checkBoxCoop = (CheckBox) findViewById(R.id.checkbox_COOPERATIVE);
        final CheckBox checkBoxEUA = (CheckBox) findViewById(R.id.checkbox_EUA);
        final CheckBox checkBoxPT = (CheckBox) findViewById(R.id.checkbox_PT);
        final CheckBox checkBoxFR = (CheckBox) findViewById(R.id.checkbox_FR);
        final CheckBox checkBoxES = (CheckBox) findViewById(R.id.checkbox_ES);
        final List<String> ling = new ArrayList<String>();
        final List<String> num = new ArrayList<String>();

        EditText edNome = (EditText) findViewById(R.id.add_name_game);
        EditText edCategoria = (EditText) findViewById(R.id.add_categoria_game);
        EditText edDescricao = (EditText) findViewById(R.id.add_descricao_game);
        EditText edDesenvolvedor = (EditText) findViewById(R.id.add_desenvolvedor_game);
        EditText edPlataforma = (EditText) findViewById(R.id.add_plataforma_game);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.add_rating_bar);

        String nome = edNome.getText().toString();
        String categoria = edCategoria.getText().toString();
        String descricao = edDescricao.getText().toString();
        String desenvolvedor = edDesenvolvedor.getText().toString();
        String plataforma = edPlataforma.getText().toString();


        if (checkBoxSingle.isChecked()) {
            num.add("Um jogador");
        }
        if (checkBoxMulti.isChecked()) {
            num.add("Multijogador");
        }
        if (checkBoxOnline.isChecked()) {
            num.add("Online");
        }
        if (checkBoxCoop.isChecked()) {
            num.add("Coop");
        }

        if (checkBoxEUA.isChecked()) {
            ling.add("Inglês");
        }
        if (checkBoxPT.isChecked()) {
            ling.add("Português");
        }
        if (checkBoxFR.isChecked()) {
            ling.add("Francês");
        }
        if (checkBoxES.isChecked()) {
            ling.add("Espanhol");
        }


        float ranking = ratingBar.getRating();
        drGames.push().setValue(new Games(nome, ranking, categoria, descricao, desenvolvedor, num, plataforma, ling));
        startActivity(intent);
        finish();


    }


}
