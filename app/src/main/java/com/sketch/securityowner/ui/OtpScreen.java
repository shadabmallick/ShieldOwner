package com.sketch.securityowner.ui;

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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class OtpScreen extends AppCompatActivity {
    String TAG="OtpScreen";
     OtpView otpView;
     String number,otp,fcm,device_id;
    AVLoadingIndicatorView avLoadingIndicatorView;
    GlobalClass globalClass;
    Shared_Preference preference;
    TextView tv_otp_sent;
    String fcm_token;

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
        number = bundle.getString("number");

        tv_otp_sent.setText(number);
      /*  otp = bundle.getString("otp");
        fcm = bundle.getString("fcm_token");
        device_id = bundle.getString("device_id");*/

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {

                /*Intent setting=new Intent(getApplicationContext(),SettingActivity.class);
                setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(setting);*/

               checkOtp(otp);

            }
        });


    }

    public void checkOtp(final String otp){
        String tag_string_req = "req_login";
        avLoadingIndicatorView.setVisibility(View.VISIBLE);

        startAnim();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"login", new Response.Listener<String>() {

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
                       JsonObject data=jobj.getAsJsonObject("data");
                        String user_id = data.get("user_id").getAsString().replaceAll("\"", "");
                        String user_type = data.get("user_type").getAsString().replaceAll("\"", "");
                        String flat_no = data.get("flat_no").getAsString().replaceAll("\"", "");
                        String flat_name = data.get("flat_name").getAsString().replaceAll("\"", "");
                        String block = data.get("block").getAsString().replaceAll("\"", "");
                        String complex_name = data.get("complex_name").getAsString().replaceAll("\"", "");
                        String complex_id = data.get("complex_id").getAsString().replaceAll("\"", "");

                        String emailid = data.get("emailid").getAsString().replaceAll("\"", "");
                        String user_name = data.get("user_name").getAsString().replaceAll("\"", "");
                        String user_mobile = data.get("user_mobile").getAsString().replaceAll("\"", "");
                        String user_image = data.get("user_image").getAsString().replaceAll("\"", "");
                        String first_time_login = data.get("first_time_login").getAsString().replaceAll("\"", "");
                        String is_login = data.get("is_login").getAsString().replaceAll("\"", "");
                        globalClass.setId(user_id);
                        globalClass.setName(user_name);
                        globalClass.setPhone_number(user_mobile);
                        globalClass.setUser_type(user_type);
                        globalClass.setProfil_pic(user_image);
                        globalClass.setEmail(emailid);

                        globalClass.setFlat_no(flat_no);
                        globalClass.setFlat_name(flat_name);
                        globalClass.setBlock(block);
                        globalClass.setComplex_name(complex_name);
                        globalClass.setComplex_id(complex_id);
                        globalClass.setFirst_time_login(first_time_login);
                        globalClass.setIs_login(is_login);
                        globalClass.setLogin_status(true);
                        //array.add("Select Location");
                        preference.savePrefrence();

                        Intent setting=new Intent(getApplicationContext(),SettingActivity.class);
                        setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(setting);


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



                params.put("phone_Number",number);
                params.put("otp",otp);
                params.put("device_type","android");
                params.put("device_id",device_id);
                params.put("fcm_token",fcm_token);

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
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }
}
