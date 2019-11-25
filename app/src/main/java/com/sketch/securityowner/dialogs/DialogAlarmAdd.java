package com.sketch.securityowner.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;


public class DialogAlarmAdd extends Dialog {

    private Context context;
    private GlobalClass globalClass;
    private CardView animal,fire,threat,lift,medical,theif;

    private TextView tv_animal,tv_medical,tv_thief,tv_threat, tv_lift,tv_id;

    LoaderDialog loaderDialog;


    public DialogAlarmAdd(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_alarm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        loaderDialog = new LoaderDialog(context, android.R.style.Theme_Translucent,
                false, "");

        globalClass = (GlobalClass) context.getApplicationContext();


        animal=findViewById(R.id.animal);
        theif=findViewById(R.id.burglary);
        lift=findViewById(R.id.lift);
        medical=findViewById(R.id.medical);
        threat=findViewById(R.id.threat);
        tv_id=findViewById(R.id.tv_fire);
        tv_lift=findViewById(R.id.tv_lift);
        tv_threat=findViewById(R.id.tv_threat);
        tv_thief=findViewById(R.id.tv_thief);
        tv_medical=findViewById(R.id.tv_medical);
        tv_animal=findViewById(R.id.tv_animal);
        threat=findViewById(R.id.threat);

        fire=findViewById(R.id.fire);



        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_id.getText().toString();
                FireAlarm(category);
            }
        });
        lift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_lift.getText().toString();
                FireAlarm(category);
            }
        });
        threat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_threat.getText().toString();
                FireAlarm(category);
            }
        });
        theif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_thief.getText().toString();
                FireAlarm(category);
            }
        });
        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category=tv_medical.getText().toString();
                FireAlarm(category);
            }
        });
        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category=tv_animal.getText().toString();
                FireAlarm(category);

            }
        });




    }





    private void FireAlarm(final String category) {
        String tag_string_req = "req_login";

        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_panic, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                loaderDialog.dismiss();

                Gson gson = new Gson();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {
                        TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        dismiss();

                    }
                    else {
                        TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(context, "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("user_id", globalClass.getId());
                params.put("category", category);
                params.put("complex_id", globalClass.getComplex_id());
                params.put("flat_id", globalClass.getFlat_no());

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(context)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }



}
