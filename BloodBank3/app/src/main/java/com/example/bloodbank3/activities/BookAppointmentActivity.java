package com.example.bloodbank3.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.models.AppointmentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookAppointmentActivity extends AppCompatActivity {

    private EditText appointmentDate;
    private EditText appointmentTime;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private Button appointmentBookBtn;

    private DatabaseReference db_ref;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;

    String uid,uemail,aid;
    String appttime, apptdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        mAuth = FirebaseAuth.getInstance();
        fdb = FirebaseDatabase.getInstance();
        db_ref = fdb.getReference("appointments");

        appointmentDate = findViewById(R.id.appointment_date);
        appointmentTime = findViewById(R.id.appointment_time);
        appointmentBookBtn = findViewById(R.id.schedule_appointment);

        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(BookAppointmentActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        appointmentDate.setText(day + "/" + (month + 1) + "/" + year);
                    }
                },year,month,day);
                datePicker.show();
            }
        });

        appointmentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                timePicker = new TimePickerDialog(BookAppointmentActivity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        appointmentTime.setText(hourOfDay+":"+minute);
                    }
                },hour,minute,true);
                timePicker.show();
            }
        });

        //Fetching currently logged in user
        FirebaseUser cur_user = mAuth.getInstance().getCurrentUser();

        if(cur_user == null)
        {
            //Navigate to login page if no user is logged in
            startActivity(new Intent(BookAppointmentActivity.this, LoginActivity.class));
        } else {
            //Else get the user id and store it in uid
            uid = cur_user.getUid();
            uemail = cur_user.getEmail();
            Log.d("BookAppointment: ", "*****User email: "+uemail);
        }

        appointmentBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Storing user's input for date and time into variables
                final String ApptDate = appointmentDate.getText().toString();
                final String ApptTime = appointmentTime.getText().toString();

                //Validation on user's input for date
                if (appointmentDate.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Please pick a date", Toast.LENGTH_LONG).show();
                }
                //Validation on user's input for time
                else if (appointmentTime.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Please pick a time", Toast.LENGTH_LONG).show();
                }
                //Store the appointment date and time in firebase under appointments node
                else {
                    AppointmentData appointmentData = new AppointmentData();
                    appointmentData.setUserId(uid);
                    appointmentData.setDate(ApptDate);
                    appointmentData.setTime(ApptTime);
                    DatabaseReference newRef = db_ref.push();

                    newRef.setValue(appointmentData);

//                    userQuery.addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                            String myParentNode = dataSnapshot.getKey();
//                            for (DataSnapshot child: dataSnapshot.getChildren()){
//                                String key = child.getKey().toString();
//                                String value = child.getValue().toString();
//                                Log.d("BookAppointmentActivity","*****key = "+key);
//                                Log.d("BookAppointmentActivity","*****value = "+value);
//                                db_ref.child(uid).child(key).child("Time").setValue(ApptTime);
//                            }
//                        }
//
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
                }
            }
        });
    }
}
