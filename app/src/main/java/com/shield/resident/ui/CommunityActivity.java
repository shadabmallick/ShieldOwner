package com.shield.resident.ui;

import android.app.ProgressDialog;
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
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Adapter.ViewPagerCommunity;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.R;
import com.shield.resident.dialogs.DialogAlarmAdd;
import com.shield.resident.dialogs.DialogCabAdd;
import com.shield.resident.dialogs.DialogDeliveryAdd;
import com.shield.resident.dialogs.DialogGuestActivityAdd;
import com.shield.resident.dialogs.DialogGuestAdd;
import com.shield.resident.dialogs.DialogHelpActivityAdd;
import com.shield.resident.dialogs.DialogHelpAdd;

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
    LinearLayout ll_activity,ll_security,ll_visitor_option, ll_app_help;
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
        ll_visitor_option =  findViewById(R.id.ll_visitor_option);
        ll_app_help =  findViewById(R.id.button_E4);
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

                if (globalClass.getIs_tenant().equals("no")){
                    ll_visitor_option.setVisibility(ll_visitor_option.getVisibility()
                            == View.VISIBLE ? View.GONE : View.VISIBLE);
                }else {
                    TastyToast.makeText(getApplicationContext(),
                            "You shifted this features to your tenant",
                            TastyToast.LENGTH_LONG, TastyToast.INFO);
                }
            }
        });

        ll_app_help.setOnClickListener(v -> {
            Intent notification=new Intent(CommunityActivity.this, AppHelp.class);
            startActivity(notification);
        });


        img_cab.setOnClickListener(v -> {

            ll_visitor_option.setVisibility(View.GONE);

            DialogCabAdd dialogCabAdd = new DialogCabAdd(CommunityActivity.this);
            dialogCabAdd.show();

        });

        img_delivery.setOnClickListener(v -> {

            ll_visitor_option.setVisibility(View.GONE);

            DialogDeliveryAdd dialogDeliveryAdd = new DialogDeliveryAdd(CommunityActivity.this);
            dialogDeliveryAdd.show();

        });


        img_guest.setOnClickListener(v -> {
            ll_visitor_option.setVisibility(View.GONE);

            /*DialogGuestAdd dialogGuestAdd = new DialogGuestAdd(CommunityActivity.this);
            dialogGuestAdd.show();*/


            Intent intent = new Intent(CommunityActivity.this,
                    DialogGuestActivityAdd.class);
            startActivity(intent);


        });

        img_help.setOnClickListener(v -> {
            ll_visitor_option.setVisibility(View.GONE);

            /*DialogHelpAdd dialogHelpAdd = new DialogHelpAdd(CommunityActivity.this);
            dialogHelpAdd.show();*/


            Intent intent = new Intent(CommunityActivity.this,
                    DialogHelpActivityAdd.class);
            startActivity(intent);


        });

        ll_bell.setOnClickListener(v -> {

            ll_visitor_option.setVisibility(View.GONE);

            DialogAlarmAdd dialogAlarmAdd = new DialogAlarmAdd(CommunityActivity.this);
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


        img_temp3.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv3.setTextColor(getResources().getColor(R.color.blue));

    }

}
