package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifpe.tads.pdm.bog.Model.Comentario;
import br.edu.ifpe.tads.pdm.bog.Model.Games;
import br.edu.ifpe.tads.pdm.bog.Model.GamesJogados;
import br.edu.ifpe.tads.pdm.bog.Model.User;

public class DetailsMyGameListActivity extends AppCompatActivity {
    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;
    private ImageView fotinha;

    private User user;

    private User tempUser;


    private Games game;



    private DatabaseReference drUsers;
    private DatabaseReference drGames;
    private DatabaseReference drComent;

    private String idGame;

    private ArrayList<String> mDataList;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_my_game_list);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);
        final FirebaseDatabase fbDB = FirebaseDatabase.getInstance();

        drUsers = fbDB.getReference("users");
        drGames = fbDB.getReference("games");
        drComent = fbDB.getReference("comments");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        game = (Games) intent.getSerializableExtra("game");


        //TextView name = (TextView) findViewById(R.id.game_name);
        final TextView starRate = (TextView) findViewById(R.id.rate);
        TextView descricacao = (TextView) findViewById(R.id.descricao);
        TextView categoria = (TextView) findViewById(R.id.categoria);
        TextView desenvolVedor = (TextView) findViewById(R.id.desenvolvedor);
        TextView numjogadores = (TextView) findViewById(R.id.numjogadores);
        TextView plataforma = (TextView) findViewById(R.id.plataforma);
        TextView linguagem = (TextView) findViewById(R.id.linguagem);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Map<String, Object> childUpdates = new HashMap<>();


                List<GamesJogados> gamesJogados = user.getGamesJogados();
                for (int i = 0; i < gamesJogados.size(); i++) {
                    if (gamesJogados.get(i).getGame().getNome().equals(game.getNome())) {
                        gamesJogados.get(i).setAvaliacao(ratingBar.getRating());
                        childUpdates.put(mAuth.getCurrentUser().getUid() + "/gamesJogados", user.getGamesJogados());
                        drUsers.updateChildren(childUpdates);

                    }
                }
            }
        });


        starRate.setText("Rank: " + game.getRatingBar() + "/5.0");
        descricacao.setText("Descrição: \n" + game.getDescricao());
        categoria.setText("Categoria: " + game.getCategoria());
        desenvolVedor.setText("Desenvolvedor: " + game.getDesenvolvedor());
        String numero_jogadores = "Número de Jogadores: ";

        for (int i = 0; i < game.getNum_jogadores().size(); i++) {
            numero_jogadores = numero_jogadores + game.getNum_jogadores().get(i) + ",";
        }
        numjogadores.setText(numero_jogadores);
        plataforma.setText("Plataforma: " + game.getPlataforma());
        String linguagemTexto = "Linguagem: ";
        for (int i = 0; i < game.getLinguagem().size(); i++) {
            linguagemTexto = linguagemTexto + game.getLinguagem().get(i) + ",";
        }

        linguagem.setText(linguagemTexto);
        setTitle(game.getNome());


        StorageReference storageRef = storage.getReferenceFromUrl("gs://bogproject-9a0af.appspot.com/").child(game.getImagem());
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    ImageView mImageView = (ImageView) findViewById(R.id.fotinha);
                    mImageView.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }
        drUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    user = childSnapshot.getValue(User.class);


                    final RadioButton radioDesejoJogar = (RadioButton) findViewById(R.id.radio_Desejo_Jogar);
                    final RadioButton radioZerado = (RadioButton) findViewById(R.id.radio_Zerado);
                    final RadioButton radioJogando = (RadioButton) findViewById(R.id.radio_Jogando);
                    if (user != null) {
                        if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                            List<GamesJogados> gamesJogados = user.getGamesJogados();
                            for (int i = 0; i < gamesJogados.size(); i++) {
                                if (gamesJogados.get(i).getGame().getNome().equals(game.getNome())) {
                                    ratingBar.setRating(user.getGamesJogados().get(i).getAvaliacao());
                                    GamesJogados gameUpdate = user.getGamesJogados().get(i);
                                    String status = gameUpdate.getStatus();
                                    if (status != null) {
                                        if (status.equals("Jogando")) {
                                            radioJogando.setChecked(true);
                                            radioDesejoJogar.setChecked(false);
                                            radioZerado.setChecked(false);
                                        } else if (status.equals("Desejo Jogar")) {
                                            radioDesejoJogar.setChecked(true);
                                            radioJogando.setChecked(false);
                                            radioZerado.setChecked(false);
                                        } else if (status.equals("Zerado")) {
                                            radioZerado.setChecked(true);
                                            radioJogando.setChecked(false);
                                            radioDesejoJogar.setChecked(false);
                                        }
                                    }

                                    break;
                                }
                            }
                            //drawerLayout
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
                            mDrawerList.setAdapter(new ArrayAdapter(DetailsMyGameListActivity.this,
                                    R.layout.drawer_list_item, R.id.drawer, mDataList));
                            break;

                        }
                    }

                }
                float ratingGlobal = game.getRatingBar();
                //já tem o voto do cadastro
                int votos = 1;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    User usario = childSnapshot.getValue(User.class);
                    //voto recem cadastrado


                    for (int i = 0; i < usario.getGamesJogados().size(); i++) {
                        if (usario.getGamesJogados().get(i).getGame().getNome().equals(game.getNome())) {
                            if (usario.getGamesJogados().get(i).getAvaliacao() > 0) {
                                ratingGlobal = ratingGlobal + usario.getGamesJogados().get(i).getAvaliacao();
                                votos++;
                            }
                        }
                    }
                }
                ratingGlobal = ratingGlobal / votos;
                float casaDecimal = (ratingGlobal * 10) % 10;
                if ((casaDecimal >= 3) && (casaDecimal <= 7)) {
                    ratingGlobal = ((ratingGlobal * 10) - casaDecimal) + 5;
                    ratingGlobal = ratingGlobal / 10;
                    starRate.setText(("Avaliação Geral:" + ratingGlobal + "/5"));
                } else {
                    starRate.setText(("Avaliação Geral:" + Math.round(ratingGlobal) + "/5"));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        drGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Games extra = childSnapshot.getValue(Games.class);
                    if (extra.getNome().equals(game.getNome())) {
                        idGame = childSnapshot.getKey();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        drComent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final Comentario comentario = childSnapshot.getValue(Comentario.class);

                    if (idGame.equals(comentario.getIdGame())) {

                        final DatabaseReference drUsers = fbDB.getReference("users").child(comentario.getIdUsuario());
                        drUsers.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.comentario);
                                tempUser = dataSnapshot.getValue(User.class);
                                TextView textView = new TextView(DetailsMyGameListActivity.this);
                                textView.setText(tempUser.getName() + ": " + comentario.getTexto());
                                textView.setTextSize(20);
                                linearLayout.addView(textView);
                                drUsers.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //drawerLayout

        mTitle = mDrawerTitle = getTitle();
        mDataList = new ArrayList<>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, R.id.drawer, mDataList));
        mDrawerList.setOnItemClickListener(new DetailsMyGameListActivity.DrawerItemClickListener());

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
                    Toast.makeText(DetailsMyGameListActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Help":
                Toast.makeText(DetailsMyGameListActivity.this, "Não conseguimos lhe fornecer ajuda! Desculpa ;---;", Toast.LENGTH_SHORT).show();
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

        if (user.getName().equals(text)) {
            Toast.makeText(DetailsMyGameListActivity.this, "Olá " + user.getName(), Toast.LENGTH_SHORT).show();

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

    public void onRadioJogando(View v) {
        Map<String, Object> childUpdates = new HashMap<>();
        List<GamesJogados> gamesJogados = user.getGamesJogados();
        RadioButton radioJogando = (RadioButton) findViewById(R.id.radio_Jogando);
        for (int i = 0; i < gamesJogados.size(); i++) {
            if (gamesJogados.get(i).getGame().getNome().equals(game.getNome())) {
                gamesJogados.get(i).setStatus(radioJogando.getText().toString());
                childUpdates.put(mAuth.getCurrentUser().getUid() + "/gamesJogados", user.getGamesJogados());
                drUsers.updateChildren(childUpdates);
            }
        }
    }

    public void onRadioDesejo(View v) {
        Map<String, Object> childUpdates = new HashMap<>();
        List<GamesJogados> gamesJogados = user.getGamesJogados();
        RadioButton radioDesejoJogar = (RadioButton) findViewById(R.id.radio_Desejo_Jogar);
        for (int i = 0; i < gamesJogados.size(); i++) {
            if (gamesJogados.get(i).getGame().getNome().equals(game.getNome())) {
                gamesJogados.get(i).setStatus(radioDesejoJogar.getText().toString());
                childUpdates.put(mAuth.getCurrentUser().getUid() + "/gamesJogados", user.getGamesJogados());
                drUsers.updateChildren(childUpdates);
            }
        }
    }

    public void onRadioZerado(View v) {
        Map<String, Object> childUpdates = new HashMap<>();
        List<GamesJogados> gamesJogados = user.getGamesJogados();

        RadioButton radioZerado = (RadioButton) findViewById(R.id.radio_Zerado);
        for (int i = 0; i < gamesJogados.size(); i++) {
            if (gamesJogados.get(i).getGame().getNome().equals(game.getNome())) {
                gamesJogados.get(i).setStatus(radioZerado.getText().toString());
                childUpdates.put(mAuth.getCurrentUser().getUid() + "/gamesJogados", user.getGamesJogados());
                drUsers.updateChildren(childUpdates);
            }
        }
    }

    public void adicionarComentario(View v) {
        EditText editText = (EditText) findViewById(R.id.Add_comentario);
        String texto = editText.getText().toString();
        String idUsuario = mAuth.getCurrentUser().getUid();
        Comentario comentario = new Comentario(texto, idGame, idUsuario);
        drComent.push().setValue(comentario);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.comentario);
        linearLayout.removeAllViews();
        Toast.makeText(DetailsMyGameListActivity.this, "Comentario Adicionado", Toast.LENGTH_SHORT).show();
        editText.setText("");

    }


}
