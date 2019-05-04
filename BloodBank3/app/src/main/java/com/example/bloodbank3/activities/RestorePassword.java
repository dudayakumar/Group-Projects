package com.example.bloodbank3.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

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
}




//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//import com.example.bloodbank3.R;
//
//public class RestorePassword extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_restore_password);
//
//    }
//}
