package com.example.bloodbank3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.bloodbank3.R;
import com.example.bloodbank3.fragments.AboutUsFragment;
import com.example.bloodbank3.fragments.BookApppointmentFragment;
import com.example.bloodbank3.fragments.ContactUsFragment;
import com.example.bloodbank3.fragments.FindDonorFragment;
import com.example.bloodbank3.fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/***
 Class Name: DashboardActivity
 Class Description: Class to set and render navigation menu items on the dashboard
 Created by: Dhivya Udaya Kumar
 ***/

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase user_db;
    private FirebaseUser cur_user;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        user_db = FirebaseDatabase.getInstance();
        cur_user = mAuth.getCurrentUser();

        dl = (DrawerLayout)findViewById(R.id.activity_dashboard);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nav_view);

        //Conditionally rendering navigation menu items
        //If logged-in user is an admin
        if(cur_user.getEmail().equalsIgnoreCase("admin@gmail.com")) {
            nv.getMenu().findItem(R.id.userprofile).setVisible(false);
            nv.getMenu().findItem(R.id.bookAppt).setVisible(false);
            nv.getMenu().findItem(R.id.aboutUs).setVisible(false);
            nv.getMenu().findItem(R.id.contactUs).setVisible(false);
        }

        //If logged-in user is a donor
        if(!cur_user.getEmail().equalsIgnoreCase("admin@gmail.com")) {
            nv.getMenu().findItem(R.id.find_donor).setVisible(false);
        }

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Uncheck previously selected menu item
                for(int i = 0; i < nv.getMenu().size(); i++){
                    nv.getMenu().getItem(i).setChecked(false);
                }

                //Check selected menu item
                menuItem.setChecked(true);

                int id = menuItem.getItemId();
                switch (id)
                {
                    case R.id.home:
                        Log.d("DashBoardActivity","*****Navigating to home page");
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
                        break;
                    case R.id.userprofile:
                        Log.d("DashBoardActivity","*****Navigating to user profile");
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;
                    case R.id.bookAppt:
                        Log.d("DashBoardActivity","*****Navigating to book an appointment");
//                        startActivity(new Intent(getApplicationContext(), BookAppointmentActivity.class));
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new BookApppointmentFragment()).commit();
                        break;
                    case R.id.find_donor:
                        Log.d("DashBoardActivity","*****Navigating to find donors");
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new FindDonorFragment()).commit();
                        break;
                    case R.id.aboutUs:
                        Log.d("DashBoardActivity","*****Navigating to about us");
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AboutUsFragment()).commit();
                        break;
                    case R.id.contactUs:
                        Log.d("DashBoardActivity","*****Navigating to contact us");
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new ContactUsFragment()).commit();
                        break;
                    case R.id.logout:
                        mAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        Log.d("DashBoardActivity","*****Navigating to logout");
                        break;
                    default:
                        return true;
                }
                //Closing navigation drawer after selecting a menu item
                dl.closeDrawers();
                return true;
            }
        });

        if(savedInstanceState == null){
            //Initially loading fragment with HomeFragment page content
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
            nv.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
