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
import com.example.bloodbank3.interfaces.ClickListener;
import com.example.bloodbank3.models.AppointmentData;

import java.util.List;

public class AppointmentDataAdapter extends RecyclerView.Adapter<AppointmentDataAdapter.AppointmentHolder> {

    Context context;
    public interface OnItemClickListener {
        void onItemClick(AppointmentData item);
    }
    private OnItemClickListener listener;

    private List<AppointmentData> apptLists;
    private LinearLayout ll;

    public AppointmentDataAdapter(List<AppointmentData> apptLists) {
        this.apptLists = apptLists;
    }

    public class AppointmentHolder extends RecyclerView.ViewHolder {

    TextView apptDate, apptTime, userId, appointmentId, appointmentStatus;

        public AppointmentHolder(@NonNull final View itemView) {
            super(itemView);
            Log.d("AppointmentDataAdapter","*****Inside AppointmentHolder");
            appointmentId = itemView.findViewById(R.id.appointmentid);
            userId = itemView.findViewById(R.id.userid);
            apptDate = itemView.findViewById(R.id.appointmentdate);
            apptTime = itemView.findViewById(R.id.appointmenttime);
            appointmentStatus = itemView.findViewById(R.id.appointmentstatus);
        }

        public void bind(final AppointmentData item, final OnItemClickListener listener){
            apptDate.setText(item.getDate());

            Log.d("AppointmentDataAdapter","*****Inside bind");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public AppointmentHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Log.d("AppointmentDataAdapter","*****Inside onCreateViewHolder");
        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.appointment_list_item, viewGroup, false);
        return new AppointmentHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentHolder appointmentHolder, int i) {

        Log.d("AppointmentDataAdapter","*****Inside onBindViewHolder");
        AppointmentData appointmentData = apptLists.get(i);
        appointmentHolder.userId.setText("User Id:"+appointmentData.getUserId());
        appointmentHolder.apptDate.setText("Date: "+appointmentData.getDate());
        appointmentHolder.apptTime.setText("Time: "+appointmentData.getTime());
        appointmentHolder.appointmentStatus.setText("Status: "+appointmentData.getAppointmentStatus());

        Log.d("AppointmentDataAdapter","*****User Id = "+appointmentData.getUserId());
        Log.d("AppointmentDataAdapter","*****Date = "+appointmentData.getDate());
        Log.d("AppointmentDataAdapter","*****Time = "+appointmentData.getTime());
        Log.d("AppointmentDataAdapter","*****Appt Status = "+appointmentData.getAppointmentStatus());

        appointmentHolder.bind(apptLists.get(i), listener);
    }

    @Override
    public int getItemCount() {
        Log.d("AppointmentDataAdapter","*****apptLists.size() = "+apptLists.size());
        return apptLists.size();
    }
}