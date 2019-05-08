package com.example.bloodbank3.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bloodbank3.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.splashImg);

        final String imagePath = "https://v3u9z9k5.stackpathcdn.com/wp-content/uploads/2016/07/blood-drop-768x974.jpg";

        Picasso.get().load(imagePath).into(imageView, new Callback() { //.rotate(90f)
            @Override
            public void onSuccess() {
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                Log.d("SplashActivity","*****Navigating to LoginActivity");
                finish();
            }
        }, 4000);
    }
}
