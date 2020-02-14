package com.shield.resident.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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

import cz.msebera.android.httpclient.Header;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class NotificationManager extends AppCompatActivity {
    ImageView img_back;
    TextView tv_msg3, tv_ivr_number, tv_update_ivr, txt_send_notification;

    LoaderDialog loaderDialog;
    Shared_Preference preference;
    GlobalClass globalClass;

    RelativeLayout rel_main;

    Switch switch_daily_help_entry, switch_daily_help_exit,
            switch_guest_entry, switch_guest_exit,
            switch_delivery_entry, switch_delivery_exit,
            switch_cab_entry, switch_cab_exit,
            switch_others_help;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        preference = new Shared_Preference(this);
        preference.loadPrefrence();
        globalClass = (GlobalClass) getApplicationContext();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");


        img_back=findViewById(R.id.img_back);


        rel_main=findViewById(R.id.rel_main);

        tv_msg3=findViewById(R.id.tv_msg3);

        switch_daily_help_entry = findViewById(R.id.switch_daily_help_entry);
        switch_daily_help_exit = findViewById(R.id.switch_daily_help_exit);
        switch_guest_entry = findViewById(R.id.switch_guest_entry);
        switch_guest_exit = findViewById(R.id.switch_guest_exit);
        switch_delivery_entry = findViewById(R.id.switch_delivery_entry);
        switch_delivery_exit = findViewById(R.id.switch_delivery_exit);
        switch_cab_entry = findViewById(R.id.switch_cab_entry);
        switch_cab_exit = findViewById(R.id.switch_cab_exit);
        switch_others_help = findViewById(R.id.switch_others_help);
        tv_update_ivr = findViewById(R.id.tv_update_ivr);
        tv_ivr_number = findViewById(R.id.tv_ivr_number);
        txt_send_notification = findViewById(R.id.txt_send_notification);


        rel_main.setVisibility(View.INVISIBLE);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        tv_msg3.setOnClickListener(v -> {

            startInstalledAppDetailsActivity(NotificationManager.this);

        });


        tv_update_ivr.setOnClickListener(v -> {

            addOrEditIVRPhone();

        });

        txt_send_notification.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationManager.this, CallUi.class);
            startActivity(intent);
        });

        getIVRNumber();


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

                        String guest_entry_notification = data.getString("guest_entry_notification");
                        String guest_exit_notification = data.getString("guest_exit_notification");
                        String delivery_entry_notification = data.getString("delivery_entry_notification");
                        String delivery_exit_notification = data.getString("delivery_exit_notification");
                        String help_notification = data.getString("help_notification");
                        String cab_entry_notification = data.getString("cab_entry_notification");
                        String cab_exit_notification = data.getString("cab_exit_notification");
                        String staff_exit_notification = data.getString("staff_exit_notification");
                        String staff_entry_notification = data.getString("staff_entry_notification");
                        //String car_notification = data.getString("car_notification");


                        if (guest_entry_notification.equals("y")){
                            switch_guest_entry.setChecked(true);
                        }else {
                            switch_guest_entry.setChecked(false);
                        }
                        if (guest_exit_notification.equals("y")){
                            switch_guest_exit.setChecked(true);
                        }else {
                            switch_guest_exit.setChecked(false);
                        }




                        if (delivery_entry_notification.equals("y")){
                            switch_delivery_entry.setChecked(true);
                        }else {
                            switch_delivery_entry.setChecked(false);
                        }
                        if (delivery_exit_notification.equals("y")){
                            switch_delivery_exit.setChecked(true);
                        }else {
                            switch_delivery_exit.setChecked(false);
                        }




                        if (cab_entry_notification.equals("y")){
                            switch_cab_entry.setChecked(true);
                        }else {
                            switch_cab_entry.setChecked(false);
                        }
                        if (cab_exit_notification.equals("y")){
                            switch_cab_exit.setChecked(true);
                        }else {
                            switch_cab_exit.setChecked(false);
                        }





                        if (staff_entry_notification.equals("y")){
                            switch_daily_help_entry.setChecked(true);
                        }else {
                            switch_daily_help_entry.setChecked(false);
                        }
                        if (staff_exit_notification.equals("y")){
                            switch_daily_help_exit.setChecked(true);
                        }else {
                            switch_daily_help_exit.setChecked(false);
                        }




                        if (help_notification.equals("y")){
                            switch_others_help.setChecked(true);
                        }else {
                            switch_others_help.setChecked(false);
                        }




                        checkChangeViews();

                    }

                    loaderDialog.dismiss();

                    rel_main.setVisibility(View.VISIBLE);

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
                // Posting parameters to activity_login url
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

    private void checkChangeViews(){

        switch_guest_entry.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("guest_entry_notification", "y");
            }else {
                getNotificationStatusUpdate("guest_entry_notification",  "n");
            }

        });
        switch_guest_exit.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("guest_exit_notification",   "y");
            }else {
                getNotificationStatusUpdate("guest_exit_notification", "n");
            }

        });



        switch_cab_entry.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("cab_entry_notification", "y");
            }else {
                getNotificationStatusUpdate("cab_entry_notification", "n");
            }
        });
        switch_cab_exit.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("cab_exit_notification", "y");
            }else {
                getNotificationStatusUpdate("cab_exit_notification", "n");
            }
        });



        switch_delivery_entry.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("delivery_entry_notification", "y");
            }else {
                getNotificationStatusUpdate("delivery_entry_notification", "n");
            }
        });
        switch_delivery_exit.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("delivery_exit_notification", "y");
            }else {
                getNotificationStatusUpdate("delivery_exit_notification", "n");
            }

        });


        switch_daily_help_entry.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("staff_entry_notification", "y");
            }else {
                getNotificationStatusUpdate("staff_entry_notification", "n");
            }

        });
        switch_daily_help_exit.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("staff_exit_notification", "y");
            }else {
                getNotificationStatusUpdate("staff_exit_notification", "n");
            }

        });

        switch_others_help.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                getNotificationStatusUpdate("help_notification", "y");
            }else {
                getNotificationStatusUpdate("help_notification", "n");
            }

        });

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


                        TastyToast.makeText(getApplicationContext(), message,
                                TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }else {
                        TastyToast.makeText(getApplicationContext(), message,
                                TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    }

                    loaderDialog.dismiss();

                    //linear_main.setVisibility(View.VISIBLE);

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
                // Posting parameters to activity_login url
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



    public void getIVRNumber(){

        String url = AppConfig.ivr_number;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("user_id",globalClass.getId());
        params.put("flat_id",globalClass.getFlat_id());


        // Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());

        int DEFAULT_TIMEOUT = 15 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "ivr_number- " + response.toString());
                    try {

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            String ivr_number_res = response.getString("ivr_number");

                            tv_ivr_number.setText(ivr_number_res);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }


    public void addOrEditIVRPhone(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.add_phone, null);
        alertDialog.setView(convertView);

        final AlertDialog show = alertDialog.show();

        EditText add_number = convertView.findViewById(R.id.add_number);
        LinearLayout ll_submit = convertView.findViewById(R.id.ll_submit);
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = add_number.getText().toString();

                if (number.trim().length() == 0){
                    TastyToast.makeText(getApplicationContext(),
                            "Enter a phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (number.trim().length() < 10){
                    TastyToast.makeText(getApplicationContext(),
                            "Enter 10-digit phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                EditIVRNumber(number);
                show.dismiss();
            }
        });

    }

    public void EditIVRNumber(final String number){

        loaderDialog.show();

        String url = AppConfig.update_ivr_number;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("user_id",globalClass.getId());
        params.put("ivr_number",number);
        params.put("flat_id",globalClass.getFlat_id());


       // Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 15 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "update_ivr_number- " + response.toString());
                    try {
                        loaderDialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG,
                                TastyToast.DEFAULT);

                        if (status == 1) {
                            getIVRNumber();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        TastyToast.makeText(getApplicationContext(),
                                "Error Connection",
                                TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG, "update_ivr_number- " +responseString);
                loaderDialog.dismiss();
            }
        });


    }
}
