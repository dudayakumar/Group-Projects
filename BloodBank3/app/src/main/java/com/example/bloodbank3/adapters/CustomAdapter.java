package com.example.bloodbank3.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bloodbank3.R;
import com.example.bloodbank3.models.AppointmentData;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    ArrayList<AppointmentData> appointmentData;

    public CustomAdapter(Context c , ArrayList<AppointmentData> a) {
        context = c;
        appointmentData = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
                //new MyViewHolder(LayoutInflater.from(context).inflate(R.layout., viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.apptDate.setText(appointmentData.get(i).getDate());
        myViewHolder.apptTime.setText(appointmentData.get(i).getTime());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView apptDate, apptTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            apptDate = itemView.findViewById(R.id.appointment_date);
            apptTime = itemView.findViewById(R.id.appointment_time);
        }
    }
}
