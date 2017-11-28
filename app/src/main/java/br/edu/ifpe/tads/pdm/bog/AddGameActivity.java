package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.edu.ifpe.tads.pdm.bog.Model.Games;

public class AddGameActivity extends AppCompatActivity {

    DatabaseReference drGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
    }

    public void adicionarJogo(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        drGames = fbDB.getReference("games");
        EditText edNome= (EditText) findViewById(R.id.add_name_game);
        EditText edCategoria = (EditText) findViewById(R.id.add_categoria_game);
        EditText edDescricao = (EditText) findViewById(R.id.add_descricao_game);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.add_rating_bar);
        String nome = edNome.getText().toString();
        String categoria = edCategoria.getText().toString();
        String descricao = edDescricao.getText().toString();
        float  ranking = ratingBar.getRating();
        drGames.push().setValue(new Games(nome,ranking,categoria,descricao));
        startActivity(intent);
        finish();
    }

}
