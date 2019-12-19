package com.shield.resident.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class NotificationManager extends AppCompatActivity {
    ImageView img_back;
    TextView tv_msg3;

    LoaderDialog loaderDialog;
    Shared_Preference preference;
    GlobalClass globalClass;

    LinearLayout linear_main;

    Switch switch_staff, switch_guest, switch_cab, switch_car, switch_delivery, switch_help;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_noification);

        preference = new Shared_Preference(this);
        preference.loadPrefrence();
        globalClass = (GlobalClass) getApplicationContext();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");


        img_back=findViewById(R.id.img_back);
        linear_main=findViewById(R.id.linear_main);
        switch_staff=findViewById(R.id.switch_staff);
        switch_guest=findViewById(R.id.switch_guest);
        switch_cab=findViewById(R.id.switch_cab);
        switch_car=findViewById(R.id.switch_car);
        switch_delivery=findViewById(R.id.switch_delivery);
        switch_help=findViewById(R.id.switch_help);
        tv_msg3=findViewById(R.id.tv_msg3);


        linear_main.setVisibility(View.INVISIBLE);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        switch_guest.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("guest", "y");
            }else {
                getNotificationStatusUpdate("guest", "n");
            }

        });

        switch_cab.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("cab", "y");
            }else {
                getNotificationStatusUpdate("cab", "n");
            }
        });

        switch_car.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("car", "y");
            }else {
                getNotificationStatusUpdate("car", "n");
            }
        });

        switch_delivery.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("delivery", "y");
            }else {
                getNotificationStatusUpdate("delivery", "n");
            }

        });

        switch_staff.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("staff", "y");
            }else {
                getNotificationStatusUpdate("staff", "n");
            }

        });

        switch_help.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("help", "y");
            }else {
                getNotificationStatusUpdate("help", "n");
            }

        });

        tv_msg3.setOnClickListener(v -> {

            startInstalledAppDetailsActivity(NotificationManager.this);

        });




        getNotificationStatus();

    }


    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }



    private void getNotificationStatus() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.user_notification_status_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "notification_status Response: " + response.toString());


                try {
                    JSONObject jsonObject = new JSONObject(response);


                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");

                    if(status.equals("1") ) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        String guest_notification = data.getString("guest_notification");
                        String delivery_notification = data.getString("delivery_notification");
                        String help_notification = data.getString("help_notification");
                        String cab_notification = data.getString("cab_notification");
                        String staff_notification = data.getString("staff_notification");
                        String car_notification = data.getString("car_notification");


                        if (guest_notification.equals("y")){
                            switch_guest.setChecked(true);
                        }else {
                            switch_guest.setChecked(false);
                        }


                        if (delivery_notification.equals("y")){
                            switch_delivery.setChecked(true);
                        }else {
                            switch_delivery.setChecked(false);
                        }


                        if (help_notification.equals("y")){
                            switch_help.setChecked(true);
                        }else {
                            switch_help.setChecked(false);
                        }


                        if (cab_notification.equals("y")){
                            switch_cab.setChecked(true);
                        }else {
                            switch_cab.setChecked(false);
                        }


                        if (car_notification.equals("y")){
                            switch_car.setChecked(true);
                        }else {
                            switch_car.setChecked(false);
                        }


                        if (staff_notification.equals("y")){
                            switch_staff.setChecked(true);
                        }else {
                            switch_staff.setChecked(false);
                        }


                    }

                    loaderDialog.dismiss();

                    linear_main.setVisibility(View.VISIBLE);

                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "DATA NOT FOUND: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connection Error", Toast.LENGTH_LONG).show();
                loaderDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("user_id", globalClass.getId());

                Log.d("TAG", "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(NotificationManager.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }


    private void getNotificationStatusUpdate(final String type, String value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.update_user_notification_status, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "notification_status Response: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");

                    if(status.equals("1") ) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        String guest_notification = data.getString("guest_notification");
                        String delivery_notification = data.getString("delivery_notification");
                        String help_notification = data.getString("help_notification");
                        String cab_notification = data.getString("cab_notification");
                        String staff_notification = data.getString("staff_notification");
                        String car_notification = data.getString("car_notification");


                        /*if (guest_notification.equals("y")){
                            switch_guest.setChecked(true);
                        }else {
                            switch_guest.setChecked(false);
                        }


                        if (delivery_notification.equals("y")){
                            switch_delivery.setChecked(true);
                        }else {
                            switch_delivery.setChecked(false);
                        }


                        if (help_notification.equals("y")){
                            switch_help.setChecked(true);
                        }else {
                            switch_help.setChecked(false);
                        }


                        if (cab_notification.equals("y")){
                            switch_cab.setChecked(true);
                        }else {
                            switch_cab.setChecked(false);
                        }


                        if (car_notification.equals("y")){
                            switch_car.setChecked(true);
                        }else {
                            switch_car.setChecked(false);
                        }


                        if (staff_notification.equals("y")){
                            switch_staff.setChecked(true);
                        }else {
                            switch_staff.setChecked(false);
                        }*/

                       /* TastyToast.makeText(getApplicationContext(), message,
                                TastyToast.LENGTH_LONG, TastyToast.SUCCESS);*/

                    }else {
                        /*TastyToast.makeText(getApplicationContext(), message,
                                TastyToast.LENGTH_LONG, TastyToast.WARNING);*/
                    }

                    loaderDialog.dismiss();

                    linear_main.setVisibility(View.VISIBLE);

                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "DATA NOT FOUND: " + error.getMessage());
                loaderDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("user_id", globalClass.getId());
                params.put("type", type);
                params.put("status", value);

                Log.d("TAG", "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(NotificationManager.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

}
