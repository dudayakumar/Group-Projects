package com.example.bloodbank3.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bloodbank3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AppointmentActionsActivity extends AppCompatActivity {

//
//    private View view;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        view = inflater.inflate(R.layout.content_dashboard, container, false);
//
//        return view;
//    }

    private DatabaseReference db_ref;
    private FirebaseAuth mAuth;
    TextView appointmentDate,appointmentTime,userId,appointmentId,appointmentStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_actions);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Appointment");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);










//        appointmentDate = findViewById(R.id.appointment_date);
//        appointmentTime = findViewById(R.id.appointment_time);
//        appointmentId = findViewById(R.id.appointmentid);
//        appointmentStatus = findViewById(R.id.appointmentstatus);
//        userId = findViewById(R.id.userid);

//        mAuth = FirebaseAuth.getInstance();
//        String uid = mAuth.getCurrentUser().getUid();
//        Query allAppts = db_ref.child("appointments");
//        allAppts.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snap:dataSnapshot.getChildren()){
//                    AppointmentData apptData = snap.getValue(AppointmentData.class);
//                    Log.d("AppointmentActions","*************snap.getValue()"+snap.getValue());
//                    appointmentDate.setText(apptData.getDate());
//                    appointmentTime.setText(apptData.getTime());
//                   // appointmentId.setText(apptData.());
//                    appointmentStatus.setText(apptData.getAppointmentStatus());
//                    userId.setText(apptData.getUserId());
//
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

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
