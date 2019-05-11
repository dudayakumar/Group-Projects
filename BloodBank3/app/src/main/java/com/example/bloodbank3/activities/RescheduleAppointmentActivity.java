package com.example.bloodbank3.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.models.AppointmentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RescheduleAppointmentActivity extends AppCompatActivity {

    private View view;
    private TextView userName;
    private EditText appointmentDate;
    private EditText appointmentTime;
    private DatePickerDialog datePicker;
    private Button appointmentBookBtn;
    private RadioGroup radioGroup;

    private DatabaseReference db_ref;
    private DatabaseReference db_ref2;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;

    private List<String> timeSlotList;
    private List<AppointmentData> allAppointmentDataList;

    String date,time,username,status,apptid;

    RadioButton radioButton1,radioButton2,radioButton3,radioButton4,radioButton5,radioButton6,radioButton7,radioButton8,radioButton9,radioButton10;
    String uid,uemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Reschedule Appointment");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Bundle databundle = getIntent().getExtras();

        if(databundle!=null){
            username = databundle.getString("userid");
            date = databundle.getString("appointmentDate");
            time = databundle.getString("appointmentTime");
            apptid = databundle.getString("appointmentId");
            status = databundle.getString("appointmentStatus");
        }

        mAuth = FirebaseAuth.getInstance();
        fdb = FirebaseDatabase.getInstance();
        db_ref = fdb.getReference("appointments");
        db_ref2 = FirebaseDatabase.getInstance().getReference();

        userName = findViewById(R.id.userName);
        appointmentDate = findViewById(R.id.appointment_date);
        appointmentTime = findViewById(R.id.appointment_time);
        appointmentBookBtn = findViewById(R.id.schedule_appointment);
        appointmentBookBtn.setText("Reschedule Appointment");
        radioGroup = findViewById(R.id.rGrp);
        appointmentTime.setEnabled(false);

        radioButton1 = findViewById(R.id.rBtn1);
        radioButton2 = findViewById(R.id.rBtn2);
        radioButton3 = findViewById(R.id.rBtn3);
        radioButton4 = findViewById(R.id.rBtn4);
        radioButton5 = findViewById(R.id.rBtn5);
        radioButton6 = findViewById(R.id.rBtn6);
        radioButton7 = findViewById(R.id.rBtn7);
        radioButton8 = findViewById(R.id.rBtn8);
        radioButton9 = findViewById(R.id.rBtn9);
        radioButton10 = findViewById(R.id.rBtn10);

        allAppointmentDataList = new ArrayList<>();

        appointmentDate.setText(date);
        appointmentTime.setText(time);
        userName.setText(username);

        timeSlotList = new ArrayList<>();
        Query allAppts = db_ref2.child("appointments");
        allAppts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot singleAppt: dataSnapshot.getChildren()){
                        AppointmentData appointmentData = singleAppt.getValue(AppointmentData.class);
                        if(appointmentDate.getText().toString().equals(appointmentData.getDate())){
                            timeSlotList.add(appointmentData.getTime());
                        }
                        allAppointmentDataList.add(appointmentData);
                    }
                    allAppointmentDataList.size();
                    Log.d("BookAppointment", "*****allAppointmentDataList.size(): "+allAppointmentDataList.size());
                    Log.d("BookAppointment: ", "*****timeSlotList: "+timeSlotList);
                    enableTimeSlot(timeSlotList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Logic to show date picker dialogue
        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(RescheduleAppointmentActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        appointmentDate.setText(day + "/" + (month + 1) + "/" + year);

                        //Logic to render time slots according to availability
                        if(!appointmentDate.getText().toString().isEmpty()){
                            radioGroup.setVisibility(View.VISIBLE);

                            timeSlotList = new ArrayList<>();
                            radioGroup.clearCheck();
                            for(int i = 0 ; i<radioGroup.getChildCount(); i++){
                                radioGroup.getChildAt(i).setEnabled(true);
                            }

                            Query allAppts = db_ref2.child("appointments");
                            allAppts.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for (DataSnapshot singleAppt: dataSnapshot.getChildren()){
                                            AppointmentData appointmentData = singleAppt.getValue(AppointmentData.class);
                                            if(appointmentDate.getText().toString().equals(appointmentData.getDate())){
                                                timeSlotList.add(appointmentData.getTime());
                                            }
                                            allAppointmentDataList.add(appointmentData);
                                        }
                                        allAppointmentDataList.size();
                                        Log.d("BookAppointment", "*****allAppointmentDataList.size(): "+allAppointmentDataList.size());
                                        Log.d("BookAppointment: ", "*****timeSlotList: "+timeSlotList);
                                        enableTimeSlot(timeSlotList);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                },year,month,day);
                datePicker.show();
            }
        });

        //Logic to store appointment details in database
        appointmentBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Storing user's input for date and time into variables
                final String ApptDate = appointmentDate.getText().toString();
                final String ApptTime = appointmentTime.getText().toString();

                //Validation on user's input for date
                if (appointmentDate.getText().length() == 0){
                    Toast.makeText(view.getContext(), "Please pick a date", Toast.LENGTH_LONG).show();
                }
                //Validation on user's input for time
                else if (appointmentTime.getText().length() == 0){
                    Toast.makeText(view.getContext(), "Please pick a time", Toast.LENGTH_LONG).show();
                }
                //Store the appointment date and time in Firebase under appointments node
                else {
                        String key = apptid;

                        db_ref.child(apptid).child("date").setValue(ApptDate);
                        db_ref.child(apptid).child("time").setValue(ApptTime);

                        Toast.makeText(RescheduleAppointmentActivity.this, "Appointment has been rescheduled!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RescheduleAppointmentActivity.this, DashboardActivity.class));
                }
            }
        });

        //Logic to set time edit texxt with value of the radio button
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rBtn1:
                        appointmentTime.setText(radioButton1.getText());
                        break;
                    case R.id.rBtn2:
                        appointmentTime.setText(radioButton2.getText());
                        break;
                    case R.id.rBtn3:
                        appointmentTime.setText(radioButton3.getText());
                        break;
                    case R.id.rBtn4:
                        appointmentTime.setText(radioButton4.getText());
                        break;
                    case R.id.rBtn5:
                        appointmentTime.setText(radioButton5.getText());
                        break;
                    case R.id.rBtn6:
                        appointmentTime.setText(radioButton6.getText());
                        break;
                    case R.id.rBtn7:
                        appointmentTime.setText(radioButton7.getText());
                        break;
                    case R.id.rBtn8:
                        appointmentTime.setText(radioButton8.getText());
                        break;
                    case R.id.rBtn9:
                        appointmentTime.setText(radioButton9.getText());
                        break;
                    case R.id.rBtn10:
                        appointmentTime.setText(radioButton10.getText());
                        break;
                }
            }
        });
    }

    //Logic to enable/disable timeslots based on availability
    private void enableTimeSlot(List<String> timeSlotList) {

        for (String timeSlot: timeSlotList){
            if(timeSlot.equals(radioButton1.getText().toString())){
                radioButton1.setEnabled(false);
            }
            if(timeSlot.equals(radioButton2.getText().toString())){
                radioButton2.setEnabled(false);
            }
            if(timeSlot.equals(radioButton3.getText().toString())){
                radioButton3.setEnabled(false);
            }
            if(timeSlot.equals(radioButton4.getText().toString())){
                radioButton4.setEnabled(false);
            }
            if(timeSlot.equals(radioButton5.getText().toString())){
                radioButton5.setEnabled(false);
            }
            if(timeSlot.equals(radioButton6.getText().toString())){
                radioButton6.setEnabled(false);
            }
            if(timeSlot.equals(radioButton7.getText().toString())){
                radioButton7.setEnabled(false);
            }
            if(timeSlot.equals(radioButton8.getText().toString())){
                radioButton8.setEnabled(false);
            }
            if(timeSlot.equals(radioButton9.getText().toString())){
                radioButton9.setEnabled(false);
            }
            if(timeSlot.equals(radioButton10.getText().toString())){
                radioButton10.setEnabled(false);
            }
        }
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
