package com.shield.resident.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;


import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class OtpScreen extends AppCompatActivity {
    String TAG="OtpScreen";
     OtpView otpView;
     String phone,device_id;
    AVLoadingIndicatorView avLoadingIndicatorView;
    GlobalClass globalClass;
    Shared_Preference preference;
    TextView tv_otp_sent;
    String fcm_token, from, credential_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_screen);
        otpView = findViewById(R.id.otp_view);
        tv_otp_sent = findViewById(R.id.tv_otp_sent);
        avLoadingIndicatorView=findViewById(R.id.avi);

        globalClass=(GlobalClass)getApplicationContext();
        preference = new Shared_Preference(OtpScreen.this);
        preference.loadPrefrence();
        device_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        fcm_token = FirebaseInstanceId.getInstance().getToken();

        Bundle bundle = getIntent().getExtras();
        phone = bundle.getString("phone");
        from = bundle.getString("from");
        credential_type = bundle.getString("type");


        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {

                if (from.equals("registration")){
                    registrationOtp(otp);
                }else {
                    checkLoginOtp(otp);
                }

            }
        });


    }

    public void checkLoginOtp(final String otp){

        avLoadingIndicatorView.setVisibility(View.VISIBLE);

        startAnim();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "login RESPONSE: " + response.toString());

                stopAnim();

                try {

                    JSONObject object = new JSONObject(response);

                    String status = object.optString("status");
                    String message = object.optString("message");

                    if(status.equals("1")) {

                        JSONObject data = object.getJSONObject("data");
                        String user_id = data.optString("user_id");
                        String user_type = data.optString("user_type");
                        String flat_no = data.optString("flat_no");
                        String flat_name = data.optString("flat_name");
                        String block = data.optString("block");
                        String complex_name = data.optString("complex_name");
                        String complex_id = data.optString("complex_id");

                        String emailid = data.optString("emailid");
                        String user_name = data.optString("user_name");
                        String user_mobile = data.optString("user_mobile");
                        String user_image = data.optString("user_image");
                        String first_time_login = data.optString("first_time_login");
                        String is_login = data.optString("is_login");

                        globalClass.setId(user_id);
                        globalClass.setName(user_name);
                        globalClass.setPhone_number(user_mobile);

                        globalClass.setProfil_pic(user_image);
                        globalClass.setEmail(emailid);

                        globalClass.setFlat_id(flat_no);
                        globalClass.setFlat_name(flat_name);
                        globalClass.setBlock(block);
                        globalClass.setComplex_name(complex_name);
                        globalClass.setComplex_id(complex_id);
                        globalClass.setFirst_time_login(first_time_login);
                        globalClass.setIs_login(is_login);
                        globalClass.setLogin_status(true);

                        if (user_type.equals("Flat Owner")){
                            globalClass.setUser_type("owner");
                        }else if (user_type.equals("Tenant")){
                            globalClass.setUser_type("tenant");
                        }else if (user_type.equals("Family Members")){
                            globalClass.setUser_type("member");
                        }



                        preference.savePrefrence();

                        Intent setting=new Intent(getApplicationContext(),SettingActivity.class);
                        setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(setting);
                        finish();

                    } else {
                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(),
                            "DATA NOT FOUND", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("phone_Number", phone);
                params.put("otp", otp);
                params.put("device_type","android");
                params.put("device_id",device_id);
                params.put("fcm_token", globalClass.getFcm_reg_token());
                params.put("type", credential_type);

                Log.d(TAG, "params "+params);

                return params;
            }

        };
        // Adding request to request queue
               VolleySingleton.getInstance(OtpScreen.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));



    }

    public void registrationOtp(final String otp){

        avLoadingIndicatorView.setVisibility(View.VISIBLE);

        startAnim();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.registration_login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "registration RESPONSE: " + response.toString());

                stopAnim();

                try {

                    JSONObject object = new JSONObject(response);

                    String status = object.optString("status");
                    String message = object.optString("message");

                    if(status.equals("1")) {

                        JSONObject data = object.getJSONObject("data");
                        String user_id = data.optString("user_id");
                        String user_type = data.optString("user_type");
                        String flat_no = data.optString("flat_no");
                        String flat_name = data.optString("flat_name");
                        String block = data.optString("block");
                        String complex_name = data.optString("complex_name");
                        String complex_id = data.optString("complex_id");

                        String emailid = data.optString("emailid");
                        String user_name = data.optString("user_name");
                        String user_mobile = data.optString("user_mobile");
                        String user_image = data.optString("user_image");
                        String first_time_login = data.optString("first_time_login");
                        String is_login = data.optString("is_login");

                        globalClass.setId(user_id);
                        globalClass.setName(user_name);
                        globalClass.setPhone_number(user_mobile);
                        globalClass.setUser_type(user_type);
                        globalClass.setProfil_pic(user_image);
                        globalClass.setEmail(emailid);

                        globalClass.setFlat_id(flat_no);
                        globalClass.setFlat_name(flat_name);
                        globalClass.setBlock(block);
                        globalClass.setComplex_name(complex_name);
                        globalClass.setComplex_id(complex_id);
                        globalClass.setFirst_time_login(first_time_login);
                        globalClass.setIs_login(is_login);
                        globalClass.setLogin_status(true);

                        preference.savePrefrence();

                        Intent setting=new Intent(getApplicationContext(),SettingActivity.class);
                        setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(setting);
                        finish();

                    } else {
                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(),
                            "DATA NOT FOUND", TastyToast.LENGTH_LONG, TastyToast.WARNING);

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


                params.put("phone_Number", phone);
                params.put("otp", otp);
                params.put("device_type","android");
                params.put("device_id",device_id);
                params.put("fcm_token", globalClass.getFcm_reg_token());

                Log.d(TAG, "params "+params);

                return params;
            }

        };
        // Adding request to request queue
        VolleySingleton.getInstance(OtpScreen.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));



    }

    void startAnim(){
        avLoadingIndicatorView.show();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
    }
}
