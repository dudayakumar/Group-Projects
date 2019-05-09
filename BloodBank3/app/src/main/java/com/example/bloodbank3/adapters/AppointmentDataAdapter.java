package com.example.bloodbank3.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.activities.AppointmentActionsActivity;
import com.example.bloodbank3.fragments.HomeFragment;
import com.example.bloodbank3.models.AppointmentData;
import com.example.bloodbank3.models.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

/***
 Class Name: AppointmentDataAdapter
 Class Description: Adapter class to set Appointment details data to render in recycler view
 Created by: Dhivya Udaya Kumar
 ***/

public class AppointmentDataAdapter extends RecyclerView.Adapter<AppointmentDataAdapter.AppointmentHolder> implements View.OnClickListener{

    Context context;
    private DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference();


    @Override
    public void onClick(View v) {
     //   Log.d("AppointmentDataAdapter","*****Inside onClick");
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, View view);
    }
    private OnItemClickListener listener;

    private List<AppointmentData> apptLists;

    public AppointmentDataAdapter(List<AppointmentData> apptLists) {
        this.apptLists = apptLists;
    }

    public class AppointmentHolder extends RecyclerView.ViewHolder {

    private TextView apptDate, apptTime, userId, appointmentId, appointmentStatus;

        public AppointmentHolder(@NonNull  View itemView) {
            super(itemView);
            Log.d("AppointmentDataAdapter","*****Inside AppointmentHolder");
            appointmentId = itemView.findViewById(R.id.appointmentid);
            userId = itemView.findViewById(R.id.userid);
            apptDate = itemView.findViewById(R.id.appointmentdate);
            apptTime = itemView.findViewById(R.id.appointmenttime);
            appointmentStatus = itemView.findViewById(R.id.appointmentstatus);
        }
    }

    @Override
    public AppointmentHolder onCreateViewHolder(final ViewGroup viewGroup, int viewtype) {

        Log.d("AppointmentDataAdapter","*****Inside onCreateViewHolder");
        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.appointment_list_item, viewGroup, false);

        listItem.setOnClickListener(this);
        RecyclerView.ViewHolder holder = new AppointmentHolder(listItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("AppointmentDataAdapter","***** onClick");

                //Calling method in home fragment class for navigation
                TextView userid = v.findViewById(R.id.userid);
                TextView apptDate = v.findViewById(R.id.appointmentdate);
                TextView apptTime = v.findViewById(R.id.appointmenttime);
                TextView apptId = v.findViewById(R.id.appointmentid);
                TextView status = v.findViewById(R.id.appointmentstatus);

                HomeFragment homeFragment = new HomeFragment();
                homeFragment.navigateToApptFragment(viewGroup.getContext(),userid.getText()+"",apptDate.getText()+"",apptTime.getText()+"",apptId.getText()+"",status.getText()+"");

            }


        });

        AppointmentHolder newobj = new AppointmentHolder(listItem);
        return newobj;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentHolder appointmentHolder, int i) {

        Log.d("AppointmentDataAdapter","*****Inside onBindViewHolder "+i);
        final AppointmentData appointmentData = apptLists.get(i);

        appointmentHolder.appointmentId.setText(appointmentData.getAppointmentId());
        appointmentHolder.userId.setText(appointmentData.getUserName());
        appointmentHolder.apptDate.setText(appointmentData.getDate());
        appointmentHolder.apptTime.setText(appointmentData.getTime());
        appointmentHolder.appointmentStatus.setText(appointmentData.getAppointmentStatus());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
        Log.d("AppointmentDataAdapter","setOnItemClickListener");
    }

    @Override
    public int getItemCount() {
        return apptLists.size();
    }
}