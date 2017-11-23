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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



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


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setGames();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);

        setGames();

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);
        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                game = (Games) parent.getAdapter().getItem(position);
            }
        });



       /* expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });*/

        // Listview on child click listener
        /*expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });*/

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


    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, Games>();

        // Adding data header

        for(int i=0; i<gamesList.size(); i ++) {
            listDataHeader.add(gamesList.get(i).getNome());
        }


        listDataChild.put(listDataHeader.get(0), gamesList.get(0));
        listDataChild.put(listDataHeader.get(1), gamesList.get(1));
        listDataChild.put(listDataHeader.get(2), gamesList.get(2));


    }

    public void moreButtonClick (View view){

        Intent intent = new Intent(HomeActivity.this, DetailsGameActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);

    }


}
