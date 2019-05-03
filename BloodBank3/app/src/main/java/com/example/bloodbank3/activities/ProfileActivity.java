package com.example.bloodbank3.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText inputemail, inputpassword, inputretypePassword, inputfullName, inputaddress, inputcontact;
    private Spinner inputgender, inputbloodgroup, inputdivision;
    private ProgressDialog pd;
    private FirebaseAuth mAuth;
    private Button btnSignup;

    private boolean isUpdate = false;

    private DatabaseReference db_ref;
    private FirebaseDatabase db_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        setContentView(R.layout.activity_profile);

        db_User = FirebaseDatabase.getInstance();
        db_ref = db_User.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        inputfullName = findViewById(R.id.input_userName);
        inputgender = findViewById(R.id.gender);
        inputbloodgroup = findViewById(R.id.bloodGroup);
        inputcontact = findViewById(R.id.mobile);
        inputaddress = findViewById(R.id.address);
        inputdivision = findViewById(R.id.division);
        inputemail = findViewById(R.id.input_userEmail);
        inputpassword = findViewById(R.id.input_password);
        inputretypePassword = findViewById(R.id.input_password_confirm);
        btnSignup = findViewById(R.id.button_register);

        //Check if user is logged in to display profile information
        if(mAuth.getCurrentUser() != null){
            Log.d("ProfileActivity", "*****mAuth.getCurrentUser() = "+mAuth.getCurrentUser());
            Log.d("ProfileActivity", "*****mAuth.getUid() = "+ mAuth.getUid());

            //Setting profile layout for logged in user, so that user may be able to update profile
            inputemail.setVisibility(View.GONE);
            inputpassword.setVisibility(View.GONE);
            inputretypePassword.setVisibility(View.GONE);
            btnSignup.setText("Update Profile");
            getSupportActionBar().setTitle("Profile");
            pd.dismiss();
            isUpdate = true;

            Query Profile =db_ref.child(mAuth.getCurrentUser().getUid());
            Profile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserData userData = dataSnapshot.getValue(UserData.class);

                    if(userData != null){
                        pd.show();
                        //Displaying user's profile information
                        inputfullName.setText(userData.getName());
                        inputgender.setSelection(userData.getGender());
                        inputaddress.setText(userData.getAddress());
                        inputcontact.setText(userData.getContact());
                        inputbloodgroup.setSelection(userData.getBloodGroup());
                        inputdivision.setSelection(userData.getDivision());
                        pd.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("ProfileActivity", "*****Error in loading user profile information: "+databaseError.getMessage());
                }
            });
        }
        else {
            pd.dismiss();
            Log.d("ProfileActivity", "*****mAuth.getCurrentUser() = "+mAuth.getCurrentUser());
            btnSignup.setText("Register");
            getSupportActionBar().setTitle("Register");
        }

        //Sign-up button action
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name = inputfullName.getText().toString();
                final int gender = inputgender.getSelectedItemPosition();
                final int bloodgroup = inputbloodgroup.getSelectedItemPosition();
                final String contact = inputcontact.getText().toString();
                final String address = inputaddress.getText().toString();
                final int division = inputdivision.getSelectedItemPosition();
                final String email = inputemail.getText().toString();
                final String password = inputpassword.getText().toString();
                final String confirmpassword = inputretypePassword.getText().toString();

                //Store profile information in database if isUpdate = false i.e. New registration
                if(!isUpdate) {
                    pd.show();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Registration failed! Please try again.", Toast.LENGTH_LONG)
                                                .show();
                                        Log.d("*****error", task.getException().getMessage());
                                    } else {
                                        String id = mAuth.getCurrentUser().getUid();
                                        db_ref.child(id).child("Email").setValue(email);
                                        db_ref.child(id).child("Name").setValue(Name);
                                        db_ref.child(id).child("Gender").setValue(gender);
                                        db_ref.child(id).child("Contact").setValue(contact);
                                        db_ref.child(id).child("BloodGroup").setValue(bloodgroup);
                                        db_ref.child(id).child("Address").setValue(address);
                                        db_ref.child(id).child("Division").setValue(division);
                                        Log.d("ProfileActivity", "*****createUserWithEmail:success");
                                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                        startActivity(intent);
                                        Log.d("ProfileActivity", "*****Navigating to DashboardActivity");
                                        finish();
                                    }
                                    pd.dismiss();
                                }
                            });
                }
                //Update profile information in database if isUpdate = true i.e. profile update
                else {
                    String id = mAuth.getCurrentUser().getUid();
                    db_ref.child(id).child("Name").setValue(Name);
                    db_ref.child(id).child("Gender").setValue(gender);
                    db_ref.child(id).child("Contact").setValue(contact);
                    db_ref.child(id).child("BloodGroup").setValue(bloodgroup);
                    db_ref.child(id).child("Address").setValue(address);
                    db_ref.child(id).child("Division").setValue(division);

                    Toast.makeText(getApplicationContext(), "Your profile has been updated!", Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    pd.dismiss();
                }
            }
        });
    }
}
