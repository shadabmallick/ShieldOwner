package com.sketch.securityowner.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Adapter.ViewPagerCommunity;
import com.sketch.securityowner.Adapter.categoryAdapter;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class CommunityActivity extends AppCompatActivity implements categoryAdapter.onItemClickListner {
    String TAG="CommunityActivity";
    RecyclerView company_name_recycle,delivery_recycle;
    TabLayout tabLayout;
    ViewPager viewPager;
    private int mYear, mMonth, mDay, mHour, mMinute,mSecond;
    ProgressDialog pd;
    ImageView profile_image,img_cab,img_delivery,img_guest,img_help;

    Spinner spinner_help;
    ArrayAdapter<String> dataAdapter1;
    String date_web,help_id,category;
    GlobalClass globalClass;
    CardView animal,fire,threat,lift,medical,theif;
    categoryAdapter CategoryAdapter;
    ArrayList<String> array1;
    ArrayList<HashMap<String,String>> productDetaiils;
    ArrayList<HashMap<String,String>> Category;
    ArrayList<HashMap<String,String>> DeliveryList;
    ArrayList<HashMap<String,String>> HelpList;
    ArrayList<HashMap<String,String>> productDetaiils_sub;
    ArrayList<HashMap<String,String>> staffList;
    ViewPagerCommunity viewPagerAdapter;
    LinearLayout ll_activity,ll_security,car1;
    RelativeLayout rel_profile,rel_middle_icon;
    private  boolean button1IsVisible = true;
    TextView tv_others, tv_time,date_picker,tv_animal,tv_medical,tv_thief,tv_threat,tv_lift,tv_id,tv_details_company,close,tv_details,app_setting,user_name,user_mobile,user_email;
    RadioGroup radioSex;
    RadioButton radio1,radio2;
    EditText edit_phone_cab,edit_vehicle_no,edit_name_cab,edit_car_no,edit_parking_no,edit_name,edit_phone,edit_mail,edit_family_name,edit_family_phone;
    LinearLayout ll_bell,ll_alram,ll_hide,ll_submit;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerView.LayoutManager RecyclerViewLayoutManager1;
    RecyclerView.LayoutManager RecyclerViewLayoutManager2;
    RecyclerView.LayoutManager RecyclerViewLayoutManager3;
    LinearLayoutManager HorizontalLayout ;
    LinearLayoutManager HorizontalLayout1 ;
    LinearLayoutManager HorizontalLayout2 ;
    LinearLayoutManager HorizontalLayout3 ;
    LinearLayoutManager HorizontalLayout4 ;
    Calendar myCalendar = Calendar.getInstance();
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_class);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        ll_activity=findViewById(R.id.button_E);
        ll_security=findViewById(R.id.button_E1);
        img_cab=  findViewById(R.id.img_cab);
        img_guest=  findViewById(R.id.img_guest);
        img_delivery=  findViewById(R.id.img_delivery);
        img_help=  findViewById(R.id.img_help);
        rel_middle_icon =  findViewById(R.id.rel_middle_icon);
        ll_bell =  findViewById(R.id.ll_bell);
        car1 =  findViewById(R.id.car1);
        globalClass = (GlobalClass) getApplicationContext();

        pd = new ProgressDialog(CommunityActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));
        productDetaiils=new ArrayList<>();
        staffList=new ArrayList<>();
        productDetaiils_sub=new ArrayList<>();
        DeliveryList=new ArrayList<>();
        HelpList=new ArrayList<>();

        Category=new ArrayList<>();



        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerCommunity(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        ll_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),MainActivity.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        ll_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),Activity_activity.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });

        rel_middle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true)
                {

                    car1.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false)
                {
                    // car1.animate().alpha(1.0f);
                    car1.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
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

    }

    public void AddCab(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dailog_cab);
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
        ll_submit=dialog.findViewById(R.id.ll_submit);
        tv_others=dialog.findViewById(R.id.tv_others);
        company_name_recycle=dialog.findViewById(R.id.company_name_recycle);
        HorizontalLayout3 = new LinearLayoutManager(CommunityActivity.this, LinearLayoutManager.HORIZONTAL, false);

        company_name_recycle.setLayoutManager(HorizontalLayout3);
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(CommunityActivity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


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
        ll_submit=dialog.findViewById(R.id.ll_submit);

        // if button is clicked, close the custom dialog
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio1 = dialog.findViewById(selectedId);

                String radio_value=radio1.getText().toString();
                AddVisitors("cab",radio_value);
                dialog.dismiss();


            }
        });
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


        dialog.show();

    }
    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    myCalendar.set(Calendar.YEAR, selectedYear);
                    myCalendar.set(Calendar.MONTH, selectedMonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    String myFormat = "MMM dd, yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    //   date_notify_exam = sdf1.format(myCalendar.getTime());
                    String date_to_show = sdf.format(myCalendar.getTime());
                    date_web = sdf1.format(myCalendar.getTime());
                    // Log.d(TAG, "date_notify_exam: "+date_notify_exam);
                    date_picker.setText(date_to_show);

                }
            };


    private void timePicker2(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CommunityActivity.this,R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                tv_time.setText( ""+selectedHour + ":" + selectedMinute);
            }
        }, mHour, mMinute,true);

        mTimePicker.show();
    }


    public void AddDelivery(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delivery);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        delivery_recycle=dialog.findViewById(R.id.delivery_recycle);
        HorizontalLayout4 = new LinearLayoutManager(CommunityActivity.this, LinearLayoutManager.HORIZONTAL, false);
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

                new DatePickerDialog(CommunityActivity.this, R.style.datepicker,datePickerListener, mYear, mMonth, mDay).show();


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

                new DatePickerDialog(CommunityActivity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


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

                new DatePickerDialog(CommunityActivity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


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


        dialog.show();

    }

    private void AddVisitorHelp(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"add_visitor", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


                        //array.add("Select Location");
                        //  JsonArray jarray = jobj.getAsJsonArray("data");
                        //  Log.d("jarray", "" + jarray.toString());

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
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_web);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
                params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);

                params.put("visiting_help_cat",help_id);
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(CommunityActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void AddDelivery(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"add_visitor", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();
                //  dialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        //array.add("Select Location");
                        //  JsonArray jarray = jobj.getAsJsonArray("data");
                        //  Log.d("jarray", "" + jarray.toString());

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
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_web);
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
        VolleySingleton.getInstance(CommunityActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void AddVisitors(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"add_visitor", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();
             //   dialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


                        //array.add("Select Location");
                        //  JsonArray jarray = jobj.getAsJsonArray("data");
                        //  Log.d("jarray", "" + jarray.toString());

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
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_web);
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
        VolleySingleton.getInstance(CommunityActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void AddGuest(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"add_visitor", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();
              //  dialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        //array.add("Select Location");
                        //  JsonArray jarray = jobj.getAsJsonArray("data");
                        //  Log.d("jarray", "" + jarray.toString());

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
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_web);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
                params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);
                params.put("visiting_help_cat","");
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(CommunityActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void FireAlarm(final String category) {
        String tag_string_req = "req_login";
        // startAnim();
        pd.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"add_panic", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

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
                pd.dismiss();
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
        VolleySingleton.getInstance(CommunityActivity.this)
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

        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"company_list", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();


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

                        CategoryAdapter = new categoryAdapter(CommunityActivity.this,
                                Category,CommunityActivity.this);
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
                pd.dismiss();
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
        VolleySingleton.getInstance(CommunityActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void DeliveryList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        DeliveryList.clear();

        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"company_list", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();


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

                        CategoryAdapter = new categoryAdapter(CommunityActivity.this,
                                DeliveryList,CommunityActivity.this);
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
                pd.dismiss();
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
        VolleySingleton.getInstance(CommunityActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void BrowseCity() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();


        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_DEV+"help_category_list", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());




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

                        dataAdapter1 = new ArrayAdapter(CommunityActivity.this, R.layout.item_spinner, R.id.tvCust, array1);
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
                pd.dismiss();
            }
        }) {



        };

        // Adding request to request queue
        VolleySingleton.getInstance(CommunityActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    @Override
    public void onItemClick(String s) {
        category=s;
        if (category.equals("Others")){
            tv_others.setVisibility(View.VISIBLE);
        }
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();


    }

}
