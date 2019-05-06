package com.example.bloodbank3.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.activities.DashboardActivity;
import com.example.bloodbank3.activities.LoginActivity;
import com.example.bloodbank3.activities.ProfileActivity;
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

public class ProfileFragment extends Fragment {

    private View view;

    private EditText inputemail, inputpassword, inputretypePassword, inputfullName, inputaddress, inputcontact;
    private LinearLayout emailLayout, pwdLayout, confpassLayout;
    private Spinner inputgender, inputbloodgroup, inputdivision;
    private ProgressDialog pd;
    private FirebaseAuth mAuth;
    private Button btnSignup;

    private DatabaseReference db_ref;
    private FirebaseDatabase db_User;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        view = inflater.inflate(R.layout.activity_profile, container, false);

        db_User = FirebaseDatabase.getInstance();
        db_ref = db_User.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        inputfullName = view.findViewById(R.id.input_userName);
        inputgender = view.findViewById(R.id.gender);
        inputbloodgroup = view.findViewById(R.id.bloodGroup);
        inputcontact = view.findViewById(R.id.mobile);
        inputaddress = view.findViewById(R.id.address);
        inputdivision = view.findViewById(R.id.division);
        inputemail = view.findViewById(R.id.input_userEmail);
        inputpassword = view.findViewById(R.id.input_password);
        inputretypePassword = view.findViewById(R.id.input_password_confirm);
        emailLayout = view.findViewById(R.id.emailLayout);
        pwdLayout = view.findViewById(R.id.pwdLayout);
        confpassLayout = view.findViewById(R.id.confpwdLayout);
        btnSignup = view.findViewById(R.id.button_register);

        //Check if user is logged in to display profile information
        if(mAuth.getCurrentUser() != null){
            Log.d("ProfileActivity", "*****mAuth.getCurrentUser() = "+mAuth.getCurrentUser());
            Log.d("ProfileActivity", "*****mAuth.getUid() = "+ mAuth.getUid());

            //Setting profile layout for logged in user, so that user may be able to update profile
            emailLayout.setVisibility(View.GONE);
            pwdLayout.setVisibility(View.GONE);
            confpassLayout.setVisibility(View.GONE);
            btnSignup.setText("Update Profile");
            getActivity().setTitle("Profile");
            pd.dismiss();

            Query Profile = db_ref.child(mAuth.getCurrentUser().getUid());
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
        //Else redirect to login page
        else {
            Toast.makeText(view.getContext(), "Your profile has been updated!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            startActivity(intent);
            pd.dismiss();
        }

        //Update Profile button action
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name = inputfullName.getText().toString();
                final int gender = inputgender.getSelectedItemPosition();
                final int bloodgroup = inputbloodgroup.getSelectedItemPosition();
                final String contact = inputcontact.getText().toString();
                final String address = inputaddress.getText().toString();
                final int division = inputdivision.getSelectedItemPosition();

                String id = mAuth.getCurrentUser().getUid();
                db_ref.child(id).child("Name").setValue(Name);
                db_ref.child(id).child("Gender").setValue(gender);
                db_ref.child(id).child("Contact").setValue(contact);
                db_ref.child(id).child("BloodGroup").setValue(bloodgroup);
                db_ref.child(id).child("Address").setValue(address);
                db_ref.child(id).child("Division").setValue(division);

                Toast.makeText(view.getContext(), "Your profile has been updated!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), DashboardActivity.class);
                startActivity(intent);
                pd.dismiss();
            }
        });

        return view;
    }

}
