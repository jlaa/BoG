package br.edu.ifpe.tads.pdm.bog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
