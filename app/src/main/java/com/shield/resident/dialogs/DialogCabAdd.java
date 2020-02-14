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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Adapter.categoryAdapter;
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

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;


public class DialogCabAdd extends Dialog implements categoryAdapter.onItemClickListner {

    private Context context;
    private GlobalClass globalClass;

    private int mYear, mMonth, mDay, mHour, mMinute,mSecond;
    private String date_to_send, send_time, category;

    private TextView tv_time, date_picker, tv_details_company;
    private RadioButton radio1, radio2;
    private EditText edit_car_no, edit_name_cab, edit_phone_cab, tv_others;
    private RecyclerView delivery_recycle;
    private LinearLayout ll_hide;
    private ImageView user_profile_photo;
    private ProgressBar progressBar;

    LoaderDialog loaderDialog;

    private ArrayList<HashMap<String,String>> DeliveryList;

    private Calendar myCalendar = Calendar.getInstance();

    public DialogCabAdd(@NonNull Context context) {
        super(context, R.style.datepicker);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delivery);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setCancelable(false);

        loaderDialog = new LoaderDialog(context, android.R.style.Theme_Translucent,
                false, "");

        globalClass = (GlobalClass) context.getApplicationContext();


        DeliveryList = new ArrayList<>();

        TextView close = findViewById(R.id.close);
        date_picker = findViewById(R.id.date_picker);
        radio1 = findViewById(R.id.radioMale);
        radio2 = findViewById(R.id.radioFemale);
        edit_car_no = findViewById(R.id.edt_car_no);
        RadioGroup radioSex = findViewById(R.id.radioSex);
        edit_name_cab = findViewById(R.id.edit_name);
        edit_phone_cab = findViewById(R.id.edit_phone);
        tv_time = findViewById(R.id.tv_time);
        tv_others = findViewById(R.id.tv_others);
        user_profile_photo = findViewById(R.id.user_profile_photo);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        user_profile_photo.setImageResource(R.mipmap.taxi);

        delivery_recycle=findViewById(R.id.delivery_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        delivery_recycle.setLayoutManager(layoutManager);

        tv_details_company=findViewById(R.id.tv_details_company);
        ll_hide=findViewById(R.id.ll_hide);
        ll_hide.setVisibility(View.GONE);


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
            String radio_value= (String) radioButton.getText();
            Log.d(AppConfig.TAG, "Guest: "+radio_value);
            Toast.makeText(context,radio_value, Toast.LENGTH_SHORT).show();

        });


        tv_details_company.setText("Driver Details");
        tv_details_company.setOnClickListener(v -> {

            ll_hide.setVisibility(ll_hide.getVisibility()
                    == View.VISIBLE ? View.GONE : View.VISIBLE);

        });

        LinearLayout ll_submit = findViewById(R.id.ll_submit);

        ll_submit.setOnClickListener(v -> {

            int selectedId = radioSex.getCheckedRadioButtonId();

            radio1 = findViewById(selectedId);

            String radio_value = radio1.getText().toString();

            AddCab("cab", radio_value);

        });


        myCalendar = Calendar.getInstance();
        String date_to_show = Config.sdf_show.format(myCalendar.getTime());
        date_picker.setText(date_to_show);
        date_to_send = Config.sdf_send.format(myCalendar.getTime());


        companyList();

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

    private void companyList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        DeliveryList.clear();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.company_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(AppConfig.TAG, "JOB RESPONSE: " + response.toString());
                Gson gson = new Gson();
                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);

                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

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

                        }

                        progressBar.setVisibility(View.GONE);

                        setData();

                    } else {

                        TastyToast.makeText(context, message,
                                TastyToast.LENGTH_LONG,
                                TastyToast.SUCCESS);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(context,
                            "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loaderDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<>();
                params.put("type", "cab");

                Log.d(AppConfig.TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(context)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void setData(){
        categoryAdapter CategoryAdapter = new categoryAdapter(context,
                DeliveryList,this);
        delivery_recycle.setAdapter(CategoryAdapter);
    }

    @Override
    public void onItemClick(String s) {
        category = s;
        if (category.equals("Others")){
            tv_others.setVisibility(View.VISIBLE);
        }
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();

    }


    private void AddCab(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(AppConfig.TAG, "JOB RESPONSE: " + response.toString());
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
                        if(!((qr_code.equals("")) && (qr_code_image.equals("")))){
                            //showDialog(qr_code,qr_code_image);
                        }

                        dismiss();

                    }

                    else {
                        TastyToast.makeText(context, message,
                                TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                    loaderDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(AppConfig.TAG, "DATA NOT FOUND: " + error.getMessage());
                loaderDialog.dismiss();
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
                params.put("vehicle_no",edit_car_no.getText().toString() );
                params.put("frequency",radio_value);
                params.put("vendor_name",category);
                params.put("visiting_help_cat","");
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(AppConfig.TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(context)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }


    private void showDialog(final String qr_code,final String image){


        DialogQrCode dialogQrCode = new DialogQrCode(context,qr_code,image);
        dialogQrCode.show();
        dialogQrCode.setOnDismissListener(dialog -> {

            dismiss();
        });

    }

}
