package com.sketch.securityowner.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sketch.securityowner.Adapter.ViewPagerCommunity;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.R;
import com.sketch.securityowner.dialogs.DialogAlarmAdd;
import com.sketch.securityowner.dialogs.DialogCabAdd;
import com.sketch.securityowner.dialogs.DialogDeliveryAdd;
import com.sketch.securityowner.dialogs.DialogGuestAdd;
import com.sketch.securityowner.dialogs.DialogHelpAdd;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunityActivity extends AppCompatActivity {
    String TAG="CommunityActivity";
    TabLayout tabLayout;
    ViewPager viewPager;
    ProgressDialog pd;
    ImageView img_cab,img_delivery,img_guest,img_help;

    GlobalClass globalClass;
    ArrayList<HashMap<String,String>> productDetaiils;
    ArrayList<HashMap<String,String>> Category;
    ArrayList<HashMap<String,String>> DeliveryList;
    ArrayList<HashMap<String,String>> HelpList;
    ArrayList<HashMap<String,String>> productDetaiils_sub;
    ArrayList<HashMap<String,String>> staffList;
    ViewPagerCommunity viewPagerAdapter;
    LinearLayout ll_activity,ll_security,car1;
    RelativeLayout rel_middle_icon;
    LinearLayout ll_bell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_class);
        ll_activity=findViewById(R.id.button_E);
        ll_security=findViewById(R.id.button_E1);
        img_cab=  findViewById(R.id.img_cab);
        img_guest=  findViewById(R.id.img_guest);
        img_delivery=  findViewById(R.id.img_delivery);
        img_help=  findViewById(R.id.img_help);
        rel_middle_icon =  findViewById(R.id.rel_middle_icon);
        ll_bell =  findViewById(R.id.ll_bell);
        car1 =  findViewById(R.id.car1);
        globalClass = (GlobalClass) getApplicationContext();

        pd = new ProgressDialog(CommunityActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));

        productDetaiils=new ArrayList<>();
        staffList=new ArrayList<>();
        productDetaiils_sub=new ArrayList<>();
        DeliveryList=new ArrayList<>();
        HelpList=new ArrayList<>();
        Category=new ArrayList<>();


        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerCommunity(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ll_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(CommunityActivity.this,
                        SecurityScreen.class);
                startActivity(notification);
            }
        });
        ll_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification = new Intent(CommunityActivity.this,
                        Activity_activity.class);
                startActivity(notification);
            }
        });

        rel_middle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                car1.setVisibility(car1.getVisibility()
                        == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });


        img_cab.setOnClickListener(v -> {

            car1.setVisibility(View.GONE);

            DialogCabAdd dialogCabAdd = new DialogCabAdd(CommunityActivity.this);
            dialogCabAdd.show();

        });

        img_delivery.setOnClickListener(v -> {

            car1.setVisibility(View.GONE);

            DialogDeliveryAdd dialogDeliveryAdd = new DialogDeliveryAdd(CommunityActivity.this);
            dialogDeliveryAdd.show();

        });


        img_guest.setOnClickListener(v -> {
            car1.setVisibility(View.GONE);

            DialogGuestAdd dialogGuestAdd = new DialogGuestAdd(CommunityActivity.this);
            dialogGuestAdd.show();


        });

        img_help.setOnClickListener(v -> {
            car1.setVisibility(View.GONE);

            DialogHelpAdd dialogHelpAdd = new DialogHelpAdd(CommunityActivity.this);
            dialogHelpAdd.show();

        });

        ll_bell.setOnClickListener(v -> {

            car1.setVisibility(View.GONE);

            DialogAlarmAdd dialogAlarmAdd = new DialogAlarmAdd(CommunityActivity.this);
            dialogAlarmAdd.show();
        });





    }

}
