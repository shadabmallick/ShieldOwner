package com.shield.resident.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

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

        about();

    }


    private void about() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.about, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "about Response: " + response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");

                    if(status.equals("1") ) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        String about = data.optString("about");
                        tv_aboutus.setText(about);

                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "DATA NOT FOUND: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                Log.d("TAG", "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(AboutUs.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

}
