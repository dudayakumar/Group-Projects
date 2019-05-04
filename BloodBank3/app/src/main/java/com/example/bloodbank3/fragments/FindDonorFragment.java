package com.example.bloodbank3.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank3.R;
import com.example.bloodbank3.models.UserData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDonorFragment extends Fragment {

    private DatabaseReference db_ref;
    private FirebaseAuth mAuth;

    private EditText zipCode;
    private Button findDonorBtn;

    private GoogleMap mMap = null;

    LatLng latLng = null;
    String userId = null;
    String userName = null;
    String userBloodGrp = null;
    int userBG = 0;

    Map<String, List<String>> userDisplayMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.finddonor, container, false);
        getActivity().setTitle("Find Donors");

        zipCode = view.findViewById(R.id.zip_code);
        findDonorBtn = view.findViewById(R.id.find_donorBtn);

        mAuth = FirebaseAuth.getInstance();
        db_ref = FirebaseDatabase.getInstance().getReference();
        Log.d("FindDonorFragment", "*****db_ref: "+db_ref);

        Query allUsers = db_ref.child("users");
        Log.d("FindDonorFragment", "*****allUsers: "+allUsers);

        //Storing user details in a hashmap
        allUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FindDonorFragment", "*****dataSnapshot.getChildren(): "+dataSnapshot.getChildren());
                Log.d("FindDonorFragment", "*****dataSnapshot.getValue(): "+dataSnapshot.getValue());

                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    UserData userData = snap.getValue(UserData.class);
                    Log.d("FindDonorFragment", "*****userData snapshot: "+userData);

                    if(userData != null){
                        String userAddress = userData.getAddress();
                        userId = snap.getKey();
                        Log.d("FindDonorFragment", "*****userId = "+userId);
                        userBG = userData.getBloodGroup();
                        userName = userData.getName();
                        userBloodGrp = fetchBloodGroup(userBG);
                        latLng = getLocationFromAddress(getContext(), userAddress);
                        Log.d("FindDonorFragment", "*****"+userAddress+": LatLng = "+latLng);

                        List<String> userDetailsList = new ArrayList<>();
                        userDetailsList.add(userName);
                        userDetailsList.add(userBloodGrp);
                        userDetailsList.add(Double.toString(latLng.latitude));
                        userDetailsList.add(Double.toString(latLng.longitude));
                        userDetailsList.add(userAddress);

                        Log.d("FindDonorFragment", "*****userDetailsList = "+userDetailsList);

                        userDisplayMap.put(userId,userDetailsList);
                        Log.d("FindDonorFragment", "*****userDisplayMap = "+userDisplayMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "User database is empty!", Toast.LENGTH_LONG).show();
            }

        });

        //Rendering location of users on the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.donor_location);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                final GoogleMap map = mMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {

//                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                        Log.d("FindDonorFragment", "*****inside setOnMapLoadedCallback userDisplayMap = "+userDisplayMap);
//
//                        //Iterating through the userDetail HashMap to fetch latitude, longitude, user's name and blood group to display on the map
//                        for(List<String> userDetail: userDisplayMap.values()){
//                            map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(userDetail.get(2)), Double.parseDouble(userDetail.get(3)))).title("Name: "+userDetail.get(0)).snippet("Blood Group: "+userDetail.get(1)));
//                        }
//                        //Setting map center
//                        CameraPosition cPos = CameraPosition.builder().target(new LatLng(41.836038, -87.626544)).zoom(13).bearing(0).tilt(0).build();
//                        map.moveCamera(CameraUpdateFactory.newCameraPosition(cPos));

                        findDonorBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                Log.d("FindDonorFragment", "*****inside setOnMapLoadedCallback userDisplayMap = "+userDisplayMap);

                                //Iterating through the userDetail HashMap to fetch latitude, longitude, user's name and blood group to display on the map
                                Log.d("FindDonorFragment", "*****zipCode.getText() = "+zipCode.getText());
                                for(List<String> userDetail: userDisplayMap.values()){
                                    Log.d("FindDonorFragment", "*****userDetail.get(4) = "+userDetail.get(3));
                                    if(userDetail.get(4).contains(zipCode.getText()))
                                        map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(userDetail.get(2)), Double.parseDouble(userDetail.get(3)))).title("Name: "+userDetail.get(0)).snippet("Blood Group: "+userDetail.get(1)));
                                }
                                //Setting map center
                                CameraPosition cPos = CameraPosition.builder().target(new LatLng(41.836038, -87.626544)).zoom(13).bearing(0).tilt(0).build();
                                map.moveCamera(CameraUpdateFactory.newCameraPosition(cPos));
                            }
                        });

                    }
                });

                Log.d("FindDonorFragment", "*****inside onMapReady userDisplayMap = "+userDisplayMap);
                //mMap.addMarker(new MarkerOptions().position(new LatLng(51.8403, -87.6137)).title(userName).snippet(userBloodGrp));
//                mMap.addMarker(new MarkerOptions().position(new LatLng(41.836038, -87.626544)).title("abc").snippet("B+"));
//                mMap.addMarker(new MarkerOptions().position(new LatLng(41.883230, -87.632400)).title("def").snippet("A+"));
//
//                CameraPosition cPos = CameraPosition.builder().target(new LatLng(41.836038, -87.626544)).zoom(13).bearing(0).tilt(0).build();
//                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cPos));

            }
        });

        return view;
    }

    //Method to obtain Latitude and Longitude for a given address
    public LatLng getLocationFromAddress(Context context, String strAddress){
        Geocoder geocoder = new Geocoder(context);
        LatLng p1 = null;
        List<Address> address;

        try{
            address = geocoder.getFromLocationName(strAddress, 5);
            if(address == null){
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }catch (IOException ex){
            ex.printStackTrace();
        }

        return p1;
    }

    //Method to retrive bllod group of user based on picklist position
    public String fetchBloodGroup(int userBGpos){
        String userBloodGroup = null;

        switch (userBGpos)
        {
            case 0:
                userBloodGroup = "A+";
                break;
            case 1:
                userBloodGroup = "A-";
                break;
            case 2:
                userBloodGroup = "B+";
                break;
            case 3:
                userBloodGroup = "B-";
                break;
            case 4:
                userBloodGroup = "AB+";
                break;
            case 5:
                userBloodGroup = "AB-";
                break;
            case 6:
                userBloodGroup = "O+";
                break;
            case 7:
                userBloodGroup = "O-";
                break;
            default:
                userBloodGroup = "A+";
                break;
        }
        return userBloodGroup;
    }

//    public void getCurrentLocation(){
//        Log.d("FindDonorFragment", "*****inside getCurrentLocation");
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.addMarker(new MarkerOptions().position(new LatLng(41.836038, -87.626544)).title("abc").snippet("B+"));
//        CameraPosition cPos = CameraPosition.builder().target(new LatLng(41.836038, -87.626544)).zoom(13).bearing(0).tilt(0).build();
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cPos));
//    }

}
