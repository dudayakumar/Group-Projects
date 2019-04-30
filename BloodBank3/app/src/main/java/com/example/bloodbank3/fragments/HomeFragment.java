package com.example.bloodbank3.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{

    private View view;
    private RecyclerView apptData;

    private DatabaseReference db_ref;
    private FirebaseAuth mAuth;
//    private RecyclerView.Adapter appointmentDataAdapter;
    private AppointmentDataAdapter appointmentDataAdapter;
    private List<AppointmentData> appointmentDataList;
    private ProgressDialog pd;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_dashboard, container, false);
        apptData = (RecyclerView) view.findViewById(R.id.recyclerView);

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
        //appointmentDataAdapter = new AppointmentDataAdapter(HomeFragment.this,appointmentDataList);
        RecyclerView.LayoutManager apptLayout = new LinearLayoutManager(getContext());
        apptLayout = new LinearLayoutManager(getActivity());
        apptData.setLayoutManager(apptLayout);
        apptData.setItemAnimator(new DefaultItemAnimator());
        apptData.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        apptData.setAdapter(appointmentDataAdapter);

        AddAppointments();
        return view;
    }

    private void AddAppointments(){

        final String uid = mAuth.getCurrentUser().getUid();

        Query allAppts = db_ref.child("appointments").child(uid);
        Log.d("Home Fragment", "*****allAppts.getkey(): "+((DatabaseReference) allAppts).getKey());

        pd.show();

        allAppts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot singleAppt : dataSnapshot.getChildren()) {
                        AppointmentData appointmentData = singleAppt.getValue(AppointmentData.class);
                        Log.d("BookAppointmentActivity","*****appointmentData = "+appointmentData);
                        Log.d("BookAppointmentActivity","*****appointmentData.getDate() = "+appointmentData.getDate());
                        Log.d("BookAppointmentActivity","*****appointmentData.getTime() = "+appointmentData.getTime());
                        appointmentDataList.add(appointmentData);
                        appointmentDataAdapter.notifyDataSetChanged();
                    }
                    pd.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Database is empty now!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("HomeFragment", "databaseError: " + databaseError.getMessage());
            }
        });
    }
}

//    private void AddAppointments(){
//
//        final String uid = mAuth.getCurrentUser().getUid();
//
//        Query allAppts = db_ref.child("appointments").child(uid);
//        Log.d("Home Fragment", "*****allAppts.getkey(): "+((DatabaseReference) allAppts).getKey());
//
//        pd.show();
//
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot singleAppt : dataSnapshot.getChildren()) {
//                        AppointmentData appointmentData = singleAppt.getValue(AppointmentData.class);
//                        appointmentDataList.add(appointmentData);
//
////                        apptData = view.findViewById(R.id.recyclerView);
//
////                        appointmentDataAdapter = new AppointmentDataAdapter(appointmentDataList);
////                        apptData.setAdapter(appointmentDataAdapter);
//
//                        appointmentDataAdapter.notifyDataSetChanged();
//                    }
//                    pd.dismiss();
//                } else {
//                    Toast.makeText(getActivity(), "Database is empty now!", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("HomeFragment", "databaseError: " + databaseError.getMessage());
//            }
//        };
//    }
//}





//    private void AddAppointments(){
//        final String uid = mAuth.getCurrentUser().getUid();
//
//        final Query userQuery = db_ref.orderByChild("Date");
//
//        Query allAppts = db_ref.child("appointments").child(uid);//.child("LdfQlRIxZiCuSh4-uRQ");
//        Log.d("Home Fragment", "*****allAppts: "+allAppts);
//        Log.d("Home Fragment", "*****allAppts.toString(): "+allAppts.toString());
//        Log.d("Home Fragment", "*****allAppts.getkey(): "+((DatabaseReference) allAppts).getKey());
//        pd.show();
//        allAppts.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    for(DataSnapshot singleAppt : dataSnapshot.getChildren()) {
//                        String key = singleAppt.getKey().toString();
//                        Log.d("BookAppointmentActivity","*****key = "+key);
//                        String value = singleAppt.getValue().toString();
//                        Log.d("BookAppointmentActivity","*****value = "+value);
//
//                        AppointmentData appointmentData = singleAppt.getValue(AppointmentData.class);
//                        Log.d("BookAppointmentActivity","*****appointmentData = "+appointmentData);
//                        Log.d("BookAppointmentActivity","*****appointmentData.getDate() = "+appointmentData.getDate());
//                        Log.d("BookAppointmentActivity","*****appointmentData.getTime() = "+appointmentData.getTime());
//                        appointmentDataList.add(appointmentData);
//                        appointmentDataAdapter.notifyDataSetChanged();
//                    }
//
//                    pd.dismiss();
//                }else {
//                    Toast.makeText(getActivity(), "Database is empty now!", Toast.LENGTH_LONG).show();
//                    pd.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("HomeFragment", "*****Error: "+databaseError.getMessage());
//            }
//        });
//    }


//public class HomeFragment extends FragmentActivity {
//
//    private RecyclerView apptData;
//    private DatabaseReference db_ref;
//    private FirebaseAuth mAuth;
//    private AppointmentDataAdapter appointmentDataAdapter;
//    private List<AppointmentData> appointmentDataList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.content_dashboard);
//        apptData = findViewById(R.id.recyclerView);
//        db_ref = FirebaseDatabase.getInstance().getReference("appointments");
//        db_ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                appointmentDataList = new ArrayList<AppointmentData>();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    AppointmentData value = snapshot.getValue(AppointmentData.class);
//                    AppointmentData appointmentdata = new AppointmentData();
//                    String apptDate = value.getDate();
//                    String apptTime = value.getTime();
//                    appointmentdata.setDate(apptDate);
//                    appointmentdata.setTime(apptTime);
//                    appointmentDataList.add(appointmentdata);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("HomeFragment", "*****Error: "+databaseError.getMessage());
//            }
//        });
//
//
//        AppointmentDataAdapter apptAdapter = new AppointmentDataAdapter(appointmentDataList,HomeFragment.this);
//        RecyclerView.LayoutManager recycle = new LinearLayoutManager(HomeFragment.this);
//        apptData.setLayoutManager(recycle);
//        apptData.setAdapter(apptAdapter);
//    }
//}
