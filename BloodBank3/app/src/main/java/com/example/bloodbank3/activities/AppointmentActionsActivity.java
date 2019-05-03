package com.example.bloodbank3.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.bloodbank3.R;
import com.example.bloodbank3.models.AppointmentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AppointmentActionsActivity extends AppCompatActivity {

    private DatabaseReference db_ref;
    private FirebaseAuth mAuth;
    TextView appointmentDate,appointmentTime,userId,appointmentId,appointmentStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_actions);
        appointmentDate = findViewById(R.id.appointment_date);
        appointmentTime = findViewById(R.id.appointment_time);
        appointmentId = findViewById(R.id.appointmentid);
        appointmentStatus = findViewById(R.id.appointmentstatus);
        userId = findViewById(R.id.userid);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        Query allAppts = db_ref.child("appointments");
        allAppts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    AppointmentData apptData = snap.getValue(AppointmentData.class);
                    Log.d("AppointmentActions","*************snap.getValue()"+snap.getValue());
                    appointmentDate.setText(apptData.getDate());
                    appointmentTime.setText(apptData.getTime());
                   // appointmentId.setText(apptData.());
                    appointmentStatus.setText(apptData.getAppointmentStatus());
                    userId.setText(apptData.getUserId());



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
