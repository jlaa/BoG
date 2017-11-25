package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.ifpe.tads.pdm.bog.Model.Games;


public class HomeActivity extends AppCompatActivity {

    private ArrayList<Games> gamesList = new ArrayList();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, Games> listDataChild;

    // Pega o game que foi clicado naquela expandable lista.
    private Games game;


    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;
    private ArrayAdapter<Games> adapter;

    private DatabaseReference drGames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, Games>();

        final FirebaseDatabase fbDB = FirebaseDatabase.getInstance();

        drGames = fbDB.getReference("games");

        drGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expListView = (ExpandableListView) findViewById(R.id.lvExp);

                listAdapter = new ExpandableListAdapter(HomeActivity.this, listDataHeader, listDataChild);

                expListView.setAdapter(listAdapter);
                expListView.setOnItemClickListener(new AdapterView.OnItemClickListener()         {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        game = (Games) parent.getAdapter().getItem(position);
                    }
                });

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Games games = childSnapshot.getValue(Games.class);
                    if(!listDataHeader.contains(games.getNome())) {
                        if (games != null) {
                            prepareListData(games);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        drGames.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Games games = dataSnapshot.getValue(Games.class);
                Toast.makeText(HomeActivity.this,"O jogo " + games.getNome()+" foi adicionado",Toast.LENGTH_SHORT);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

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

    /*public void setGames() {


        gamesList.add(new Games("League of Legends", 5, "Moba", "League of Legends é um jogo eletrônico do gênero multiplayer online battle arena, desenvolvido e publicado pela Riot Games. Um estilo moba como você nunca viu antes"));
        gamesList.add(new Games("Paladins", 4, "FPS/Ação", "A versão para pobres do overwatch"));
        gamesList.add(new Games("Overwatch", 5, "FPS/Ação", "Lute em vários cenários contra outros players e faça objetivos"));
        gamesList.add(new Games("Mortal Kombat", 5, "Ação", "SANGUE , SANGUE , SANGUEEEE!"));
        gamesList.add(new Games("The Last of Us", 5, "Ação/Terror", "Zumbis e uma história de emocionar corações"));
        gamesList.add(new Games("Dark Souls", 4, "Aventura/RPG", "YOU DIED!"));
        gamesList.add(new Games("Until Dawn", 4, "Terror", "JUMPSCARE!!!"));
        gamesList.add(new Games("Assassins Creed", 5, "Aventura", "Siga na pele de uma assasino no passado e faça altos parkours"));
        gamesList.add(new Games("AION", 3, "MMORPG ", "Faça batalhas de guilda e upe pets e personagens para o nível máximo faça isso denovo denovo denovo até enjoar"));
        gamesList.add(new Games("Grand Chase", 4, "RPG/Aventura", "Lute em masmorrar e salve o mundo!"));
        gamesList.add(new Games("Sonic", 4, "Avenutra", "Saia correndo feito louco e mate o bigodudo"));
        gamesList.add(new Games("Super Mario World", 5, "Aventura", "Saia correndo feito louco e mate a tartaruga, ah salve a princesa no fim!"));


    }*/

    private void prepareListData(Games games) {

        listDataHeader.add(games.getNome());
        listDataChild.put(games.getNome(),games);

    }

   /* public void moreButtonClick(View view) {

        Intent intent = new Intent(HomeActivity.this, DetailsGameActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);

    }*/

    public void onClickAdicionarJogo(View view) {
        Intent intent = new Intent(this, AddGameActivity.class);
        startActivity(intent);
    }


}
