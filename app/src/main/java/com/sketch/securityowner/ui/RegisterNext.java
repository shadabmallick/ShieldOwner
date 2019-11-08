package com.sketch.securityowner.ui;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;


import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class RegisterNext extends AppCompatActivity {
    String TAG="RegisterNext";
    RelativeLayout rel_back,rel_otp,rel_login;
    EditText edt_name,edt_phone_no,edt_email;
    GlobalClass globalClass;
    AVLoadingIndicatorView avLoadingIndicatorView;
    String email,phone,name,city_id,block,complex_id,flat_no,deviceId,complex_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_next);
        globalClass=(GlobalClass)getApplicationContext();
        rel_back=findViewById(R.id.rel_back);
        rel_otp=findViewById(R.id.rel_otp);
        rel_login=findViewById(R.id.rel_login);
        edt_name=findViewById(R.id.edt_name);
        edt_phone_no=findViewById(R.id.edt_phone_no);
        edt_email=findViewById(R.id.edt_email);
        avLoadingIndicatorView=findViewById(R.id.avi);

        Bundle bundle = getIntent().getExtras();
       city_id = bundle.getString("city_id");
         block = bundle.getString("block");
         complex_id = bundle.getString("complex_id");
        complex_name = bundle.getString("complex_name");
         flat_no = bundle.getString("flat_no");
         deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rel_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_next=new Intent(getApplicationContext(),LaunchActivity.class);
                startActivity(register_next);
            }
        });

        rel_otp.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
               name = edt_name.getText().toString().trim();
                phone = edt_phone_no.getText().toString().trim();
                email = edt_email.getText().toString().trim();
                if (globalClass.isNetworkAvailable()) {
                    if (!edt_name.getText().toString().isEmpty()) {

                        if (!edt_phone_no.getText().toString().isEmpty()) {

                            if (!edt_email.getText().toString().isEmpty()) {
                                checkRegister(name,phone,email);

                            }
                            else {
                                TastyToast.makeText(getApplicationContext(), "Please enter email", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            }

                        } else {
                            TastyToast.makeText(getApplicationContext(), "Please enter phone number", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        }

                    } else {
                        TastyToast.makeText(getApplicationContext(), "Please enter name", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    }
                } else {
                    TastyToast.makeText(getApplicationContext(), "Check internet Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                }
            }
        });
    }
    public void checkRegister(final String name,final String phone,final String email){
        String tag_string_req = "req_login";
        avLoadingIndicatorView.setVisibility(View.VISIBLE);

        startAnim();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.registration, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                stopAnim();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d("jobj", "" + jobj);
                    if(status.equals("1")) {

                        //array.add("Select Location");

                        String otp = jobj.get("otp").getAsString().replaceAll("\"", "");
                        Log.d(TAG, "onResponse: "+otp);
                        Intent otpScreen=new Intent(getApplicationContext(),OtpScreen.class);
                        otpScreen.putExtra("phone_Number", phone);
                        otpScreen.putExtra("otp", otp);
                        otpScreen.putExtra("device_id", deviceId);

                        otpScreen.putExtra("fcm_token", "12345");

                        otpScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(otpScreen);


                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "DATA NOT FOUND", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();



                params.put("name",name);
                params.put("emailid",email);
                params.put("device_type","android");
                params.put("device_id",deviceId);
                params.put("phone_Number",phone);
                params.put("complex_Number",complex_id);
                params.put("flat_id",flat_no);
                params.put("fcm_token","12222");
                params.put("city_id",city_id);
                params.put("complex_name",complex_name);
                params.put("user_type","owner");
                Log.d(TAG, "params "+params);

                return params;
            }

        };
        // Adding request to request queue
        VolleySingleton.getInstance(RegisterNext.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));



    }
    void startAnim(){
        avLoadingIndicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }
}
