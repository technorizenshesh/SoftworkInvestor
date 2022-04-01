package com.softworkinvestor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.softworkinvestor.R;
import com.softworkinvestor.retrofit.Constant;
import com.softworkinvestor.utitlity.SharedPreferenceUtility;

public class SplashActivity extends AppCompatActivity {
    private boolean isUserLoggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        finds();
        isUserLoggedIn = SharedPreferenceUtility.getInstance(SplashActivity.this).getBoolean(Constant.IS_USER_LOGGED_IN);

    }

    private void finds() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn) {
                    startActivity(new Intent(SplashActivity.this, HomeAct.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                }            }
        },3000);
    }


}