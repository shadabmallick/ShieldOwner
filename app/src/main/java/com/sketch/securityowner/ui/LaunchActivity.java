package com.sketch.securityowner.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;


import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.Config;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.sketch.securityowner.dialogs.LoaderDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class LaunchActivity extends AppCompatActivity {
    String TAG="LaunchActivity";
    RelativeLayout rel_register,rel_login;
    GlobalClass globalClass;
    Shared_Preference prefrence;
    LoaderDialog loaderDialog;
    String device_id,name;
    EditText edt_register_no_name;
    String fcm_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_page);
        globalClass = (GlobalClass)getApplicationContext();
        fcm_token = FirebaseInstanceId.getInstance().getToken();
        prefrence = new Shared_Preference(LaunchActivity.this);
        prefrence.loadPrefrence();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        if (globalClass.isNetworkAvailable()) {


            if (globalClass.getLogin_status()) {

                if (prefrence.isFirstLogin()){
                    Intent intent = new Intent(LaunchActivity.this,
                            SettingActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(LaunchActivity.this,
                            Activity_activity.class);
                    startActivity(intent);
                    finish();
                }

            }

        }
        else {
            TastyToast.makeText(getApplicationContext(), "Check Network", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

        }

        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        rel_register=findViewById(R.id.rel_register);
        rel_login=findViewById(R.id.rel_login);
        edt_register_no_name=findViewById(R.id.edt_register_no);

        rel_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(register);
            }
        });
        rel_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edt_register_no_name.getText().toString().trim();
                if (!edt_register_no_name.getText().toString().isEmpty()) {
                    checkLogin(name);

                }
                else {
                    TastyToast.makeText(getApplicationContext(), "Enter Number", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                }


            }
        });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        fcm_token = token;

                        globalClass.setFcm_reg_token(token);

                        // Log and toast
                        Log.d(AppConfig.TAG, "token = "+token);

                    }
                });
    }


    private void checkLogin(final String number) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.login_otp, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                loaderDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");

                    Log.d(TAG, "Message: "+message);

                    if(status.equals("1") ) {


                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        Intent register=new Intent(getApplicationContext(),OtpScreen.class);
                        register.putExtra("number",name);

                        register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(register);
                        finish();
                        loaderDialog.dismiss();


                    } else {

                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG,
                                TastyToast.WARNING).show();
                    }

                    Log.d(TAG,"Token \n" +message);

                }catch (Exception e) {

                    Toast.makeText(getApplicationContext(),
                            "DATA NOT FOUND", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connection Error", Toast.LENGTH_LONG).show();
                loaderDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("phone_Number", number);
                params.put("device_type", "android");
                params.put("device_id", device_id);

                if (fcm_token==null){
                    params.put("fcm_token","123456");
                } else {
                    params.put("fcm_token",fcm_token);
                }


                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(LaunchActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }


}
