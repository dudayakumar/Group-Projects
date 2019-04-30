package com.example.bloodbank3.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bloodbank3.R;
import com.example.bloodbank3.models.AppointmentData;

import java.util.List;

public class AppointmentDataAdapter extends RecyclerView.Adapter<AppointmentDataAdapter.AppointmentHolder> {

    private List<AppointmentData> apptLists;

    public class AppointmentHolder extends RecyclerView.ViewHolder{

    TextView apptDate, apptTime;

        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);

            apptDate = itemView.findViewById(R.id.appointmentdate);
            apptTime = itemView.findViewById(R.id.appointmenttime);
        }
    }

    public AppointmentDataAdapter(List<AppointmentData> apptLists) {
        this.apptLists = apptLists;
    }

//    public AppointmentDataAdapter(Context context, List<AppointmentData> apptLists) {
//        this.context = context;
//        this.apptLists = apptLists;
//    }

    @Override
    public AppointmentHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.appointment_list_item, viewGroup, false);
        return new AppointmentHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentHolder appointmentHolder, int i) {

        AppointmentData appointmentData = apptLists.get(i);
        appointmentHolder.apptDate.setText("Date: "+appointmentData.getDate());
        appointmentHolder.apptTime.setText("Time: "+appointmentData.getTime());
        Log.d("AppointmentDataAdapter","*****Date = "+appointmentData.getDate());
        Log.d("AppointmentDataAdapter","*****Time = "+appointmentData.getTime());
    }

    @Override
    public int getItemCount() {
        return apptLists.size();
    }
}


//public class AppointmentDataAdapter extends RecyclerView.Adapter<AppointmentDataAdapter.MyViewHolder>{
//
//    List<AppointmentData> appointmentDataList;
//    Context context;
//
//    public AppointmentDataAdapter(List<AppointmentData> appointmentDataList, Context context) {
//        this.appointmentDataList = appointmentDataList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(context).inflate(R.layout.appointment_list_item,viewGroup,false);
//        MyViewHolder myViewHolder = new MyViewHolder(view);
//        return myViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
//            AppointmentData appointmentList = appointmentDataList.get(i);
//            myViewHolder.apptdate.setText(appointmentList.getDate());
//            myViewHolder.appttime.setText(appointmentList.getTime());
//    }
//
//    @Override
//    public int getItemCount() {
//        int arr = 0;
//        try{
//            if(appointmentDataList.size() == 0 )
//                arr=0;
//            else
//                arr=appointmentDataList.size();
//        }
//        catch (Exception e){
//                Log.d("Exception","Exception"+e);
//        }
//        return arr;
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder{
//
//        TextView apptdate,appttime;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            apptdate = itemView.findViewById(R.id.appointment_date);
//            appttime = itemView.findViewById(R.id.appointment_time);
//        }
//    }
//}