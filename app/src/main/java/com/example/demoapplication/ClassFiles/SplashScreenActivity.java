package com.example.demoapplication.ClassFiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.demoapplication.R;
import com.example.demoapplication.UtilClass.AppPreferences;
import com.example.demoapplication.UtilClass.AppUtils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread background = new Thread() {
            public void run() {
                try {
                    Intent i;
                    // Thread will sleep for 5 seconds
                    sleep(2 * 1000);
                    // After 5 seconds redirect to another intent
                    if (AppPreferences.getIsLogin(SplashScreenActivity.this, AppUtils.ISLOGIN)) {
                        i = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                } catch (Exception e) {

                }
            }
        };
        background.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
