package br.edu.ifpe.tads.pdm.bog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<Games> arrayList = new ArrayList();


    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;
    private ArrayAdapter<Games> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);
        ListView listView = (ListView) findViewById(R.id.list_games);
        setGames();
        adapter = new ListViewAdapter(this, R.layout.activity_home, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener());
    }

    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.activity_home);
                dialog.setTitle("Game details");

                TextView name = (TextView) dialog.findViewById(R.id.nomeJogo);
                RatingBar starRate = (RatingBar) dialog.findViewById(R.id.ratingBar);

                Games game = (Games) parent.getAdapter().getItem(position);
                name.setText("Nome do jogo: " + game.getNome());
                starRate.setNumStars(starRate.getNumStars());
            }
        };
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
                    Toast.makeText(HomeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.help:
                Toast.makeText(HomeActivity.this, "Não conseguimos lhe fornecer ajuda! Desculpa ;---;", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


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

    public void setGames() {
        /*private static final Games [] games = {"League of legends", "Paladins", "Overwatch", "Mortal Kombat",
            "The Last of Us", "Dark Souls", "Until Dawn", "Assassins Creed", "AION", "Grand Chase", "Sonic",
            "Super Mario World"};*/
        arrayList.add(new Games("League of legends", 5));
        arrayList.add(new Games("Paladins", 4));
        arrayList.add(new Games("Overwatch", 5));
        arrayList.add(new Games("Mortal Kombat", 5));
        arrayList.add(new Games("The Last of Us", 5));
        arrayList.add(new Games("Dark Souls", 4));
        arrayList.add(new Games("Until Dawn", 4));
        arrayList.add(new Games("Assassins Creed", 5));
        arrayList.add(new Games("AION", 3));
        arrayList.add(new Games("Grand Chase", 4));
        arrayList.add(new Games("Sonic", 4));
        arrayList.add(new Games("Super Mario World", 5));


    }
}
