package com.example.bloodbank3.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.adapters.AppointmentDataAdapter;
import com.example.bloodbank3.models.AppointmentData;
import com.example.bloodbank3.models.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView apptData;

    private DatabaseReference db_ref;
    private FirebaseAuth mAuth;
    private AppointmentDataAdapter appointmentDataAdapter;
    private List<AppointmentData> appointmentDataList;
    private ProgressDialog pd;
    private CardView card;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_dashboard, container, false);
        apptData = (RecyclerView) view.findViewById(R.id.recyclerView);
        card = view.findViewById(R.id.cardview);

        apptData.setLayoutManager(new LinearLayoutManager(getContext()));

        db_ref = FirebaseDatabase.getInstance().getReference();
        appointmentDataList = new ArrayList<>();

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle("Home");

        appointmentDataAdapter = new AppointmentDataAdapter(appointmentDataList);
        RecyclerView.LayoutManager apptLayout = new LinearLayoutManager(getContext());
        apptData.setLayoutManager(apptLayout);
        apptData.setItemAnimator(new DefaultItemAnimator());
        apptData.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        apptData.setAdapter(appointmentDataAdapter);

        Log.d("HomeFragment", "******Before set adapter ");
        AddAppointments();

        return view;
    }

    private void AddAppointments(){
        Query allAppts = db_ref.child("appointments");
        pd.show();

        allAppts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    for (DataSnapshot singleAppt: dataSnapshot.getChildren()){
                        AppointmentData appointmentData = singleAppt.getValue(AppointmentData.class);

                        //Filtering appointments display for logged in user and showing all appts for admin
                        if(appointmentData.getUserId().equals(mAuth.getCurrentUser().getUid()) || mAuth.getCurrentUser().getEmail().equalsIgnoreCase("admin@gmail.com")) {
                            appointmentDataList.add(appointmentData);
                            appointmentDataAdapter.notifyDataSetChanged();
                        }
                    }
                    pd.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Database is empty now!", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("HomeFragment", "Error: "+databaseError.getMessage());
            }
        });
    }
}