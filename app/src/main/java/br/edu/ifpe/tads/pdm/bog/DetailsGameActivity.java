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
        Intent i = getIntent();
        Games game = (Games) i.getSerializableExtra("game");
        //TextView name = (TextView) findViewById(R.id.game_name);
        TextView starRate = (TextView) findViewById(R.id.rate);
        TextView descricacao = (TextView) findViewById(R.id.descricao);
        TextView categoria = (TextView) findViewById(R.id.categoria);

        //name.setText(game.getNome());
        starRate.setText("Rank: " + game.getRatingBar());
        descricacao.setText("Descrição: \n" + game.getDescricao());
        categoria.setText("Gênero: " + game.getCategoria());

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
