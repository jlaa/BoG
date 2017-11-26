package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
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
import br.edu.ifpe.tads.pdm.bog.Model.User;


public class HomeActivity extends AppCompatActivity {


    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, Games> listDataChild;


    private ArrayList<String> mDataList;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    // Pega o game que foi clicado naquela expandable lista.
    private Games game;
    private User user;


    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;
    private ArrayAdapter<Games> adapter;

    private DatabaseReference drGames;
    private DatabaseReference drUsers;

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
        drUsers = fbDB.getReference("users");

        drUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    user = childSnapshot.getValue(User.class);

                        if (user != null) {
                            String txtLogout =  getResources().getString(R.string.logout);
                            String txtHelp =  getResources().getString(R.string.help);
                            String txtPerfil = user.getName();
                            mDataList =  new ArrayList();
                            mDataList.add(txtPerfil);
                            mDataList.add(txtLogout);
                            mDataList.add(txtHelp);
                            mDrawerList.setAdapter(new ArrayAdapter(HomeActivity.this,
                                    R.layout.drawer_list_item,R.id.drawer, mDataList));
                        }else if (user==null)
                        {
                            String txtLogout =  getResources().getString(R.string.logout);
                            String txtHelp =  getResources().getString(R.string.help);
                            String txtPerfil = "Anônimo";
                            mDataList =  new ArrayList();
                            mDataList.add(txtPerfil);
                            mDataList.add(txtLogout);
                            mDataList.add(txtHelp);
                            mDrawerList.setAdapter(new ArrayAdapter(HomeActivity.this,
                                    R.layout.drawer_list_item,R.id.drawer, mDataList));
                        }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        drGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expListView = (ExpandableListView) findViewById(R.id.lvExp);

                listAdapter = new ExpandableListAdapter(HomeActivity.this, listDataHeader, listDataChild);

                expListView.setAdapter(listAdapter);
                expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        game = (Games) parent.getAdapter().getItem(position);
                    }
                });

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Games games = childSnapshot.getValue(Games.class);
                    if (!listDataHeader.contains(games.getNome())) {
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
                Toast.makeText(HomeActivity.this, "O jogo " + games.getNome() + " foi adicionado", Toast.LENGTH_SHORT);
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

        mTitle = mDrawerTitle = getTitle();

        mDataList = new ArrayList<>();

        String txtLogout =  getResources().getString(R.string.logout);
        String txtHelp =  getResources().getString(R.string.help);
        String txtPerfil = "Anonimo";
        mDataList.add(txtPerfil);
        mDataList.add(txtLogout);
        mDataList.add(txtHelp);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item,R.id.drawer, mDataList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Toolbar toolBar = new Toolbar(this);
        toolBar.setNavigationIcon(R.drawable.ic_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolBar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        /*// Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            selectItem("",0);
        }*/


    }



    //DrawerLayout
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    //DrawerLayout
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //DrawerLayout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    //Drawer Layout

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(String text) {
        switch (text) {
            case "Logout":
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser users = mAuth.getCurrentUser();
                if (users != null) {
                    mAuth.signOut();
                } else {
                    Toast.makeText(HomeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Help":
                Toast.makeText(HomeActivity.this, "Não conseguimos lhe fornecer ajuda! Desculpa ;---;", Toast.LENGTH_SHORT).show();
            break;
        }

        if(user.getName()==text)
        {
            Toast.makeText(HomeActivity.this, "Olá " + user.getName(), Toast.LENGTH_SHORT).show();

        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    //Drawer Layout
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    //Drawer Layout
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String drawerText =  (String) parent.getItemAtPosition(position);
            selectItem(drawerText);
        }
    }
    /*//Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    /*//Menu
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


    }*/

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
        listDataChild.put(games.getNome(), games);

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
