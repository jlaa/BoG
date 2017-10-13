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

public class SignUpActivity extends AppCompatActivity {
    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);
    }

    public void buttonSingUpClick(View view) {
        EditText edMail = (EditText) findViewById(R.id.email_register);
        EditText edPassword = (EditText) findViewById(R.id.senha_register);
        String email = edMail.getText().toString();
        String password = edPassword.getText().toString();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String msg = task.isSuccessful() ? "SIGN UP OK!" : "SIGN UP ERROR!";
                Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
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
