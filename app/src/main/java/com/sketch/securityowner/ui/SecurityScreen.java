package com.sketch.securityowner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sketch.securityowner.Adapter.ViewPagerAdapter;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.R;
import com.sketch.securityowner.dialogs.DialogAlarmAdd;
import com.sketch.securityowner.dialogs.DialogCabAdd;
import com.sketch.securityowner.dialogs.DialogDeliveryAdd;
import com.sketch.securityowner.dialogs.DialogGuestAdd;
import com.sketch.securityowner.dialogs.DialogHelpAdd;
import com.sketch.securityowner.dialogs.LoaderDialog;


public class SecurityScreen extends AppCompatActivity {
    String TAG="Security";
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    LinearLayout ll_activty_class,ll_comunity,ll_security,car1;
    RelativeLayout rel_middle_icon;
    LinearLayout ll_activity;
    LinearLayout ll_bell;

    ImageView profile_image,img_cab,img_delivery,img_guest,img_help;

    GlobalClass globalClass;


    LoaderDialog loaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        ll_activty_class =  findViewById(R.id.button_E);
        ll_comunity =  findViewById(R.id.button_E3);
        ll_security =  findViewById(R.id.button_E1);
        rel_middle_icon =  findViewById(R.id.rel_middle_icon);
        car1 =  findViewById(R.id.car1);

        ll_security=findViewById(R.id.button_E1);
        img_cab=  findViewById(R.id.img_cab);
        img_guest=  findViewById(R.id.img_guest);
        img_delivery=  findViewById(R.id.img_delivery);
        img_help=  findViewById(R.id.img_help);
        rel_middle_icon =  findViewById(R.id.rel_middle_icon);
        ll_bell =  findViewById(R.id.ll_bell);
        car1 =  findViewById(R.id.car1);
        globalClass = (GlobalClass) getApplicationContext();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");


        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ll_activty_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),Activity_activity.class);
                startActivity(notification);
            }
        });
        ll_comunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),CommunityActivity.class);
                startActivity(notification);
            }
        });

        rel_middle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                car1.setVisibility(car1.getVisibility() ==
                        View.VISIBLE ? View.GONE : View.VISIBLE);

            }
        });


        img_cab.setOnClickListener(v -> {

            car1.setVisibility(View.GONE);

            DialogCabAdd dialogCabAdd = new DialogCabAdd(SecurityScreen.this);
            dialogCabAdd.show();

        });

        img_delivery.setOnClickListener(v -> {

            car1.setVisibility(View.GONE);

            DialogDeliveryAdd dialogDeliveryAdd = new DialogDeliveryAdd(SecurityScreen.this);
            dialogDeliveryAdd.show();

        });


        img_guest.setOnClickListener(v -> {
            car1.setVisibility(View.GONE);

            DialogGuestAdd dialogGuestAdd = new DialogGuestAdd(SecurityScreen.this);
            dialogGuestAdd.show();

        });

        img_help.setOnClickListener(v -> {
            car1.setVisibility(View.GONE);

            DialogHelpAdd dialogHelpAdd = new DialogHelpAdd(SecurityScreen.this);
            dialogHelpAdd.show();


        });

        ll_bell.setOnClickListener(v -> {

            car1.setVisibility(View.GONE);

            DialogAlarmAdd dialogAlarmAdd = new DialogAlarmAdd(SecurityScreen.this);
            dialogAlarmAdd.show();
        });


        tabViews();

    }

    private void tabViews(){

        ImageView img_temp = findViewById(R.id.img_temp);
        ImageView img_temp1 = findViewById(R.id.img_temp1);
        ImageView img_temp3 = findViewById(R.id.img_temp3);
        ImageView img_temp4 = findViewById(R.id.img_temp4);

        TextView tv = findViewById(R.id.tv);
        TextView tv1 = findViewById(R.id.tv1);
        TextView tv3 = findViewById(R.id.tv3);
        TextView tv4 = findViewById(R.id.tv4);


        img_temp1.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv1.setTextColor(getResources().getColor(R.color.blue));

    }


}
