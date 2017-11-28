package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.edu.ifpe.tads.pdm.bog.Model.Games;

public class DetailsGameActivity extends AppCompatActivity {
    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_game);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);
        Intent intent = getIntent();
        Games game = (Games) intent.getSerializableExtra("game");

        //TextView name = (TextView) findViewById(R.id.game_name);
        TextView starRate = (TextView) findViewById(R.id.rate);
        TextView descricacao = (TextView) findViewById(R.id.descricao);
        TextView categoria = (TextView) findViewById(R.id.categoria);
        TextView desenvolVedor = (TextView) findViewById(R.id.desenvolvedor);
        TextView numjogadores = (TextView) findViewById(R.id.numjogadores);
        TextView plataforma = (TextView) findViewById(R.id.plataforma);
        TextView linguagem = (TextView) findViewById(R.id.linguagem);

        //name.setText(game.getNome());
        starRate.setText("Rank: " + game.getRatingBar());
        descricacao.setText("Descrição: \n" + game.getDescricao());
        categoria.setText("Categoria: " + game.getCategoria());
        desenvolVedor.setText("Desenvolvedor: " + game.getDesenvolvedor());
        String numero_jogadores = "Número de Jogadores: ";

        for(int i=0;i<game.getNum_jogadores().size();i++)
        {
            numero_jogadores=numero_jogadores+game.getNum_jogadores().get(i)+",";
        }
        numjogadores.setText(numero_jogadores);
        plataforma.setText("Plataforma: " + game.getPlataforma());
        String linguagemTexto = "Linguagem: ";
        for(int i =0 ;i<game.getLinguagem().size();i++) {
            linguagemTexto = linguagemTexto + game.getLinguagem().get(i) + ",";
        }

        linguagem.setText(linguagemTexto);
        setTitle(game.getNome());

    }

    @Override

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    mAuth.signOut();
                } else {
                    Toast.makeText(DetailsGameActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.help:
                Toast.makeText(DetailsGameActivity.this, "Não conseguimos lhe fornecer ajuda! Desculpa ;---;", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
