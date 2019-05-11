package com.example.bloodbank3.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.fragments.BookApppointmentFragment;
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
    private LinearLayout linearLayout,adminDisplay;
    TextView appointmentDate,appointmentTime,userId,appointmentId,appointmentStatus;
    String userid,apptDate,apptTime,apptId,status,username;
    private TextView answer1,answer2,answer3,answer4,answer5,answer6,answer7,answer8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_actions);

        adminDisplay = findViewById(R.id.admindisplay);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Appointment");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Bundle databundle = getIntent().getExtras();
        if(databundle!=null){
            username = databundle.getString("username");
            apptDate = databundle.getString("appointmentDate");
            apptTime = databundle.getString("appointmentTime");
            apptId = databundle.getString("appointmentId");
            status = databundle.getString("appointmentStatus");
            userid = databundle.getString("user_id");
            Log.d("AppointmentActions","*****user_id"+userid);
            Log.d("AppointmentActions","*****username"+username);
        }

        appointmentDate = findViewById(R.id.appointment_date);
        appointmentTime = findViewById(R.id.appointment_time);
        appointmentId = findViewById(R.id.appointment_id);
        appointmentStatus = findViewById(R.id.appointment_status);
        statusBtn = findViewById(R.id.statusBtn);
        rescheduleBtn = findViewById(R.id.rescheduleBtn);
        userId = findViewById(R.id.user_id);
        builder = new AlertDialog.Builder(this);
        linearLayout = findViewById(R.id.innerLinearLayout);
        adminDisplay = findViewById(R.id.admindisplay);

        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        answer5 = findViewById(R.id.answer5);
        answer6 = findViewById(R.id.answer6);
        answer7 = findViewById(R.id.answer7);
        answer8 = findViewById(R.id.answer8);


        mAuth = FirebaseAuth.getInstance();

        appointmentDate.setText(apptDate);
        appointmentTime.setText(apptTime);
        appointmentId.setText(apptId);
        appointmentStatus.setText(status);
        userId.setText(username);

        Log.d("AppointmentActions","*****appointmentDate"+appointmentDate.getText().toString());
        Log.d("AppointmentActions","*****appointmentTime"+appointmentTime.getText().toString());
        Log.d("AppointmentActions","*****appointmentId"+appointmentId.getText().toString());
        Log.d("AppointmentActions","*****appointmentStatus"+appointmentStatus.getText().toString());
        Log.d("AppointmentActions","*****user name"+userId.getText().toString());

        if(!mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){
            adminDisplay.setVisibility(View.GONE);
        }
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

        Query allAppts = db_ref.child("questionnaire");



        allAppts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    for (DataSnapshot singleQuestion: dataSnapshot.getChildren()){
                        Log.d("AppointmentAct","****singleQuestion.getValue() : "+singleQuestion.getValue());
                        Log.d("AppointmentAct","****singleQuestion.getKey() : "+singleQuestion.getKey());
                        Log.d("AppointmentAct","****singleQuestion.getValue() ans1 : "+singleQuestion.child("ans1").getValue());
                        Log.d("AppointmentAct","****singleQuestion.getValue() ans2 : "+singleQuestion.child("ans2").getValue());
                        Log.d("AppointmentAct","****userid : "+userid);
                        if(singleQuestion.getKey().equals(userid) ){
                            answer1.setText(singleQuestion.child("ans1").getValue().toString());
                            answer2.setText(singleQuestion.child("ans2").getValue().toString());
                            answer3.setText(singleQuestion.child("ans3").getValue().toString());
                            answer4.setText(singleQuestion.child("ans4").getValue().toString());
                            answer5.setText(singleQuestion.child("ans5").getValue().toString());
                            answer6.setText(singleQuestion.child("ans6").getValue().toString());
                            answer7.setText(singleQuestion.child("ans7").getValue().toString());
                            answer8.setText(singleQuestion.child("ans8").getValue().toString());

                            Log.d("AppointmentAct","****user id matches: "+userid);
                        }

                    }

                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });






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
                linearLayout.setVisibility(View.GONE);
                rescheduleBtn.setVisibility(View.GONE);

                Log.d("AppointmentActions","*****inside rescheduleBtn appointmentDate"+appointmentDate.getText().toString());
                Log.d("AppointmentActions","*****inside rescheduleBtn appointmentTime"+appointmentTime.getText().toString());
                Log.d("AppointmentActions","*****inside rescheduleBtn appointmentId"+appointmentId.getText().toString());
                Log.d("AppointmentActions","*****inside rescheduleBtn appointmentStatus"+appointmentStatus.getText().toString());
                Log.d("AppointmentActions","*****inside rescheduleBtn userId"+userId.getText().toString());

//                Activity activity = (Activity)getApplicationContext();
                Intent intent = new Intent(getApplicationContext(), RescheduleAppointmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid",userId.getText().toString());
                bundle.putString("appointmentDate",appointmentDate.getText().toString());
                bundle.putString("appointmentTime",appointmentTime.getText().toString());
                bundle.putString("appointmentId",appointmentId.getText().toString());
                bundle.putString("appointmentStatus",appointmentStatus.getText().toString());

                intent.putExtras(bundle);
                startActivity(intent);


//                RescheduleAppointmentActivity fragObj = new RescheduleAppointmentActivity();
//                fragObj.setArguments(bundle);
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.activity_appointment_actions, new RescheduleAppointmentActivity()).commit();

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
