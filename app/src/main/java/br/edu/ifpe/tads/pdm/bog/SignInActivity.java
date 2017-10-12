package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void buttonSingInClick(View v) {
        EditText edEmail = (EditText) findViewById(R.id.email_login);
        EditText edPassword = (EditText) findViewById(R.id.senha_login);
        Intent intent = new Intent(this, HomeActivity.class);
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String msg = task.isSuccessful() ? "SIGN IN OK!" : "SIGN IN ERROR!";
                Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(intent);
        finish();
    }
}
