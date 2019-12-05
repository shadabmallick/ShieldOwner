package com.shield.resident.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.splunk.mint.Mint;

public class Splash extends AppCompatActivity {


    GlobalClass globalClass;
    Shared_Preference prefrence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        globalClass = (GlobalClass)getApplicationContext();
        prefrence = new Shared_Preference(Splash.this);
        prefrence.loadPrefrence();

        Mint.initAndStartSession(this.getApplication(), "8a38783c");

        threadFor();
    }

    private void threadFor(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (globalClass.isNetworkAvailable()) {

                    if (globalClass.getLogin_status()) {

                        if (prefrence.isFirstLogin()) {
                            Intent intent = new Intent(Splash.this,
                                    SettingActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(Splash.this,
                                    Activity_activity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {

                        Intent intent = new Intent(Splash.this,
                                WelcomeActivity.class);
                        startActivity(intent);
                        finish();

                    }

                }

            }
        }, 2000);

    }
}
