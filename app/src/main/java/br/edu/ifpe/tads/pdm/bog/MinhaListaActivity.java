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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifpe.tads.pdm.bog.Model.Games;
import br.edu.ifpe.tads.pdm.bog.Model.GamesJogados;
import br.edu.ifpe.tads.pdm.bog.Model.User;

public class MinhaListaActivity extends AppCompatActivity {

    private ExpandableListAdapter listMyGamesAdapter;
    private ExpandableListView expMyListView;
    private List<String> myListDataHeader;
    private HashMap<String, Games> myListDataChild;

    private Games game;
    private User user;

    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;

    private ArrayList<String> mDataList;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private DatabaseReference drUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_lista);


        final TextView nomePerfil = (TextView) findViewById(R.id.perfilName);

        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);

        myListDataHeader = new ArrayList<String>();
        myListDataChild = new HashMap<String, Games>();


        final FirebaseDatabase fbDB = FirebaseDatabase.getInstance();

        drUsers = fbDB.getReference("users");

        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        drUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myListDataHeader = new ArrayList<>();
                myListDataChild = new HashMap<>();


                expMyListView = (ExpandableListView) findViewById(R.id.myListGames);

                listMyGamesAdapter = new ExpandableListAdapter(MinhaListaActivity.this, myListDataHeader, myListDataChild);
                expMyListView.setAdapter(listMyGamesAdapter);
                expMyListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                        // game = (Games) parent.getAdapter().getItem(groupPosition);
                        game = (Games) parent.getExpandableListAdapter().getChild(groupPosition, groupPosition);

                        return false;
                    }

                });
                expMyListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        for (int g = 0; g < myListDataHeader.size(); g++) {
                            if (g != groupPosition) {
                                expMyListView.collapseGroup(g);
                            }
                        }
                    }
                });

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    user = childSnapshot.getValue(User.class);
                    if (user != null) {
                        if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                            nomePerfil.setText(user.getName());
                            //drawerLayout
                            String txtLogout = getResources().getString(R.string.logout);
                            String txtHelp = getResources().getString(R.string.help);
                            String txtPerfil = user.getName();
                            String lista = "Minha Lista";
                            String home = "Home";
                            mDataList = new ArrayList();
                            mDataList.add(home);
                            mDataList.add(txtPerfil);
                            mDataList.add(lista);
                            mDataList.add(txtLogout);
                            mDataList.add(txtHelp);
                            mDrawerList.setAdapter(new ArrayAdapter(MinhaListaActivity.this,
                                    R.layout.drawer_list_item, R.id.drawer, mDataList));

                            //lista de jogos jogados
                            List<GamesJogados> minhaLista = user.getGamesJogados();
                            for (int i = 0; i < minhaLista.size(); i++) {
                                if (!myListDataHeader.contains(minhaLista.get(i).getGame().getNome())) {
                                    if (minhaLista.get(i) != null) {
                                        prepareListMyGames(minhaLista.get(i).getGame());
                                    }
                                }
                            }
                            break;
                        }
                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mTitle = mDrawerTitle = getTitle();
        mDataList = new ArrayList<>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, R.id.drawer, mDataList));
        mDrawerList.setOnItemClickListener(new MinhaListaActivity.DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);

        Toolbar toolBar = new Toolbar(this);
        toolBar.setNavigationIcon(R.drawable.ic_drawer);
        mDrawerToggle = new

                ActionBarDrawerToggle(
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
    }

    private void prepareListMyGames(Games games) {

        myListDataHeader.add(games.getNome());
        myListDataChild.put(games.getNome(), games);

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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//Drawer Layout


    private void selectItem(String text) {
        switch (text) {
            case "Home":
                Intent intents = new Intent(this, HomeActivity.class);
                startActivity(intents);
                break;
            case "Logout":
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser users = mAuth.getCurrentUser();
                if (users != null) {
                    mAuth.signOut();
                } else {
                    Toast.makeText(MinhaListaActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Help":
                Toast.makeText(MinhaListaActivity.this, "Não conseguimos lhe fornecer ajuda! Desculpa ;---;", Toast.LENGTH_SHORT).show();
                break;
            case "Minha Lista":
                Intent intent = new Intent(this, MinhaListaActivity.class);
                startActivity(intent);
                break;

        }

        if (user.getName() == text) {
            Toast.makeText(MinhaListaActivity.this, "Olá " + user.getName(), Toast.LENGTH_SHORT).show();

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
            String drawerText = (String) parent.getItemAtPosition(position);
            selectItem(drawerText);
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

    public void moreButtonClickMyList(View view) {

        Intent intent = new Intent(MinhaListaActivity.this, DetailsMyGameListActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);

    }

    public void removerDaMinhaLista(View view) {
        List<GamesJogados> games = user.getGamesJogados();
        Integer posicao = null;
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getGame().getNome().equals(game.getNome())) {
                posicao = i;
            }
        }
        user.getGamesJogados().remove(posicao.intValue());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(mAuth.getCurrentUser().getUid() + "/gamesJogados", user.getGamesJogados());
        drUsers.updateChildren(childUpdates);

    }
}