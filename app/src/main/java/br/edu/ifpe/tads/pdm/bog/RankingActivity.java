package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.edu.ifpe.tads.pdm.bog.Model.Games;
import br.edu.ifpe.tads.pdm.bog.Model.GamesJogados;
import br.edu.ifpe.tads.pdm.bog.Model.User;

public class RankingActivity extends AppCompatActivity {



    private ArrayList<String> mDataList;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Games game;
    private User user;


    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;
    private ArrayAdapter<Games> adapter;

    private DatabaseReference drGames;
    private DatabaseReference drUsers;

    private ArrayList<User> usuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Ranking");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);



        final FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        usuarios = new ArrayList();

        /*fbDB.setPersistenceEnabled(true);*/
        drGames = fbDB.getReference("games");
        drUsers = fbDB.getReference("users");
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.ranking);



        /*drUsers.keepSynced(true);
        drGames.keepSynced(true);*/

        drUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnpashot : dataSnapshot.getChildren()) {
                    int pontuacao = 0;
                    user = childSnpashot.getValue(User.class);

                    List<GamesJogados> gamesJogados = user.getGamesJogados();
                    for (int i = 0; i < gamesJogados.size(); i++) {
                        switch (user.getGamesJogados().get(i).getStatus()) {
                            case "Desejo Jogar":
                                break;
                            case "Jogando":
                                pontuacao = pontuacao + 1;
                                break;
                            case "Zerado":
                                pontuacao = pontuacao + 5;
                                break;
                        }
                    }

                    user.setPontuacao(pontuacao);
                    usuarios.add(user);

                }


                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    user = childSnapshot.getValue(User.class);


                    if (user != null) {
                        if (mAuth.getCurrentUser().getEmail().equals(user.getEmail())) {
                            String txtLogout = getResources().getString(R.string.logout);
                            String txtHelp = getResources().getString(R.string.help);
                            String txtPerfil = user.getName();
                            String lista = "Minha Lista";
                            String home = "Home";
                            String ranking = "Ranking";
                            mDataList = new ArrayList();
                            mDataList.add(home);
                            mDataList.add(txtPerfil);
                            mDataList.add(lista);
                            mDataList.add(ranking);
                            mDataList.add(txtLogout);
                            mDataList.add(txtHelp);
                            mDrawerList.setAdapter(new ArrayAdapter(RankingActivity.this,
                                    R.layout.drawer_list_item, R.id.drawer, mDataList));
                            break;
                        } //olhar essa linha se der algo errado
                        else {
                            user = null;
                        }
                    } else if (user == null) {
                        String txtLogout = getResources().getString(R.string.logout);
                        String txtHelp = getResources().getString(R.string.help);
                        String txtPerfil = "Anônimo";
                        String ranking = "Ranking";
                        mDataList = new ArrayList();
                        mDataList.add(txtPerfil);
                        mDataList.add(ranking);
                        mDataList.add(txtLogout);
                        mDataList.add(txtHelp);
                        mDrawerList.setAdapter(new ArrayAdapter(RankingActivity.this,
                                R.layout.drawer_list_item, R.id.drawer, mDataList));
                        break;
                    }

                }//Fazendo oa ordenaçao pela pontuaçao dos usuarios
                Collections.sort(usuarios,new Comparator() {
                    public int compare(Object o1, Object o2) {
                        User p1 = (User) o1;
                        User p2 = (User) o2;
                        return p1.getPontuacao() > p2.getPontuacao() ? -1 :
                                (p1.getPontuacao() < p2.getPontuacao() ? +1 : 0);
                    }
                });
                for (int i = 0; i < usuarios.size(); i++) {
                    TableRow tableRow = new TableRow(RankingActivity.this);
                    TextView colocacao = new TextView(RankingActivity.this);
                    colocacao.setTextSize(20);
                    TextView usuario = new TextView(RankingActivity.this);
                    usuario.setGravity(Gravity.CENTER);
                    usuario.setTextSize(20);
                    TextView pontuacao = new TextView(RankingActivity.this);
                    pontuacao.setTextSize(20);
                    colocacao.setText(""+(i+1)+"º");
                    usuario.setText(usuarios.get(i).getName());
                    pontuacao.setText(""+usuarios.get(i).getPontuacao()+" pontos");
                    tableRow.addView(colocacao);
                    tableRow.addView(usuario);
                    tableRow.addView(pontuacao);
                    tableLayout.addView(tableRow);

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
        mDrawerList.setOnItemClickListener(new RankingActivity.DrawerItemClickListener());

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
                finish();
                break;
            case "Logout":
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser users = mAuth.getCurrentUser();
                if (users != null) {
                    mAuth.signOut();
                } else {
                    Toast.makeText(RankingActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Help":
                Toast.makeText(RankingActivity.this, "Não conseguimos lhe fornecer ajuda! Desculpa ;---;", Toast.LENGTH_SHORT).show();
                break;
            case "Minha Lista":
                Intent intent = new Intent(this, MinhaListaActivity.class);
                startActivity(intent);
                break;
            case "Ranking":
                intent = new Intent(this, RankingActivity.class);
                startActivity(intent);
                break;

        }

        if (user.getName() == text) {
            Toast.makeText(RankingActivity.this, "Olá " + user.getName(), Toast.LENGTH_SHORT).show();

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


}
