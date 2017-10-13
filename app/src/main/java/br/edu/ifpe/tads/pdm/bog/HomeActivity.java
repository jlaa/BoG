package br.edu.ifpe.tads.pdm.bog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private static final String [] games = {"League of legends", "Paladins", "Overwatch", "Mortal Kombat",
            "The Last of Us", "Dark Souls", "Until Dawn", "Assassins Creed", "AION", "Grand Chase", "Sonic",
            "Super Mario World"};

    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);
        ListView listView = (ListView)findViewById(R.id.list_games);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, games));
    }

    public void buttonSignOutClick(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signOut();
        } else {
            Toast.makeText(HomeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
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
