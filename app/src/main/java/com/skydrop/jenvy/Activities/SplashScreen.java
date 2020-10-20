package com.skydrop.jenvy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.skydrop.jenvy.R;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIMEOUT);
    }
}