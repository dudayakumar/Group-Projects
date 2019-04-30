package com.example.bloodbank3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button signin, signup, resetpass;
    private EditText inputemail, inputpassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputemail = findViewById(R.id.input_username);
        inputpassword = findViewById(R.id.input_password);

        signin = findViewById(R.id.button_login);
        signup = findViewById(R.id.button_register);
        resetpass = findViewById(R.id.button_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputemail.getText().toString()+"";
                final String password = inputpassword.getText().toString()+"";

                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG).show();
                                    Log.d("*****error", task.getException().getMessage());
                                } else {
                                    Log.d("*****success", "User authenticated successfully");
                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                    startActivity(intent);
                                    Log.d("LoginActivity","*****Navigating to DashboardActivity");
                                }
                            }
                        });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                Log.d("LoginActivity","*****Navigating to ProfileActivity");
            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RestorePassword.class);
                startActivity(intent);
                Log.d("LoginActivity","*****Navigating to RestorePassword");
            }
        });
    }
}
