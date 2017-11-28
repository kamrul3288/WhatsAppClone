package com.kamrulhasan3288.whatsappclone.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kamrulhasan3288.whatsappclone.Manifest;
import com.kamrulhasan3288.whatsappclone.R;

public class LoginActivity extends AppCompatActivity {

    /**
     *------xml instance-----------
     **/
    private EditText userEmailText,userPasswordText;


    /**
     *-------class instance----------
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         *---------initialize xml instance--------------
         **/
        userEmailText = findViewById(R.id.login_activity_email);
        userPasswordText = findViewById(R.id.login_activity_password);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
            }
        };
    }

    /**
     * ----Executed when login button pressed------------
     *-----app login button------------------
     **/
    public void attemptLogin(View view){
        String email = userEmailText.getText().toString();
        final String password = userPasswordText.getText().toString();

        if (email.isEmpty()){
            userEmailText.setError("Enter an email");
            return;
        }

        if (password.isEmpty()){
            userPasswordText.setError("Enter Password");
        }

       if (!email.isEmpty() && !password.isEmpty()){
           final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
           dialog.setMessage("Attempting to loading...");
           dialog.setCancelable(false);
           dialog.show();

           mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()){
                       Toast.makeText(getApplicationContext(),"login success",Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(LoginActivity.this, MainActivity.class));
                   }
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   dialog.dismiss();
                   Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
               }
           });
       }
    }

    /**
     * ----Executed when register button pressed------------
     **/
    public void registerNewUser(View view){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
