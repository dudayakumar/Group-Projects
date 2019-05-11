package com.example.bloodbank3.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.activities.DashboardActivity;
import com.example.bloodbank3.activities.LoginActivity;
import com.example.bloodbank3.models.AppointmentData;
import com.example.bloodbank3.models.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/***
 Class Name: BookApppointmentFragment
 Class Description: Fragment class to enable user to book appointments
 Created by: Shivangee Kulkarni
 ***/

public class BookApppointmentFragment extends Fragment {

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

    RadioButton radioButton1,radioButton2,radioButton3,radioButton4,radioButton5,radioButton6,radioButton7,radioButton8,radioButton9,radioButton10;
    String uid,uemail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_book_appointment, container, false);

        mAuth = FirebaseAuth.getInstance();
        fdb = FirebaseDatabase.getInstance();
        db_ref = fdb.getReference("appointments");
        db_ref2 = FirebaseDatabase.getInstance().getReference();

        userName = view.findViewById(R.id.userName);
        appointmentDate = view.findViewById(R.id.appointment_date);
        appointmentTime = view.findViewById(R.id.appointment_time);
        appointmentBookBtn = view.findViewById(R.id.schedule_appointment);
        radioGroup = view.findViewById(R.id.rGrp);
        appointmentTime.setEnabled(false);
        radioGroup.setVisibility(View.GONE);

        allAppointmentDataList = new ArrayList<>();

        radioButton1 = view.findViewById(R.id.rBtn1);
        radioButton2 = view.findViewById(R.id.rBtn2);
        radioButton3 = view.findViewById(R.id.rBtn3);
        radioButton4 = view.findViewById(R.id.rBtn4);
        radioButton5 = view.findViewById(R.id.rBtn5);
        radioButton6 = view.findViewById(R.id.rBtn6);
        radioButton7 = view.findViewById(R.id.rBtn7);
        radioButton8 = view.findViewById(R.id.rBtn8);
        radioButton9 = view.findViewById(R.id.rBtn9);
        radioButton10 = view.findViewById(R.id.rBtn10);


        Query allUsers = db_ref2.child("users");
        allUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = null;
                if(dataSnapshot.exists()) {
                    for (DataSnapshot singleUser : dataSnapshot.getChildren()) {
                        UserData userData = singleUser.getValue(UserData.class);
                        Log.d("BookAppointment: ", "*****singleUser.child(Name).getValue: "+singleUser.child("Name").getValue());
                        if(mAuth.getCurrentUser().getEmail().equals(userData.getEmail())){
                            userName.setText(userData.getName());
                        }
                    }
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

                datePicker = new DatePickerDialog(view.getContext(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        //Validating to disallow user from selecting past dates
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String date = sdf.format(cal.getTime());

                        String datePicked = day + "/" + (month + 1) + "/" + year;
                        Date date1 = null ,date2 = null;

                        try {
                            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                            date2 = new SimpleDateFormat("dd/MM/yyyy").parse(datePicked);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date2.after(date1)) {
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
                                            AppointmentData appointmentData = null;
                                            for (DataSnapshot singleAppt: dataSnapshot.getChildren()){
                                                appointmentData = singleAppt.getValue(AppointmentData.class);
                                                if(appointmentDate.getText().toString().equals(appointmentData.getDate())){
                                                    timeSlotList.add(appointmentData.getTime());
                                                }
                                                allAppointmentDataList.add(appointmentData);
                                            }
                                            allAppointmentDataList.size();
                                            Log.d("BookAppointment", "*****allAppointmentDataList.size(): "+allAppointmentDataList.size());
                                            Log.d("BookAppointment: ", "*****timeSlotList: "+timeSlotList);

                                            if(!appointmentData.getAppointmentStatus().equals("Rejected")) {
                                                enableTimeSlot(timeSlotList);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                        else{
                            Toast.makeText(view.getContext(), "Please pick a date in the future", Toast.LENGTH_LONG).show();
                        }

                    }
                },year,month,day);
                datePicker.show();
            }

        });

        //Fetching currently logged in user
        FirebaseUser cur_user = mAuth.getInstance().getCurrentUser();

        if(cur_user == null)
        {
            //Navigate to login page if no user is logged in
            startActivity(new Intent(view.getContext(), LoginActivity.class));
        } else {
            //Else get the user id and store it in uid
            uid = cur_user.getUid();
            uemail = cur_user.getEmail();
            Log.d("BookAppointment: ", "*****User email: "+uemail);
        }

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
                    AppointmentData appointmentData = new AppointmentData();

                    String key = "APPT"+String.valueOf(allAppointmentDataList.size()+1);
                    appointmentData.setUserId(uid);
                    appointmentData.setDate(ApptDate);
                    appointmentData.setTime(ApptTime);
                    appointmentData.setAppointmentStatus("Pending");
                    appointmentData.setAppointmentId(key);
                    appointmentData.setUserName(userName.getText().toString());

                    db_ref.child(key).setValue(appointmentData);

                    Toast.makeText(view.getContext(), "Appointment has been booked successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(view.getContext(), DashboardActivity.class));

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

        return view;
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
}
