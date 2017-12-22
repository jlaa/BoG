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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.tads.pdm.bog.Model.GamesJogados;
import br.edu.ifpe.tads.pdm.bog.Model.User;

public class SignUpActivity extends AppCompatActivity {
    private FireBaseAuthListener authListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.mAuth = FirebaseAuth.getInstance();
        this.authListener = new FireBaseAuthListener(this);
        setTitle("Bank Of Games");
    }

    public void buttonSingUpClick(View view) {
        final List<GamesJogados> gamesJogados = new ArrayList();
        EditText edName = (EditText) findViewById(R.id.name_register);
        EditText edMail = (EditText) findViewById(R.id.email_register);
        EditText edPassword = (EditText) findViewById(R.id.senha_register);
        if(!(edName.getText().toString().equals(""))&&!(edMail.getText().toString().equals(""))
                &&!(edPassword.getText().toString().equals(""))) {
        final String name = edName.getText().toString();
        final String email = edMail.getText().toString();
        final String password = edPassword.getText().toString();
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String msg = task.isSuccessful() ? "SIGN UP OK!" : "SIGN UP ERROR!";
                    Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {
                        User tempUser = new User(name, email, gamesJogados);
                        DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("users");
                        drUsers.child(mAuth.getCurrentUser().getUid()).setValue(tempUser);
                    }
                }
            });
        }else
            {
                Toast.makeText(SignUpActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
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
