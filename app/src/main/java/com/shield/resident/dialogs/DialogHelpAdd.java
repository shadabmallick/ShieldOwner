package com.shield.resident.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.Config;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;


public class DialogHelpAdd extends Dialog {

    private Context context;
    private GlobalClass globalClass;
    RadioGroup radioSex;
    private int mYear, mMonth, mDay, mHour, mMinute,mSecond;
    private String date_to_send, send_time,help_id;
    ArrayList<HashMap<String,String>> HelpList;
    private TextView close,tv_details_company,tv_time, date_picker, tv_get_contact;
    private RadioButton radio1, radio2;
    private EditText edit_name_cab, edit_phone_cab;
    ArrayList<String> array1;
    LinearLayout ll_hide;
    private  boolean button1IsVisible = true;
    private Calendar myCalendar = Calendar.getInstance();
    Spinner spinner_help;

    LoaderDialog loaderDialog;


    public DialogHelpAdd(@NonNull Context context) {
        super(context, R.style.datepicker);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_help);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setCancelable(false);
        HelpList=new ArrayList<>();
        globalClass = (GlobalClass) context.getApplicationContext();

        loaderDialog = new LoaderDialog(context, android.R.style.Theme_Translucent,
                false, "");


        close = findViewById(R.id.close);
        ll_hide = findViewById(R.id.ll_hide);

        close=findViewById(R.id.close);
        date_picker=findViewById(R.id.date_picker);
        radio1=findViewById(R.id.radioMale);
        radio2=findViewById(R.id.radioFemale);
        radioSex=findViewById(R.id.radioSex);
        edit_name_cab=findViewById(R.id.edit_name);
        edit_phone_cab=findViewById(R.id.edit_phone);
        tv_time=findViewById(R.id.tv_time);
        tv_details_company=findViewById(R.id.tv_details_company);
        spinner_help=findViewById(R.id.spinner_help);
        ll_hide=findViewById(R.id.ll_hide);
        tv_get_contact=findViewById(R.id.tv_get_contact);

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(context,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


            }


        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });

        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton =  findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(TAG, "AddCab: "+radio_value);
            Toast.makeText(context,radio_value, Toast.LENGTH_SHORT).show();

        });
        spinner_help.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int position, long arg3) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position !=0){

                    help_id = HelpList.get(position-1).get("name");
                    Log.d(TAG, "onItemSelected: "+help_id);

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
              dismiss();
            }
        });


        LinearLayout ll_submit=findViewById(R.id.ll_submit);

        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSex.getCheckedRadioButtonId();

                radio1 = findViewById(selectedId);

                String radio_value=radio1.getText().toString();

                AddVisitorHelp("visiting_help",radio_value);
            }
        });




        tv_details_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true) {

                    ll_hide.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false) {

                    ll_hide.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(context,R.style.datepicker,
                        datePickerListener, mYear, mMonth, mDay).show();

            }

        });


        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });


        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(AppConfig.TAG, "Guest: "+radio_value);
            Toast.makeText(context,radio_value, Toast.LENGTH_SHORT).show();

        });



        myCalendar = Calendar.getInstance();
        String date_to_show = Config.sdf_show.format(myCalendar.getTime());
        date_picker.setText(date_to_show);
        date_to_send = Config.sdf_send.format(myCalendar.getTime());


        tv_get_contact.setOnClickListener(v -> {



        });


        visitingHelpCat();


    }

    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    myCalendar.set(Calendar.YEAR, selectedYear);
                    myCalendar.set(Calendar.MONTH, selectedMonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                    String date_to_show = Config.sdf_show.format(myCalendar.getTime());

                    date_to_send = Config.sdf_send.format(myCalendar.getTime());

                    date_picker.setText(date_to_show);

                }
            };


    private void timePicker2(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context,R.style.datepicker,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour,
                                          int selectedMinute) {

                        String time1 = selectedHour + ":" + selectedMinute;
                        SimpleDateFormat  format1 = new SimpleDateFormat("HH:mm",
                                Locale.ENGLISH);
                        SimpleDateFormat  format2 = new SimpleDateFormat("HH:mm:ss",
                                Locale.ENGLISH);

                        SimpleDateFormat  format3 = new SimpleDateFormat("hh:mm a",
                                Locale.ENGLISH);
                        try {

                            Date date1 = format1.parse(time1);
                            send_time = format2.format(date1);


                            Date date = format1.parse(time1);
                            tv_time.setText(format3.format(date));

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, mHour, mMinute,true);

        mTimePicker.show();
    }

    private void visitingHelpCat() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();

        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.help_category_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                Gson gson = new Gson();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        JsonArray jarray = jobj.getAsJsonArray("data");

                        array1 = new ArrayList<>();
                        array1.add("Select Visiting Help");

                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobj1 = jarray.get(i).getAsJsonObject();

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

                        ArrayAdapter dataAdapter1 = new ArrayAdapter(context,
                                R.layout.help_spinner, R.id.tvCust, array1);
                        spinner_help.setAdapter(dataAdapter1);
                        spinner_help.setPrompt("Visiting Help:");
                    }
                    else {
                        TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                    loaderDialog.dismiss();

                } catch (Exception e) {
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

                params.put("complex_id", globalClass.getComplex_id());

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

    private void AddVisitorHelp(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        loaderDialog.show();
        HelpList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                Gson gson = new Gson();
                loaderDialog.dismiss();
                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");
                    String qr_code = jobj.get("qr_code").getAsString().replaceAll("\"", "");
                    String qr_code_image = jobj.get("qr_code_image").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        TastyToast.makeText(context, message,
                                TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        dismiss();

                    }

                    else {
                        TastyToast.makeText(context, message, TastyToast.LENGTH_LONG,
                                TastyToast.WARNING);

                    }

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", send_time);
                params.put("date", date_to_send);
                params.put("flat_no", globalClass.getFlat_id());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
                params.put("frequency",radio_value);
                params.put("visiting_help_cat",help_id);
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

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
