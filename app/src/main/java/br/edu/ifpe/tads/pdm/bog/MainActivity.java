package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);
    }

    public void toLogin(View view)
    {
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);

    }

    public void toRegister(View view)
    {
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);

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
