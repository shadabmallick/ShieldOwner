package com.sketch.securityowner.ui;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sketch.securityowner.Adapter.ViewPagerAdapter;
import com.sketch.securityowner.R;


public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    LinearLayout ll_activty_class,ll_comunity,ll_security,car1;
    RelativeLayout rel_profile,rel_middle_icon;
    private  boolean button1IsVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ll_activty_class =  findViewById(R.id.button_E);
        ll_comunity =  findViewById(R.id.button_E3);
        ll_security =  findViewById(R.id.button_E1);
        rel_middle_icon =  findViewById(R.id.rel_middle_icon);
        car1 =  findViewById(R.id.car1);




        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ll_activty_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),Activity_activity.class);
                notification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        ll_comunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),CommunityActivity.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        ll_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),MainActivity.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        rel_middle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true)
                {

                    car1.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false)
                {
                    // car1.animate().alpha(1.0f);
                    car1.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
            }
        });

    }


}
