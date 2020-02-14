package com.shield.resident.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

public class Login extends AppCompatActivity {
    String TAG="Login";
    RelativeLayout rel_register,rel_login;
    GlobalClass globalClass;
    Shared_Preference prefrence;
    LoaderDialog loaderDialog;
    String device_id,number;
    EditText edt_register_no_name;
    String credential_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globalClass = (GlobalClass)getApplicationContext();
        prefrence = new Shared_Preference(Login.this);
        prefrence.loadPrefrence();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");


        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        rel_register=findViewById(R.id.rel_register);
        rel_login=findViewById(R.id.rel_login);
        edt_register_no_name=findViewById(R.id.edt_register_no);

        rel_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(Login.this,
                        RegisterUserDetails.class);
                startActivity(register);
            }
        });


        rel_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = edt_register_no_name.getText().toString().trim();

                Log.d(TAG, "number: " +number);


                if (!globalClass.isNetworkAvailable()) {
                    TastyToast.makeText(Login.this,
                            "Check your internet connection", TastyToast.LENGTH_LONG,
                            TastyToast.WARNING);

                    return;
                }

                if (number.isEmpty()) {
                    TastyToast.makeText(Login.this,
                            "Enter phone number or email",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (isValidEmail(number)){
                    credential_type = "email";
                }else {
                    credential_type = "phone";
                }



                checkLogin(number);

            }
        });

    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target)
                && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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

                    loaderDialog.dismiss();

                    if(status.equals("1") ) {


                        TastyToast.makeText(getApplicationContext(), message,
                                TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        Intent register=new Intent(getApplicationContext(),OtpScreen.class);
                        register.putExtra("phone", number);
                        register.putExtra("type", credential_type);
                        register.putExtra("from","activity_login");

                        register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(register);
                        finish();

                    } else {

                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG,
                                TastyToast.WARNING);
                    }

                    Log.d(TAG,"Token \n" +message);

                }catch (Exception e) {

                    TastyToast.makeText(getApplicationContext(),
                            "DATA NOT FOUND", TastyToast.LENGTH_LONG,
                            TastyToast.WARNING);

                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());

                loaderDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<>();

                params.put("phone_Number", number);
                params.put("device_type", "android");
                params.put("device_id", device_id);
                params.put("fcm_token", globalClass.getFcm_reg_token());
                params.put("type", credential_type);

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(Login.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }


}
