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
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;

import br.edu.ifpe.tads.pdm.bog.Model.Games;
import br.edu.ifpe.tads.pdm.bog.Model.User;

public class DetailsGameActivity extends AppCompatActivity {
    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;
    private ImageView fotinha;

    private User user;
    private Games game;

    private DatabaseReference drUsers;


    private ArrayList<String> mDataList;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_game);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);
        final FirebaseDatabase fbDB = FirebaseDatabase.getInstance();

        drUsers = fbDB.getReference("users");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        game = (Games) intent.getSerializableExtra("game");


        final List<String> status = new ArrayList();

        //TextView name = (TextView) findViewById(R.id.game_name);
        final TextView starRate = (TextView) findViewById(R.id.rate);
        TextView descricacao = (TextView) findViewById(R.id.descricao);
        TextView categoria = (TextView) findViewById(R.id.categoria);
        TextView desenvolVedor = (TextView) findViewById(R.id.desenvolvedor);
        TextView numjogadores = (TextView) findViewById(R.id.numjogadores);
        TextView plataforma = (TextView) findViewById(R.id.plataforma);
        TextView linguagem = (TextView) findViewById(R.id.linguagem);


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
                    dataSnapshot.getRef();
                    if (user != null) {
                        if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {

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
                            mDrawerList.setAdapter(new ArrayAdapter(DetailsGameActivity.this,
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
                    ratingGlobal = (((ratingGlobal * 10) - casaDecimal)) + 5;
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
        mDrawerList.setOnItemClickListener(new DetailsGameActivity.DrawerItemClickListener());

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
                    Toast.makeText(DetailsGameActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Help":
                Toast.makeText(DetailsGameActivity.this, "Não conseguimos lhe fornecer ajuda! Desculpa ;---;", Toast.LENGTH_SHORT).show();
                break;
            case "Minha Lista":
                Intent intent = new Intent(this, MinhaListaActivity.class);
                startActivity(intent);
                break;

        }

        if (user.getName().equals(text)) {
            Toast.makeText(DetailsGameActivity.this, "Olá " +user.getName(), Toast.LENGTH_SHORT).show();

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

}
