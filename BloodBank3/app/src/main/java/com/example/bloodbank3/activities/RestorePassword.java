package com.example.bloodbank3.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RestorePassword extends AppCompatActivity {

    ProgressBar progressBar;
    EditText email;
    Button sendPassword;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        //Action bar back button navigation logic
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        email = findViewById(R.id.resetUsingEmail);
        sendPassword = findViewById(R.id.resetPassbtn);

        firebaseAuth = FirebaseAuth.getInstance();
        sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RestorePassword.this,"Email Sent for Password Reset",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RestorePassword.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });

    }

    //Minimize application on pressing back button
    @Override
    public void onBackPressed(){
        this.moveTaskToBack(true);
    }

    //Navigate to previous activity on pressing action bar's back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}