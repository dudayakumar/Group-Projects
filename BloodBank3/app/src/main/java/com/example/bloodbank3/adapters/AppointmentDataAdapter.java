package com.example.bloodbank3.adapters;

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
import com.example.bloodbank3.interfaces.ClickListener;
import com.example.bloodbank3.models.AppointmentData;

import java.util.List;

public class AppointmentDataAdapter extends RecyclerView.Adapter<AppointmentDataAdapter.AppointmentHolder> {

    private List<AppointmentData> apptLists;
    private LinearLayout ll;
    private ClickListener clickListener = null;

    public class AppointmentHolder extends RecyclerView.ViewHolder {

    TextView apptDate, apptTime, userId, appointmentId, appointmentStatus;

        public AppointmentHolder(@NonNull final View itemView) {
            super(itemView);

            appointmentId = itemView.findViewById(R.id.appointmentid);
            userId = itemView.findViewById(R.id.userid);
            apptDate = itemView.findViewById(R.id.appointmentdate);
            apptTime = itemView.findViewById(R.id.appointmenttime);
            appointmentStatus = itemView.findViewById(R.id.appointmentstatus);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "clicked!", Toast.LENGTH_LONG).show();
//                    navigateToAppointmentAction();
//                }
//            });
        }

//        private void navigateToAppointmentAction(){
//            Intent i = new Intent(itemView.getContext(), AppointmentActionsActivity.class);
//            itemView.star
//        }

    }

    public void setClickListener(ClickListener clicklistener){
        this.clickListener = clicklistener;
    }

    public AppointmentDataAdapter(List<AppointmentData> apptLists) {
        this.apptLists = apptLists;
    }

    @Override
    public AppointmentHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.appointment_list_item, viewGroup, false);
        return new AppointmentHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentHolder appointmentHolder, int i) {

        AppointmentData appointmentData = apptLists.get(i);
        appointmentHolder.userId.setText("User Id:"+appointmentData.getUserId());
        appointmentHolder.apptDate.setText("Date: "+appointmentData.getDate());
        appointmentHolder.apptTime.setText("Time: "+appointmentData.getTime());
        appointmentHolder.appointmentStatus.setText("Status: "+appointmentData.getAppointmentStatus());

        Log.d("AppointmentDataAdapter","*****User Id = "+appointmentData.getUserId());
        Log.d("AppointmentDataAdapter","*****Date = "+appointmentData.getDate());
        Log.d("AppointmentDataAdapter","*****Time = "+appointmentData.getTime());
        Log.d("AppointmentDataAdapter","*****Appt Status = "+appointmentData.getAppointmentStatus());


    }

    @Override
    public int getItemCount() {
        return apptLists.size();
    }
}