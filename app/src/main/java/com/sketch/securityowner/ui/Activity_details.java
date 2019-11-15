package com.sketch.securityowner.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.sketch.securityowner.model.ActivityChild;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class Activity_details extends AppCompatActivity {

    String TAG="Activity Details";

    RelativeLayout rl_top, rl_call_visitor;
    ImageView img_back, iv_profile, iv_vendor_image, iv_visitor_type;
    TextView tv_name, tv_date_time, tv_status, visitor_type_name, tv_vendor_name, tv_message;
    LinearLayout ll_call_security, ll_generate_passcode, ll_vendors;

    ActivityChild activityChild;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();


    }


    private void initViews(){


        rl_top=findViewById(R.id.rl_top);
        img_back=findViewById(R.id.img_back);
        rl_call_visitor=findViewById(R.id.rl_call_visitor);
        iv_profile=findViewById(R.id.iv_profile);
        iv_vendor_image=findViewById(R.id.iv_vendor_image);
        tv_name=findViewById(R.id.tv_name);
        tv_date_time=findViewById(R.id.tv_date_time);
        tv_status=findViewById(R.id.tv_status);
        visitor_type_name=findViewById(R.id.visitor_type_name);
        tv_vendor_name=findViewById(R.id.tv_vendor_name);
        ll_call_security=findViewById(R.id.ll_call_security);
        ll_generate_passcode=findViewById(R.id.ll_generate_passcode);
        ll_vendors=findViewById(R.id.ll_vendors);
        iv_visitor_type=findViewById(R.id.iv_visitor_type);
        tv_message=findViewById(R.id.tv_message);

        ll_vendors.setVisibility(View.GONE);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            activityChild = (ActivityChild) bundle.getSerializable("info");


            tv_name.setText(activityChild.getName());


            //// status check ...
            if (activityChild.getApprove_status().equals("new")){

                tv_status.setText("New Added");

                showTime(tv_date_time,
                        (activityChild.getVisiting_date() + " "
                                + activityChild.getVisiting_time()));

            }else if (activityChild.getApprove_status().equals("w")){

                tv_status.setText("Waiting for approval");

                showTime(tv_date_time,
                        (activityChild.getVisiting_date() + " "
                                + activityChild.getVisiting_time()));



            }else if (activityChild.getApprove_status().equals("y")){

                tv_status.setText("Approved");

                showTime(tv_date_time, activityChild.getActual_in_time());

                tv_message.setText("Approved by : "
                        +activityChild.getApprove_by());

            }else if (activityChild.getApprove_status().equals("n")){

                tv_status.setText("Rejected");

                showTime(tv_date_time, activityChild.getActual_in_time());


                tv_message.setText("Rejected by : "
                        +activityChild.getApprove_by());

            }else if (activityChild.getApprove_status().equals("l")){

                tv_status.setText("Leave At Gate");

                showTime(tv_date_time, activityChild.getActual_out_time());


                tv_message.setText("Leave at gate code : "
                        +activityChild.getLeave_at_gate_code());

            }



            //// visitor type ...
            if (activityChild.getVisitor_type().equals(AppConfig.guest)){

                visitor_type_name.setText("Guest");

                iv_visitor_type.setImageResource(R.mipmap.guest_white);

            }else  if (activityChild.getVisitor_type().equals(AppConfig.delivery)){

                visitor_type_name.setText("Delivery");

                ll_vendors.setVisibility(View.VISIBLE);

                tv_vendor_name.setText(activityChild.getVendor_name());
                if (!activityChild.getVendor_image().isEmpty()){
                    Picasso.with(Activity_details.this)
                            .load(activityChild.getVendor_image()) // web image url
                            .into(iv_vendor_image);
                }

                iv_visitor_type.setImageResource(R.mipmap.deliveryman_white);

            }else  if (activityChild.getVisitor_type().equals(AppConfig.staff)){

                visitor_type_name.setText("Staff");

                //iv_visitor_type.setImageResource(R.mipmap.guest_white);

            } else  if (activityChild.getVisitor_type().equals(AppConfig.cab)){

                visitor_type_name.setText("Cab");

                ll_vendors.setVisibility(View.VISIBLE);

                tv_vendor_name.setText(activityChild.getVendor_name());
                if (!activityChild.getVendor_image().isEmpty()){
                    Picasso.with(Activity_details.this)
                            .load(activityChild.getVendor_image()) // web image url
                            .into(iv_vendor_image);
                }

                iv_visitor_type.setImageResource(R.mipmap.cab_white);

            }else  if (activityChild.getVisitor_type().equals(AppConfig.visiting_help)){

                visitor_type_name.setText("Visiting Help ("
                        + activityChild.getVisiting_help_cat()+")");

                iv_visitor_type.setImageResource(R.mipmap.visiting_help_white);


            }



            Log.d(TAG, "getpase > "+activityChild.getGetpass());
            Log.d(TAG, "Actual_out_time > "+activityChild.getActual_out_time());

            if (activityChild.getGetpass().equals("0")
                    && ((activityChild.getActual_out_time() == null)
                            || activityChild.getActual_out_time().equals("null")
                            || activityChild.getActual_out_time().equals(null))){

                ll_generate_passcode.setVisibility(View.VISIBLE);

            }else {

                ll_generate_passcode.setVisibility(View.GONE);
            }



            if (!activityChild.getProfile_image().isEmpty()){
                Picasso.with(Activity_details.this)
                        .load(activityChild.getProfile_image()) // web image url
                        .fit().centerInside()
                        .error(R.mipmap.profile_image)
                        .placeholder(R.mipmap.profile_image)
                        .into(iv_profile);
            }
        }


        rl_call_visitor.setOnClickListener(v -> {

            checkCallPermission(activityChild.getMobile());

        });

        ll_call_security.setOnClickListener(v -> {

            checkCallPermission(activityChild.getSecurity_mobile());
        });

        ll_generate_passcode.setOnClickListener(v -> {


        });

        img_back.setOnClickListener(v -> finish());
    }

    private void showTime(TextView textView, String sTime){

        try {

            DateFormat originalFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat =
                    new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);

            Date date = originalFormat.parse(sTime);

            String formattedDate = targetFormat.format(date);

            textView.setText(formattedDate);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //// call user ....

    private static final int REQUEST_PHONE_CALL = 1212;
    private void checkCallPermission(String number){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Activity_details.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Activity_details.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

            } else {
                callPhone(number);
            }
        } else {
            callPhone(number);
        }

    }

    private void callPhone(String number){

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));// Initiates the Intent
        startActivity(intent);

    }


    /// generate passcode ...

    private void generatePasscode() {
        // Tag used to cancel the request

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.getpass_generate, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        }) {



        };

        // Adding request to request queue
        VolleySingleton.getInstance(Activity_details.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

}
