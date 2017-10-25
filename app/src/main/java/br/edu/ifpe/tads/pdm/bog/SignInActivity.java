package br.edu.ifpe.tads.pdm.bog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);
        setTitle("Bank Of Games");
    }

    public void buttonSingInClick(View v) {
        EditText edEmail = (EditText) findViewById(R.id.email_login);
        EditText edPassword = (EditText) findViewById(R.id.senha_login);

        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String msg ;
                if (task.isSuccessful()) {
                    msg = "SIGN IN OK!";
                    Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    msg= "SIGN IN ERROR!";
                    Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

            }
        });
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
