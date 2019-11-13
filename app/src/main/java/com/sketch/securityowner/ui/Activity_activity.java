package com.sketch.securityowner.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Adapter.ActivityListAdapterIN;
import com.sketch.securityowner.Adapter.categoryAdapter;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.sketch.securityowner.model.ActivityChild;
import com.sketch.securityowner.model.ActivityModel;
import com.sketch.securityowner.model.ChildItem;
import com.sketch.securityowner.model.HeaderItem;
import com.sketch.securityowner.model.ListItem;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;


public class Activity_activity extends AppCompatActivity implements
        View.OnClickListener,
        ActivityListAdapterIN.ItemClickListenerIN,
        ActivityListAdapterIN.ItemClickListenerStatusUpdate,
        DatePickerDialog.OnDateSetListener,
       categoryAdapter.onItemClickListner {

   static String TAG="Activity_activity";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycle_activity) RecyclerView recycle_activity;
    @BindView(R.id.recycle_upcoming) RecyclerView recycle_upcoming;
    @BindView(R.id.edit) ImageView edit;
    @BindView(R.id.profile_image) ImageView  profile_image;
    @BindView(R.id.user_name) TextView  user_name;


    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;

    SimpleDateFormat df_show = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
    SimpleDateFormat df_send = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    RecyclerView delivery_recycle,company_name_recycle;
    ProgressDialog progressDialog;
    GlobalClass globalClass;
    ArrayList<String> array1;
    Shared_Preference prefManager;
    RelativeLayout rel_profile,rel_middle_icon;
    EditText tv_others,edit_car_no,edit_parking_no,edit_name,edit_phone,edit_mail,
           edit_name_cab,edit_phone_cab, edit_vehicle_no;
    String type_in_out, response_value = "", approved_by = "";
    AVLoadingIndicatorView avLoadingIndicatorView;
    LinearLayout ll_alram,ll_submit,ll_bell,ll_hide,button_activity,
            car1,rel_upcoming_visitor,rel_all_visitor,ll_community;
    RelativeLayout rl_profile;
    View view_all_visitor,view_upcoming_visitor;
    TextView  tv_animal,tv_medical,tv_thief,tv_threat,tv_lift,
            tv_time,date_picker,tv_upcoming_visitor,tv_all_visitor;
    Button btn_in;
    Dialog dialog;
    String category,help_id,date_to_push;
    ArrayAdapter<String> dataAdapter1;
    Spinner spinner_help;
    CardView animal,fire,threat,lift,medical,theif;
    categoryAdapter CategoryAdapter;
    private int mYear, mMonth, mDay, mHour, mMinute,mSecond;
    ArrayList<HashMap<String,String>> Category;
    ArrayList<HashMap<String,String>> DeliveryList;
    ArrayList<HashMap<String,String>> HelpList;
    TextView tv_id,tv_details_company,close,tv_request_response;
    ImageView img_guest,img_delivery,img_cab,img_help;
    boolean approve_status = false;
    private  boolean button1IsVisible = true;
    RadioButton radio1,radio2;
    RadioGroup radioSex;
    Calendar myCalendar = Calendar.getInstance();

    LinearLayoutManager HorizontalLayout3;
    LinearLayoutManager HorizontalLayout4;
    ArrayList<ActivityModel> activityModelArrayList;
    ArrayList<String> listDates;
    ActivityListAdapterIN activityListAdapter;
    ArrayList<ListItem> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        ButterKnife.bind(this);

        rel_upcoming_visitor =  findViewById(R.id.rel_upcoming_visitor);
        rel_all_visitor =  findViewById(R.id.rel_all_visitor);
        view_upcoming_visitor =  findViewById(R.id.view_upcoming_visitor);
        view_all_visitor =  findViewById(R.id.view_all_visitor);
        tv_upcoming_visitor =  findViewById(R.id.tv_upcoming_visitor);
        tv_all_visitor =  findViewById(R.id.tv_all_visitor);
        avLoadingIndicatorView =  findViewById(R.id.avi);
        button_activity =  findViewById(R.id.button_E1);
        ll_community =  findViewById(R.id.button_E3);
        rel_middle_icon =  findViewById(R.id.rel_middle_icon);
        car1 =  findViewById(R.id.car1);
        rl_profile =  findViewById(R.id.rl_profile);
        img_guest =  findViewById(R.id.img_guest);
        img_delivery =  findViewById(R.id.img_delivery);
        img_cab =  findViewById(R.id.img_cab);
        img_help =  findViewById(R.id.img_help);
        ll_bell =  findViewById(R.id.ll_bell);
        actionViews();
        user_name =  findViewById(R.id.user_name);

        recycle_activity.setVisibility(View.VISIBLE);
        recycle_upcoming.setVisibility(View.GONE);

        user_name.setText(globalClass.getName());

        if (!globalClass.getProfil_pic().isEmpty()){
            Picasso.with(Activity_activity.this)
                    .load(globalClass.getProfil_pic()) // web image url
                    .fit().centerInside()
                    .rotate(90)                    //if you want to rotate by 90 degrees
                    .error(R.mipmap.profile_image)
                    .placeholder(R.mipmap.profile_image)
                    .into(profile_image);
        }

        DeliveryList = new ArrayList<>();
        HelpList=new ArrayList<>();

        Category=new ArrayList<>();
        user_name.setText(globalClass.getName());

        this.registerReceiver(mMessageReceiver, new IntentFilter("activity_screen"));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting=new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(setting);
            }
        });

        img_cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCab();
                car1.setVisibility(View.GONE);

                //  dialog.dismiss();
            }
        });
        ll_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm();
                car1.setVisibility(View.GONE);

                //  dialog.dismiss();
            }
        });
        img_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDelivery();
                car1.setVisibility(View.GONE);


            }
        });
        img_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGuest();
                car1.setVisibility(View.GONE);


            }
        });
        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHelp();
                car1.setVisibility(View.GONE);


            }
        });
        rel_all_visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityList();
                recycle_activity.setVisibility(View.VISIBLE);
                recycle_upcoming.setVisibility(View.GONE);
                tv_upcoming_visitor.setTypeface(null, Typeface.NORMAL); //only text style(only bold)

                tv_all_visitor.setTypeface(null, Typeface.BOLD);
                view_all_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                view_upcoming_visitor.setBackgroundColor(Color.parseColor("#DCDCDC"));    }
        });
        rel_upcoming_visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityListUpcoming();
                recycle_activity.setVisibility(View.GONE);
                recycle_upcoming.setVisibility(View.VISIBLE);
                tv_upcoming_visitor.setTypeface(null, Typeface.BOLD); //only text style(only bold)

                tv_all_visitor.setTypeface(null, Typeface.NORMAL);
                view_upcoming_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                view_all_visitor.setBackgroundColor(Color.parseColor("#DCDCDC"));    }
        });
        button_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),MainActivity.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        ll_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),CommunityActivity.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        rel_middle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                car1.setVisibility(car1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            }
        });
        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDailog();
            }
        });


    }
    public void AddCab(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.cab_dilaog_for_settings);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        close=dialog.findViewById(R.id.close);
        date_picker=dialog.findViewById(R.id.date_picker);
        radio1=dialog.findViewById(R.id.radioMale);
        radio2=dialog.findViewById(R.id.radioFemale);
        radioSex=dialog.findViewById(R.id.radioSex);
        edit_vehicle_no=dialog.findViewById(R.id.edit_vehicle_no);
        edit_name_cab=dialog.findViewById(R.id.edit_name);
        edit_phone_cab=dialog.findViewById(R.id.edit_phone);
        tv_time=dialog.findViewById(R.id.tv_time);
        tv_details_company=dialog.findViewById(R.id.tv_details_company);
        ll_submit=dialog.findViewById(R.id.ll_submit_settigns);
        tv_others=dialog.findViewById(R.id.tv_others);
        company_name_recycle=dialog.findViewById(R.id.company_name_recycle);
        HorizontalLayout3 = new LinearLayoutManager(Activity_activity.this, LinearLayoutManager.HORIZONTAL, false);

        company_name_recycle.setLayoutManager(HorizontalLayout3);
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new android.app.DatePickerDialog(Activity_activity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


            }


        });



        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = dialog. findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(TAG, "AddCab: "+radio_value);
            Toast.makeText(getApplicationContext(),radio_value, Toast.LENGTH_SHORT).show();

        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });


        ll_hide=dialog.findViewById(R.id.ll_hide);
        CategoryList();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        // if button is clicked, close the custom dialog
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio1 = dialog.findViewById(selectedId);
                String radio_value=radio1.getText().toString();
                AddVisitors("cab",radio_value);


            }
        });
        tv_details_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_hide.setVisibility(ll_hide.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                if(button1IsVisible==true) {

                }
            }
        });


        dialog.show();

    }
    private android.app.DatePickerDialog.OnDateSetListener datePickerListener =
            new android.app.DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    myCalendar.set(Calendar.YEAR, selectedYear);
                    myCalendar.set(Calendar.MONTH, selectedMonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    String myFormat = "dd MMMM, yyyy";

                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    //   date_notify_exam = sdf1.format(myCalendar.getTime());
                    String date_to_show = sdf.format(myCalendar.getTime());
                     date_to_push = sdf1.format(myCalendar.getTime());
                    // Log.d(TAG, "date_notify_exam: "+date_notify_exam);
                    date_picker.setText(date_to_show);

                }
            };


    private void timePicker2(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond=c.get(Calendar.SECOND);


        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(Activity_activity.this, R.style.datepicker,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String time1 = selectedHour + ":" + selectedMinute;
                SimpleDateFormat  format1 = new SimpleDateFormat("HH:mm",
                        Locale.ENGLISH);
                SimpleDateFormat  format2 = new SimpleDateFormat("hh:mm:ss a",
                        Locale.ENGLISH);

                try {

                    Date date = format1.parse(time1);

                    tv_time.setText(format2.format(date));

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }, mHour, mMinute,false);

        mTimePicker.show();
    }
    private void AddVisitorHelp(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
       progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "add_visitor: " + response.toString());

                progressDialog.dismiss();
                //  dialog.dismiss();

                Gson gson = new Gson();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {
                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG,
                                TastyToast.SUCCESS);

                        getActivityList();

                    } else {
                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG,
                                TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    TastyToast.makeText(getApplicationContext(),
                            "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_to_push);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
               // params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);

                params.put("visiting_help_cat",help_id);
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }

    private void AddDelivery(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "add_visitor: " + response.toString());

                progressDialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        getActivityList();

                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_to_push);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
                // params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);
                params.put("vendor_name",category);
                params.put("visiting_help_cat","");
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void AddVisitors(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
       progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "add_visitor: " + response.toString());

                progressDialog.dismiss();
                 // dialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {
                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG,
                                TastyToast.SUCCESS);


                        getActivityList();

                    } else {
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
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_to_push);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
                params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);
                params.put("vendor_name",category);
                params.put("visiting_help_cat","");
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void AddGuest(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
       progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "add_visitor " + response.toString());

                progressDialog.dismiss();
                 // dialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {
                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG,
                                TastyToast.SUCCESS);

                        getActivityList();

                    }

                    else {
                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG,
                                TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    TastyToast.makeText(getApplicationContext(),
                            "Some error occurred.\nPlease try again",
                            TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_to_push);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
               // params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);
                params.put("visiting_help_cat","");
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    public void AddDelivery(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delivery);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        delivery_recycle=dialog.findViewById(R.id.delivery_recycle);
        HorizontalLayout4 = new LinearLayoutManager(Activity_activity.this, LinearLayoutManager.HORIZONTAL, false);
        date_picker=dialog.findViewById(R.id.date_picker);
        radio1=dialog.findViewById(R.id.radioMale);
        radio2=dialog.findViewById(R.id.radioFemale);
        radioSex=dialog.findViewById(R.id.radioSex);
        edit_name_cab=dialog.findViewById(R.id.edit_name);
        edit_phone_cab=dialog.findViewById(R.id.edit_phone);
        tv_time=dialog.findViewById(R.id.tv_time);
        delivery_recycle.setLayoutManager(HorizontalLayout4);
        close=dialog.findViewById(R.id.close);
        tv_details_company=dialog.findViewById(R.id.tv_details_company);
        ll_hide=dialog.findViewById(R.id.ll_hide);
        DeliveryList();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new android.app.DatePickerDialog(Activity_activity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


            }


        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });

        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = dialog. findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(TAG, "AddCab: "+radio_value);
            Toast.makeText(getApplicationContext(),radio_value, Toast.LENGTH_SHORT).show();

        });


        LinearLayout ll_submit=dialog.findViewById(R.id.ll_submit);

        // if button is clicked, close the custom dialog
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio1 = dialog.findViewById(selectedId);
                String radio_value=radio1.getText().toString();
                AddDelivery("delivery",radio_value);


                dialog.dismiss();
            }
        });
        tv_details_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_hide.setVisibility(ll_hide.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            }
        });


        dialog.show();

    }

    public void AddGuest(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_guest);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        close=dialog.findViewById(R.id.close);
        // tv_details_company=dialog.findViewById(R.id.tv_details_company);
        ll_hide=dialog.findViewById(R.id.ll_hide);
        date_picker=dialog.findViewById(R.id.date_picker);
        radio1=dialog.findViewById(R.id.radioMale);
        radio2=dialog.findViewById(R.id.radioFemale);
        radioSex=dialog.findViewById(R.id.radioSex);
        edit_name_cab=dialog.findViewById(R.id.edit_name);
        edit_phone_cab=dialog.findViewById(R.id.edit_phone);
        tv_time=dialog.findViewById(R.id.tv_time);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new android.app.DatePickerDialog(Activity_activity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


            }


        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });

        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = dialog. findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(TAG, "AddCab: "+radio_value);
            Toast.makeText(getApplicationContext(),radio_value, Toast.LENGTH_SHORT).show();

        });

        LinearLayout ll_submit=dialog.findViewById(R.id.ll_submit);

        // if button is clicked, close the custom dialog
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio1 = dialog.findViewById(selectedId);

                String radio_value=radio1.getText().toString();
                AddGuest("guest",radio_value);
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public void AddHelp(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_help);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        BrowseCity();
        close=dialog.findViewById(R.id.close);
        date_picker=dialog.findViewById(R.id.date_picker);
        radio1=dialog.findViewById(R.id.radioMale);
        radio2=dialog.findViewById(R.id.radioFemale);
        radioSex=dialog.findViewById(R.id.radioSex);
        edit_name_cab=dialog.findViewById(R.id.edit_name);
        edit_phone_cab=dialog.findViewById(R.id.edit_phone);
        tv_time=dialog.findViewById(R.id.tv_time);
        tv_details_company=dialog.findViewById(R.id.tv_details_company);
        spinner_help=dialog.findViewById(R.id.spinner_help);
        ll_hide=dialog.findViewById(R.id.ll_hide);

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new android.app.DatePickerDialog(Activity_activity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


            }


        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });

        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = dialog. findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(TAG, "AddCab: "+radio_value);
            Toast.makeText(getApplicationContext(),radio_value, Toast.LENGTH_SHORT).show();

        });
        spinner_help.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // Locate the textviews in activity_main.xml
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position !=0){
                    help_id = HelpList.get(position-1).get("name");
                    Log.d(TAG, "onItemSelected: "+help_id);


                    // BrowseComplex(city_id);

                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout ll_submit=dialog.findViewById(R.id.ll_submit);

        // if button is clicked, close the custom dialog
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio1 = dialog.findViewById(selectedId);
                String radio_value=radio1.getText().toString();

                AddVisitorHelp("visiting_help",radio_value);
                dialog.dismiss();
            }
        });
        tv_details_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_hide.setVisibility(ll_hide.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            }
        });


        dialog.show();

    }

    private void BrowseCity() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
        startAnim();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.help_category_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                stopAnim();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        Log.d("jobj", "" + jobj);
                        ArrayList<String> state_array = new ArrayList<String>();
                        state_array.add("Select State");

                        //array.add("Select Location");
                        JsonArray jarray = jobj.getAsJsonArray("data");
                        Log.d("jarray", "" + jarray.toString());
                        array1 = new ArrayList<>();
                        array1.add("Help");
                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            //get the object

                            //JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            String id = jobj1.get("id").toString().replaceAll("\"", "");
                            String name = jobj1.get("name").toString().replaceAll("\"", "");
                            String image = jobj1.get("image").toString().replaceAll("\"", "");

                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("id", id);
                            map_ser.put("name", name);
                            map_ser.put("image", image);


                            HelpList.add(map_ser);

                            array1.add(name);


                        }

                        dataAdapter1 = new ArrayAdapter(Activity_activity.this, R.layout.item_spinner, R.id.tvCust, array1);
                        spinner_help.setAdapter(dataAdapter1);
                        spinner_help.setPrompt("Help");
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
                progressDialog.dismiss();
            }
        }) {



        };

        // Adding request to request queue
        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void FireAlarm(final String category) {
        String tag_string_req = "req_login";
        HelpList.clear();
        // startAnim();
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_panic, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                progressDialog.dismiss();


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
                progressDialog.dismiss();
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
        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }

    public void Alarm(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_alarm);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        close=dialog.findViewById(R.id.close);
        animal=dialog.findViewById(R.id.animal);
        theif=dialog.findViewById(R.id.burglary);
        lift=dialog.findViewById(R.id.lift);
        medical=dialog.findViewById(R.id.medical);
        threat=dialog.findViewById(R.id.threat);
        tv_details_company=dialog.findViewById(R.id.tv_details_company);
        tv_id=dialog.findViewById(R.id.tv_fire);
        tv_lift=dialog.findViewById(R.id.tv_lift);
        tv_threat=dialog.findViewById(R.id.tv_threat);
        tv_thief=dialog.findViewById(R.id.tv_thief);
        tv_medical=dialog.findViewById(R.id.tv_medical);
        tv_animal=dialog.findViewById(R.id.tv_animal);
        threat=dialog.findViewById(R.id.threat);
        ll_alram=dialog.findViewById(R.id.ll_alram);
        ll_hide=dialog.findViewById(R.id.ll_hide);
        fire=dialog.findViewById(R.id.fire);

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_id.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        lift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_lift.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        threat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_threat.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        theif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_thief.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_medical.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_animal.getText().toString();

                FireAlarm(category);
                dialog.dismiss();

            }
        });
/*
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
*/
        //    LinearLayout ll_submit=dialog.findViewById(R.id.ll_submit);

        // if button is clicked, close the custom dialog
/*
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
/*
        tv_details_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true)
                {

                    ll_hide.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false)
                {
                    // car1.animate().alpha(1.0f);
                    ll_hide.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
            }
        });
*/


        dialog.show();

    }
    private void CategoryList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        Category.clear();

        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.company_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                progressDialog.dismiss();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);

                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d(TAG, "Message: "+message);

                    if(status.equals("1") ) {



                        JsonArray product = jobj.getAsJsonArray("data");
                        for (int i = 0; i < product.size(); i++) {
                            JsonObject images1 = product.get(i).getAsJsonObject();
                            String id = images1.get("id").toString().replaceAll("\"", "");
                            String company = images1.get("company").toString().replaceAll("\"", "");
                            String type = images1.get("type").toString().replaceAll("\"", "");
                            String icon = images1.get("icon").toString().replaceAll("\"", "");


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", id);
                            hashMap.put("company", company);
                            hashMap.put("type", type);
                            hashMap.put("icon", icon);

                            Category.add(hashMap);
                            Log.d(TAG, "Hashmap " + hashMap);

                        }

                        CategoryAdapter = new categoryAdapter(Activity_activity.this,
                                Category,Activity_activity.this);
                        company_name_recycle.setAdapter(CategoryAdapter);




                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", "cab");

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void DeliveryList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        DeliveryList.clear();

        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.company_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                progressDialog.dismiss();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);

                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d(TAG, "Message: "+message);

                    if(status.equals("1") ) {



                        JsonArray product = jobj.getAsJsonArray("data");
                        for (int i = 0; i < product.size(); i++) {
                            JsonObject images1 = product.get(i).getAsJsonObject();
                            String id = images1.get("id").toString().replaceAll("\"", "");
                            String company = images1.get("company").toString().replaceAll("\"", "");
                            String type = images1.get("type").toString().replaceAll("\"", "");
                            String icon = images1.get("icon").toString().replaceAll("\"", "");


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", id);
                            hashMap.put("company", company);
                            hashMap.put("type", type);
                            hashMap.put("icon", icon);

                            DeliveryList.add(hashMap);
                            Log.d(TAG, "Hashmap " + hashMap);

                        }

                        CategoryAdapter = new categoryAdapter(Activity_activity.this,
                                DeliveryList,Activity_activity.this);
                        delivery_recycle.setAdapter(CategoryAdapter);




                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", "delivery");

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void actionViews(){

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
       // user_name.setText(globalClass.getName());
        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");

      //  tv_in_list.setBackgroundColor(getResources().getColor(R.color.button_orange));
      //  tv_out_list.setBackgroundColor(getResources().getColor(R.color.light_grey));

        recycle_activity.setLayoutManager(new LinearLayoutManager(this));
        recycle_upcoming.setLayoutManager(new LinearLayoutManager(this));


       // tv_in_list.setOnClickListener(this);
      //  tv_out_list.setOnClickListener(this);
      //  fav.setOnClickListener(this);



        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        String formattedDate = df_show.format(c);

        from_date = df_send.format(c);
        to_date = df_send.format(c);


        activityModelArrayList = new ArrayList<>();
        type_in_out = "in";

        mItems = new ArrayList<>();
        activityListAdapter = new ActivityListAdapterIN(Activity_activity.this,
                mItems);

        getActivityList();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @SuppressLint("NewApi")
    private boolean checkPermission() {

        List<String> permissionsList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(Activity_activity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(Activity_activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(Activity_activity.this,
                Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.CAMERA);
        }

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) Activity_activity.this,
                    permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

            return false;
        }else {


            captureImage();
        }


        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                        (permissions.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                } else {

                    checkPermission();
                }

                break;

        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){


            default:
                break;

        }

    }


    String from_date, to_date;
    Calendar calender_now = Calendar.getInstance();
    private boolean mAutoHighlight = true;
    private void dialogDatePicker(){

        DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                Activity_activity.this,
                calender_now.get(Calendar.YEAR),
                calender_now.get(Calendar.MONTH),
                calender_now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAutoHighlight(mAutoHighlight);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,
                          int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        String sendFromDate = year + "-" +(++monthOfYear) + "-"+dayOfMonth;
        String sendEndDate = yearEnd + "-" +(++monthOfYearEnd) + "-"+dayOfMonthEnd;

        from_date = sendFromDate;
        to_date = sendEndDate;

        Log.d(AppConfig.TAG, "sendFromDate = "+sendFromDate);
        Log.d(AppConfig.TAG, "sendEndDate = "+sendEndDate);

        try {

            Date date1 = df_send.parse(sendFromDate);
            Date date2 = df_send.parse(sendEndDate);


        }catch (Exception e){
            e.printStackTrace();
        }

        getActivityList();

    }


    //// activity api call ....
    public void getActivityList(){
        progressDialog.show();
        activityModelArrayList.clear();

        activityModelArrayList = new ArrayList<>();

        listDates = new ArrayList<>();

        String url = AppConfig.activity_list;

        final Map<String, String> params = new HashMap<>();
        params.put("type", "all");
        params.put("flat_no", globalClass.getFlat_no());
        params.put("complex_id", globalClass.getComplex_id());


        Log.d(AppConfig.TAG , "activity_list- " + url);
        Log.d(AppConfig.TAG , "activity_list- " + params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(AppConfig.TAG , "activity_list- " + response);
                              progressDialog.dismiss();
                        if (response != null){
                            try {

                                ArrayList<ActivityChild> childArrayList;
                                ActivityModel activityModel;

                                JSONObject main_object = new JSONObject(response);

                                int status = main_object.optInt("status");
                                if (status == 1){

                                    if (main_object.has("data_master")) {

                                        JSONArray data_master =
                                                main_object.optJSONArray("data_master");

                                        for (int i = 0; i < data_master.length(); i++){
                                            JSONObject object = data_master.getJSONObject(i);

                                            String visiting_hour = object.optString("visiting_hour");
                                            JSONArray data = object.optJSONArray("data");

                                            listDates.add(visiting_hour);


                                            childArrayList = new ArrayList<>();
                                            for (int j = 0; j < data.length(); j++) {
                                                JSONObject obj = data.getJSONObject(j);


                                                ActivityChild child = new ActivityChild();
                                                child.setType(obj.optString("type"));
                                                child.setActivity_id(obj.optString("activity_id"));
                                                child.setVisitor_id(obj.optString("visitor_id"));
                                                child.setName(obj.optString("name"));
                                                child.setMobile(obj.optString("mobile"));
                                                child.setQr_code(obj.optString("qr_code"));
                                                child.setQr_code_image(obj.optString("qr_code_image"));
                                                child.setActivity_type(obj.optString("activity_type"));
                                                child.setSecurity_id(obj.optString("security_id"));
                                                child.setVisitor_type(obj.optString("visitor_type"));
                                                child.setActual_in_time(obj.optString("actual_in_time"));
                                                child.setActual_out_time(obj.optString("actual_out_time"));
                                                child.setVisiting_time(obj.optString("visiting_time"));
                                                child.setVisiting_date(visiting_hour);
                                                child.setVisiting_help_cat(obj.optString("visiting_help_cat"));
                                                child.setVehicle_no(obj.optString("vehicle_no"));
                                                child.setProfile_image(obj.optString("profile_image"));
                                                child.setVendor_name(obj.optString("vendor_name"));
                                                child.setGetpass(obj.optString("getpass"));
                                                child.setDescription(obj.optString("description"));
                                                child.setGetpass(obj.optString("getpass_image"));
                                                child.setVendor_image(obj.optString("vendor_image"));
                                                child.setApprove_status(obj.optString("approve_status"));
                                                child.setApprove_by(obj.optString("approve_by"));
                                                child.setLeave_at_gate_code(obj.optString("leave_at_gate_code"));
                                                child.setBlock(obj.optString("block"));
                                                child.setFlat_id(obj.optString("flat_id"));
                                                child.setFlat_no(obj.optString("flat_name"));





                                                childArrayList.add(child);

                                            }

                                            activityModel = new ActivityModel();
                                            activityModel.setDate(visiting_hour);
                                            activityModel.setListChild(childArrayList);

                                            activityModelArrayList.add(activityModel);
                                        }


                                    }



                                    if (main_object.has("data_temp")) {

                                        JSONArray data_temp =
                                                main_object.optJSONArray("data_temp");

                                        for (int i = 0; i < data_temp.length(); i++){
                                            JSONObject object = data_temp.getJSONObject(i);

                                            String visiting_hour = object.optString("visiting_hour");
                                            JSONArray data = object.optJSONArray("data");
                                            listDates.add(visiting_hour);

                                            childArrayList = new ArrayList<>();
                                            for (int j = 0; j < data.length(); j++) {
                                                JSONObject obj = data.getJSONObject(j);


                                                ActivityChild child = new ActivityChild();
                                                child.setType(obj.optString("type"));
                                                child.setActivity_id(obj.optString("activity_id"));
                                                child.setUser_id(obj.optString("user_id"));
                                                child.setSecurity_id(obj.optString("security_id"));
                                                child.setName(obj.optString("name"));
                                                child.setMobile(obj.optString("mobile"));
                                                child.setQr_code(obj.optString("qr_code"));
                                                child.setQr_code_image(obj.optString("qr_code_image"));
                                                child.setActivity_type(obj.optString("activity_type"));
                                                child.setVisitor_type(obj.optString("visitor_type"));
                                                child.setVisiting_time(obj.optString("visiting_time"));
                                                child.setVisiting_date(visiting_hour);
                                                child.setVisiting_help_cat(obj.optString("visiting_help_cat"));
                                                child.setVehicle_no(obj.optString("vehicle_no"));
                                                child.setProfile_image(obj.optString("profile_image"));
                                                child.setVendor_name(obj.optString("vendor_name"));
                                                child.setVendor_image(obj.optString("vendor_image"));
                                                child.setApprove_status(obj.optString("approve_status"));
                                                child.setBlock(obj.optString("block"));
                                                child.setFlat_id(obj.optString("flat_id"));
                                                child.setFlat_no(obj.optString("flat_name"));


                                                child.setType_in_out("in");


                                                childArrayList.add(child);

                                            }


                                            activityModel = new ActivityModel();
                                            activityModel.setDate(visiting_hour);
                                            activityModel.setListChild(childArrayList);

                                            activityModelArrayList.add(activityModel);
                                            Log.d(AppConfig.TAG, "onResponse: "+activityModelArrayList.size());

                                        }

                                    }


                                    setData();

                                }

                              stopAnim();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //on error storing the name to sqlite with status unsynced

                        Log.e(AppConfig.TAG, "error - "+error);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;

            }
        };

        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(stringRequest
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }

    public void getActivityListUpcoming(){
        progressDialog.show();
         activityModelArrayList.clear();
        activityModelArrayList = new ArrayList<>();

        listDates = new ArrayList<>();

        String url = AppConfig.activity_list;

        final Map<String, String> params = new HashMap<>();
        params.put("type", "up");
        params.put("flat_no", globalClass.getFlat_no());
        params.put("complex_id", globalClass.getComplex_id());


        Log.d(AppConfig.TAG , "activity_list- " + url);
        Log.d(AppConfig.TAG , "activity_list- " + params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(AppConfig.TAG , "activity_list- " + response);
                            progressDialog.dismiss();
                        if (response != null){
                            try {

                                ArrayList<ActivityChild> childArrayList;
                                ActivityModel activityModel;

                                JSONObject main_object = new JSONObject(response);

                                int status = main_object.optInt("status");
                                if (status == 1){

                                    if (main_object.has("data_master")) {

                                        JSONArray data_master =
                                                main_object.optJSONArray("data_master");

                                        for (int i = 0; i < data_master.length(); i++){
                                            JSONObject object = data_master.getJSONObject(i);

                                            String visiting_hour = object.optString("visiting_hour");
                                            JSONArray data = object.optJSONArray("data");

                                            listDates.add(visiting_hour);


                                            childArrayList = new ArrayList<>();
                                            for (int j = 0; j < data.length(); j++) {
                                                JSONObject obj = data.getJSONObject(j);


                                                ActivityChild child = new ActivityChild();
                                                child.setType(obj.optString("type"));
                                                child.setActivity_id(obj.optString("activity_id"));
                                                child.setVisitor_id(obj.optString("visitor_id"));
                                                child.setName(obj.optString("name"));
                                                child.setMobile(obj.optString("mobile"));
                                                child.setQr_code(obj.optString("qr_code"));
                                                child.setQr_code_image(obj.optString("qr_code_image"));
                                                child.setActivity_type(obj.optString("activity_type"));
                                                child.setSecurity_id(obj.optString("security_id"));
                                                child.setVisitor_type(obj.optString("visitor_type"));
                                                child.setActual_in_time(obj.optString("actual_in_time"));
                                                child.setActual_out_time(obj.optString("actual_out_time"));
                                                child.setVisiting_time(obj.optString("visiting_time"));
                                                child.setVisiting_date(visiting_hour);
                                                child.setVisiting_help_cat(obj.optString("visiting_help_cat"));
                                                child.setVehicle_no(obj.optString("vehicle_no"));
                                                child.setProfile_image(obj.optString("profile_image"));
                                                child.setVendor_name(obj.optString("vendor_name"));
                                                child.setGetpass(obj.optString("getpass"));
                                                child.setDescription(obj.optString("description"));
                                                child.setGetpass(obj.optString("getpass_image"));
                                                child.setVendor_image(obj.optString("vendor_image"));
                                                child.setApprove_status(obj.optString("approve_status"));
                                                child.setApprove_by(obj.optString("approve_by"));





                                                childArrayList.add(child);

                                            }

                                            activityModel = new ActivityModel();
                                            activityModel.setDate(visiting_hour);
                                            activityModel.setListChild(childArrayList);

                                            activityModelArrayList.add(activityModel);
                                        }


                                    }



                                    if (main_object.has("data_temp")) {

                                        JSONArray data_temp =
                                                main_object.optJSONArray("data_temp");

                                        for (int i = 0; i < data_temp.length(); i++){
                                            JSONObject object = data_temp.getJSONObject(i);

                                            String visiting_hour = object.optString("visiting_hour");
                                            JSONArray data = object.optJSONArray("data");
                                            listDates.add(visiting_hour);

                                            childArrayList = new ArrayList<>();
                                            for (int j = 0; j < data.length(); j++) {
                                                JSONObject obj = data.getJSONObject(j);


                                                ActivityChild child = new ActivityChild();
                                                child.setType(obj.optString("type"));
                                                child.setActivity_id(obj.optString("activity_id"));
                                                child.setUser_id(obj.optString("user_id"));
                                                child.setName(obj.optString("name"));
                                                child.setMobile(obj.optString("mobile"));
                                                child.setQr_code(obj.optString("qr_code"));
                                                child.setQr_code_image(obj.optString("qr_code_image"));
                                                child.setActivity_type(obj.optString("activity_type"));
                                                child.setVisitor_type(obj.optString("visitor_type"));
                                                child.setVisiting_time(obj.optString("visiting_time"));
                                                child.setVisiting_date(visiting_hour);
                                                child.setVisiting_help_cat(obj.optString("visiting_help_cat"));
                                                child.setVehicle_no(obj.optString("vehicle_no"));
                                                child.setProfile_image(obj.optString("profile_image"));
                                                child.setVendor_name(obj.optString("vendor_name"));
                                                child.setVendor_image(obj.optString("vendor_image"));



                                                child.setType_in_out("in");


                                                childArrayList.add(child);

                                            }


                                            activityModel = new ActivityModel();
                                            activityModel.setDate(visiting_hour);
                                            activityModel.setListChild(childArrayList);

                                            activityModelArrayList.add(activityModel);
                                            Log.d(AppConfig.TAG, "onResponse: "+activityModelArrayList.size());

                                        }

                                    }


                                    setData();

                                }

                                stopAnim();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //on error storing the name to sqlite with status unsynced

                        Log.e(AppConfig.TAG, "error - "+error);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };

        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(stringRequest
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }

    private void setData(){

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(listDates);
        listDates.clear();
        listDates.addAll(hashSet);

        Collections.sort(listDates, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });


        ArrayList<HashMap<String, ArrayList<ActivityChild>>> listMain = new ArrayList<>();
        for (int i = 0; i < listDates.size(); i++){

            HashMap<String, ArrayList<ActivityChild>> map = new HashMap<>();

            ArrayList<ActivityChild> arrayList2 = getChildArray(listDates.get(i));

            if (arrayList2.size() > 0){
                map.put(listDates.get(i), arrayList2);
                listMain.add(map);
            }

        }

        mItems = new ArrayList<>();
        for (HashMap<String, ArrayList<ActivityChild>> map1 : listMain){

            for (String string : map1.keySet()) {

                HeaderItem header = new HeaderItem();
                header.setHeader(string);
                mItems.add(header);

                for (ActivityChild activityChild : map1.get(string)) {
                    ChildItem item = new ChildItem();
                    item.setActivityChild(activityChild);
                    mItems.add(item);
                }

            }

        }


        activityListAdapter = new ActivityListAdapterIN(Activity_activity.this,
                        mItems);
        recycle_activity.setAdapter(activityListAdapter);
        activityListAdapter.notifyDataSetChanged();

        recycle_upcoming.setAdapter(activityListAdapter);
        activityListAdapter.notifyDataSetChanged();

        activityListAdapter.setClickListenerIN(this);
        activityListAdapter.setClickListenerStatusUpdate(this);


    }


    private ArrayList<ActivityChild> getChildArray(String date){

        ArrayList<ActivityChild> list = new ArrayList<>();

        for (int i = 0; i < activityModelArrayList.size(); i++){

            ActivityModel activityModel = activityModelArrayList.get(i);

            if (date.equals(activityModel.getDate())){

                list.addAll(activityModel.getListChild());

            }

        }

        return list;
    }


    @Override
    public void onItemClickIN(ActivityChild activityChild) {

        Intent activity_details=new Intent(getApplicationContext(),Activity_details.class);
        activity_details.putExtra("images",activityChild.getProfile_image());
        activity_details.putExtra("number",activityChild.getMobile());
        activity_details.putExtra("status",activityChild.getApprove_status());
        activity_details.putExtra("date_time",activityChild.getActual_in_time());
        activity_details.putExtra("type",activityChild.getType());
        Log.d(TAG, "images: "+activityChild.getProfile_image());
        Log.d(TAG, "number: "+activityChild.getMobile());
        Log.d(TAG, "type: "+activityChild.getType());

        startActivity(activity_details);


    }


    ///// image related methods ...
    String mCurrentPhotoPath;
    File p_image;
    private static final int CAMERA_REQUEST = 333;
    private void captureImage(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
          //  dispatchTakePictureIntent();
        } else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        }

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        try {
            Cursor cursor = this.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx); // Exception raised HERE
                cursor.close(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Uri getDestinationUri(){

        String filename = "profile_pic_crop.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        Uri uri = Uri.fromFile(dest);

        return uri;
    }

    private void writeBitmap(Bitmap bitmap){

        final String dir = Environment
                .getExternalStorageDirectory() + "/Shield";

        File file = new File(dir);
        if (!file.exists())
            file.mkdir();


        String files = dir + "/profile_pic" +".jpg";
        File newfile = new File(files);

        try {

            profile_image.setImageBitmap(bitmap);

            newfile.delete();
            OutputStream outFile = null;
            try {

                p_image = newfile;

                outFile = new FileOutputStream(newfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outFile);
                outFile.flush();
                outFile.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    ///////////////////////

    protected void onDestroy() {
        Log.d(AppConfig.TAG, "onDestroy");
        this.unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String type = intent.getStringExtra("type");
            approved_by = intent.getStringExtra("approved_by");
            //do other stuff here
            Log.d(AppConfig.TAG, "Act type = "+type);

            if (type != null && !type.equals("null")){

                response_value = type;

                if (type.equalsIgnoreCase("approved")){

                    approve_status = true;



                }else if (type.equalsIgnoreCase("rejected")){

                    approve_status = true;

                }else if (type.equalsIgnoreCase("visitor added")
                        || type.equalsIgnoreCase("final status")
                        || type.equalsIgnoreCase("visitor out")
                ){


                }


                getActivityList();
            }


        }

    };




    void startAnim(){
        avLoadingIndicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }

    public void ProfileDailog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_settings);

        ImageView profile_image;
        TextView user_name,block,address;
        LinearLayout ll_setting;

        profile_image=dialog.findViewById(R.id.profile_image);
        ll_setting=dialog.findViewById(R.id.ll_setting);
        user_name=dialog.findViewById(R.id.user_name);
        block=dialog.findViewById(R.id.block);
        address=dialog.findViewById(R.id.address);
        Picasso.with(Activity_activity.this)
                .load(globalClass.getProfil_pic()) // web image url
                .fit().centerInside()
                .rotate(90)                    //if you want to rotate by 90 degrees
                .error(R.mipmap.profile_image)
                .placeholder(R.mipmap.profile_image)
                .into(profile_image);
        user_name.setText(globalClass.getName());
        address.setText(globalClass.getComplex_name());
        block.setText(globalClass.getFlat_name()+" "+globalClass.getBlock()+" "+"block");

        // if button is clicked, close the custom dialog
        ll_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent setting=new Intent(Activity_activity.this,SettingActivity.class);
                startActivity(setting);

            }
        });

        dialog.show();

    }
    @Override
    public void onItemClick(String s) {
        category=s;
        if (category.equals("Others")){
            tv_others.setVisibility(View.VISIBLE);
        }
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();


    }


    ///////////////


    @Override
    public void onItemClickStatusUpdate(ActivityChild activityChild, String status) {

        if (activityChild.getUser_id().equals("0")){
            status_updateNewCall(activityChild, status);
        }else {
            status_updateCall(activityChild, status);
        }


    }



    public void status_updateNewCall(ActivityChild activityChild, String status){

        progressDialog.show();

        String url = AppConfig.new_visitor_status_update;

        final Map<String, String> params = new HashMap<>();

        params.put("user_id", globalClass.getId());
        params.put("table", activityChild.getType());
        params.put("activity_id", activityChild.getActivity_id());

        params.put("visitor_id", activityChild.getUser_id());


        params.put("security_id", activityChild.getSecurity_id());
        params.put("complex_id", globalClass.getComplex_id());
        params.put("flat_name", globalClass.getFlat_name());
        params.put("block", activityChild.getBlock());
        params.put("status", status);


        Log.d(AppConfig.TAG , "status_update- " + url);
        Log.d(AppConfig.TAG , "status_update- " + params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(AppConfig.TAG , "visitor_out- " +response);

                        if (response != null){
                            try {

                                JSONObject main_object = new JSONObject(response);

                                int status = main_object.optInt("status");
                                String message = main_object.optString("message");
                                if (status == 1){

                                    TastyToast.makeText(getApplicationContext(),
                                            message,
                                            TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                                    getActivityList();

                                }else {

                                    TastyToast.makeText(getApplicationContext(),
                                            message,
                                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                }

                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //on error storing the name to sqlite with status unsynced

                Log.e(AppConfig.TAG, "error - "+error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };

        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(stringRequest
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }


    public void status_updateCall(ActivityChild activityChild, String status){

        progressDialog.show();

        String url = AppConfig.visitor_status_update;

        final Map<String, String> params = new HashMap<>();

        params.put("user_id", globalClass.getId());
        params.put("table", activityChild.getType());
        params.put("activity_id", activityChild.getActivity_id());

        params.put("visitor_id", activityChild.getUser_id());


        params.put("security_id", activityChild.getSecurity_id());
        params.put("complex_id", globalClass.getComplex_id());
        params.put("flat_name", globalClass.getFlat_name());
        params.put("block", activityChild.getBlock());
        params.put("status", status);


        Log.d(AppConfig.TAG , "status_update- " + url);
        Log.d(AppConfig.TAG , "status_update- " + params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(AppConfig.TAG , "visitor_out- " +response);

                        if (response != null){
                            try {

                                JSONObject main_object = new JSONObject(response);

                                int status = main_object.optInt("status");
                                String message = main_object.optString("message");
                                if (status == 1){

                                    TastyToast.makeText(getApplicationContext(),
                                            message,
                                            TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                                    getActivityList();

                                }else {

                                    TastyToast.makeText(getApplicationContext(),
                                            message,
                                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                }

                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //on error storing the name to sqlite with status unsynced

                Log.e(AppConfig.TAG, "error - "+error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };

        VolleySingleton.getInstance(Activity_activity.this)
                .addToRequestQueue(stringRequest
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }


}
