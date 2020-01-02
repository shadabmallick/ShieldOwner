package com.shield.resident.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shield.resident.R;

public class AboutUs extends AppCompatActivity {
    ImageView img_back;
    TextView tv_aboutus, tv_appVersion;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        tv_aboutus=findViewById(R.id.tv_aboutus);
        img_back=findViewById(R.id.img_back);
        tv_appVersion=findViewById(R.id.tv_appVersion);


        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pInfo.versionName;
            int versionCode = pInfo.versionCode;

            Log.d("TAG", "versionName = "+versionName);
            Log.d("TAG", "versionCode = "+versionCode);

            tv_appVersion.setText("App Version: "+versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        String s = "SHIELD is an easy to understand security app/platform that is intelligent, efficient and affordable. It is a realistic and reasonable app that can be used in residential complexes, private security agencies, corporate companies, hospitals and educational institutions.\n" +
                " \n" +
                "SHIELD will make sure the user lives peacefully and not risk office or home security.\n" +
                "\n" +
                "\n" +
                "Some of the features include:\n" +
                "\n" +
                "Single page management: The managing committee can check all the activities and updates through instant notifications. They can also look into different complaints, attendance of servants and communicate with any member of the group through the app.\n" +
                "\n" +
                "Approval of guests: Any visitors or outsiders will only gain entry if approved by the concerned resident. They will either by entering through a passkey or be pre-approved by the residents.\n" +
                "\n" +
                "Delivery option: The SHIELD app offers 'Leave at gate' facility for leaving any courier or parcel at the entry gate to be collected later by the owner.\n" +
                "\n" +
                "Payments: The payment section of SHIELD offers the users a unified accounting platform. Payments in various modes can be easily processed by the platform. Different kinds of monthly reports and invoices relating to the premises can also be accessed through the app.\n" +
                "\n" +
                "What can the users expect from the app?\n" +
                "Authorization, validation and managing at the entry gate\n" +
                "Notification with photo before anyone knocks at the door\n" +
                "Notification upon guest arrival\n" +
                "Notification when help arrives\n" +
                "Pre-approval of the cab to enter the premises\n" +
                "\n" +
                "Welcome to a safe and secured neighbourhood that is protected by SHIELD.";

        tv_aboutus.setText(s);

    }


}
