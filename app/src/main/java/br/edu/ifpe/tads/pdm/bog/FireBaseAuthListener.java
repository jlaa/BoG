package br.edu.ifpe.tads.pdm.bog;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseAuthListener implements FirebaseAuth.AuthStateListener {

    private final Activity activity;

    public FireBaseAuthListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Intent intent = null;
        if ((user != null) && ((activity instanceof SignInActivity)||(activity instanceof SignUpActivity))) {
            intent = new Intent(activity, HomeActivity.class);
        }
        if((user==null)&&(!(activity instanceof SignInActivity)&&!(activity instanceof SignUpActivity)))
        {
            intent = new Intent(activity,SignInActivity.class);
        }
        if(intent !=null)
        {
         activity.startActivity(intent);
         activity.finish();
        }
    }

}
