package com.example.bloodbank3.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.fragments.BookApppointmentFragment;
import com.example.bloodbank3.fragments.HomeFragment;
import com.example.bloodbank3.models.AppointmentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AppointmentActionsActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private DatabaseReference db_ref;
    private Button statusBtn,rescheduleBtn;
    private FirebaseAuth mAuth;
    TextView appointmentDate,appointmentTime,userId,appointmentId,appointmentStatus;
    String userid,apptDate,apptTime,apptId,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_actions);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Appointment");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Bundle databundle = getIntent().getExtras();
        if(databundle!=null){
            userid = databundle.getString("userid");
            apptDate = databundle.getString("appointmentDate");
            apptTime = databundle.getString("appointmentTime");
            apptId = databundle.getString("appointmentId");
            status = databundle.getString("appointmentStatus");
        }

        appointmentDate = findViewById(R.id.appointment_date);
        appointmentTime = findViewById(R.id.appointment_time);
        appointmentId = findViewById(R.id.appointment_id);
        appointmentStatus = findViewById(R.id.appointment_status);
        statusBtn = findViewById(R.id.statusBtn);
        rescheduleBtn = findViewById(R.id.rescheduleBtn);
        userId = findViewById(R.id.user_id);
        builder = new AlertDialog.Builder(this);
        mAuth = FirebaseAuth.getInstance();

        appointmentDate.setText(apptDate);
        appointmentTime.setText(apptTime);
        appointmentId.setText(apptId);
        appointmentStatus.setText(status);
        userId.setText(userid);

        if(mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){
            rescheduleBtn.setVisibility(View.GONE);
        }
        if(!mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){
            statusBtn.setVisibility(View.GONE);
        }

        Log.d("AppointmentActions","*************apptId: "+apptId);
        Log.d("AppointmentActions","*************appointmentId: "+appointmentId.getText().toString());

        //Populating the appointment details
        mAuth = FirebaseAuth.getInstance();
        db_ref = FirebaseDatabase.getInstance().getReference();

        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Would you like to approve the appointment?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(appointmentStatus.getText().toString().equals("Pending")) {
                            appointmentStatus.setText("Approved");
                            Toast.makeText(getApplicationContext(), "Appointment Status is Approved!", Toast.LENGTH_LONG).show();
                            db_ref.child("appointments").child(apptId).child("appointmentStatus").setValue("Approved");

                            Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Appointment has already been approved/rejected", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(appointmentStatus.getText().toString().equals("Pending")) {
                            appointmentStatus.setText("Rejected");
                            Toast.makeText(getApplicationContext(), "Appointment Status is Rejected!", Toast.LENGTH_LONG).show();
                            db_ref.child("appointments").child(apptId).child("appointmentStatus").setValue("Rejected");

                            Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Appointment has already been approved/rejected", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Appointment Status");
                alertDialog.show();

            }
        });

        rescheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavigationView nv = (NavigationView)findViewById(R.id.nav_view);

//                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//                startActivity(intent); //working

//                navigateToFragment();

//                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new BookApppointmentFragment()).commit();
//                nv.getMenu().getItem(0).setChecked(true);



//                FragmentTransaction ft = fm.beginTransaction();
//                ft.add(R.id.activity_appointment_actions, )
            }
        });

    }

    private void navigateToFragment() {
        Log.d("AppointmentActions","*****Entered navigateToFragment()");
        Intent intent = new Intent(getApplicationContext(), BookApppointmentFragment.class);
        startActivity(intent);
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
