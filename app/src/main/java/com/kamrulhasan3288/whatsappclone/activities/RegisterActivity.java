package com.kamrulhasan3288.whatsappclone.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kamrulhasan3288.whatsappclone.R;
import com.kamrulhasan3288.whatsappclone.infrastructure.Utils;

public class RegisterActivity extends AppCompatActivity {

    /**
     *-------xml instance--------------
     **/
    private EditText userName,userEmail,userPassword,userRePassword;

    /**
     *-------class instance-----
     * ------fire base instance---------------
     **/
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**
         *-----initialize xml instance--------------
         **/
        userName = findViewById(R.id.register_activity_userName);
        userEmail  = findViewById(R.id.register_activity_email);
        userPassword = findViewById(R.id.register_activity_password);
        userRePassword = findViewById(R.id.register_activity_rePassword);

        mAuth = FirebaseAuth.getInstance();
    }

    /**
     *---------register the user----------
     **/
    public void attemptToRegister(View view){
        if (userName.getText().toString().isEmpty()){
            userName.setError("Enter your name");
        }
        if (userEmail.getText().toString().isEmpty()){
            userEmail.setError("Enter your email");
        }else if (!isEmailValid(userEmail.getText().toString())){
            userEmail.setError("Enter valid email");
        }

        if (userPassword.getText().toString().isEmpty()){
            userPassword.setError("Enter password");
        }else if (!isPasswordIsValid(userPassword.getText().toString())){
            userPassword.setError("Enter valid password");
            userRePassword.setError("Enter valid password");

        }

        if (!userName.getText().toString().isEmpty() && isEmailValid(userEmail.getText().toString()) && isPasswordIsValid(userPassword.getText().toString())){
            createFirebaseUser();
        }
    }

    /**
     *----------save user name-------------
     **/
    public void saveDisplayName(String userName){
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Utils.USER_NAME,userName).apply();
    }

    /**
     *-------back to login activity------------
     **/
    public void loginNow(View view){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
    }


    /**
     *------create firebase new user---------
     **/
    public void createFirebaseUser(){
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    saveDisplayName(userName.getText().toString());
                    mAuth.signOut();
                    Toast.makeText(getApplicationContext(),"Account is registered",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    /**
     *-------check validation of email not a real way just manually-----
     **/
    public boolean isEmailValid(String email){
        return email.contains("@");
    }

    /**
     *--------check password validation------
     **/
    public boolean isPasswordIsValid(String password){
        String rePassword = userRePassword.getText().toString();
        return rePassword.equals(password) && password.length()>=5;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
