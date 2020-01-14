package com.shield.resident.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Adapter.ActivityListAdapterIN;
import com.shield.resident.Adapter.categoryAdapter;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.DialogAlarmAdd;
import com.shield.resident.dialogs.DialogCabAdd;
import com.shield.resident.dialogs.DialogDeliveryAdd;
import com.shield.resident.dialogs.DialogGuestActivityAdd;
import com.shield.resident.dialogs.DialogHelpActivityAdd;
import com.shield.resident.dialogs.DialogProfile;
import com.shield.resident.dialogs.LoaderDialog;
import com.shield.resident.model.ActivityChild;
import com.shield.resident.model.ActivityModel;
import com.shield.resident.model.ChildItem;
import com.shield.resident.model.HeaderItem;
import com.shield.resident.model.ListItem;
import com.shield.resident.util.DigitTextView;
import com.shield.resident.util.OnSwipeTouchListener;

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

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;


public class Activity_activity extends AppCompatActivity implements
        View.OnClickListener,
        ActivityListAdapterIN.ItemClickListenerIN,
        ActivityListAdapterIN.ItemClickListenerStatusUpdate,
        DatePickerDialog.OnDateSetListener,
        categoryAdapter.onItemClickListner,
        SwipeRefreshLayout.OnRefreshListener{

   static String TAG = "Activity_activity";
    @BindView(R.id.recycle_activity) RecyclerView recycle_activity;
    @BindView(R.id.recycle_upcoming) RecyclerView recycle_upcoming;

    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;

    SimpleDateFormat df_show = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
    SimpleDateFormat df_send = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    GlobalClass globalClass;

    Shared_Preference prefManager;
    RelativeLayout rel_middle_icon;
    EditText tv_others;
    String type_in_out, response_value = "", approved_by = "";
    LinearLayout ll_bell,button_activity,
            view_add_visitor,rel_upcoming_visitor,rel_all_visitor,ll_community,
            ll_app_help, linear_nodata;
    RelativeLayout rl_profile;
    View view_all_visitor,view_upcoming_visitor;
    TextView tv_upcoming_visitor,tv_all_visitor,tv_flat_name;
    DigitTextView tv_digit_textview;
    SwipeRefreshLayout swipe_refresh;

    String category, date_to_push;
    EditText edt_search;
    ArrayList<HashMap<String,String>> Category;
    ArrayList<HashMap<String,String>> DeliveryList;
    ArrayList<HashMap<String,String>> HelpList;
    TextView tv_id,tv_details_company,close;
    ImageView img_guest,img_delivery,img_cab,img_help;
    boolean approve_status = false;
    Calendar myCalendar = Calendar.getInstance();
    ArrayList<ActivityModel> activityModelArrayList;
    ArrayList<String> listDates;
    ActivityListAdapterIN activityListAdapter;
    ArrayList<ListItem> mItems;

    String all_upcoming;

    LoaderDialog loaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        ButterKnife.bind(this);

        actionViews();

        inAppUpdate();

    }


    private void actionViews(){

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);


        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");


        rel_upcoming_visitor =  findViewById(R.id.rel_upcoming_visitor);
        rel_all_visitor =  findViewById(R.id.rel_all_visitor);
        view_upcoming_visitor =  findViewById(R.id.view_upcoming_visitor);
        view_all_visitor =  findViewById(R.id.view_all_visitor);
        tv_upcoming_visitor =  findViewById(R.id.tv_upcoming_visitor);
        tv_all_visitor =  findViewById(R.id.tv_all_visitor);
        button_activity =  findViewById(R.id.button_E1);
        ll_community =  findViewById(R.id.button_E3);
        rel_middle_icon =  findViewById(R.id.rel_middle_icon);
        view_add_visitor =  findViewById(R.id.view_add_visitor);
        rl_profile =  findViewById(R.id.rl_profile);
        img_guest =  findViewById(R.id.img_guest);
        img_delivery =  findViewById(R.id.img_delivery);
        img_cab =  findViewById(R.id.img_cab);
        img_help =  findViewById(R.id.img_help);
        ll_bell =  findViewById(R.id.ll_bell);
        tv_flat_name =  findViewById(R.id.tv_flat_name);
        edt_search =  findViewById(R.id.edt_search);
        tv_digit_textview =  findViewById(R.id.tv_digit_textview);
        ll_app_help =  findViewById(R.id.button_E4);
        linear_nodata = findViewById(R.id.linear_nodata);
        linear_nodata.setVisibility(View.GONE);

        swipe_refresh = findViewById(R.id.swipe_refresh);

        recycle_activity.setVisibility(View.VISIBLE);
        recycle_upcoming.setVisibility(View.GONE);

        view_all_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.range_white));
        view_upcoming_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));

        DeliveryList = new ArrayList<>();
        HelpList=new ArrayList<>();

        Category=new ArrayList<>();

        all_upcoming = "all";

        this.registerReceiver(mMessageReceiver, new IntentFilter("activity_screen"));


        img_cab.setOnClickListener(v -> {

            view_add_visitor.setVisibility(View.GONE);

            DialogCabAdd dialogCabAdd = new DialogCabAdd(Activity_activity.this);
            dialogCabAdd.show();

            dialogCabAdd.setOnDismissListener(dialog1 -> {
                forAllVisitor();
            });

        });

        img_delivery.setOnClickListener(v -> {

            view_add_visitor.setVisibility(View.GONE);

            DialogDeliveryAdd dialogDeliveryAdd = new DialogDeliveryAdd(Activity_activity.this);
            dialogDeliveryAdd.show();

            dialogDeliveryAdd.setOnDismissListener(dialog1 -> {
                forAllVisitor();
            });

        });

        img_guest.setOnClickListener(v -> {
            view_add_visitor.setVisibility(View.GONE);

            Intent intent = new Intent(Activity_activity.this,
                    DialogGuestActivityAdd.class);
            startActivity(intent);


        });

        img_help.setOnClickListener(v -> {
            view_add_visitor.setVisibility(View.GONE);

            Intent intent = new Intent(Activity_activity.this,
                    DialogHelpActivityAdd.class);
            startActivity(intent);


        });

        ll_bell.setOnClickListener(v -> {

            view_add_visitor.setVisibility(View.GONE);

            DialogAlarmAdd dialogAlarmAdd = new DialogAlarmAdd(Activity_activity.this);
            dialogAlarmAdd.show();
        });



        rel_all_visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_upcoming = "all";
                forAllVisitor();
            }
        });
        rel_upcoming_visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_upcoming = "up";
                forUpcomingVisitor();
            }
        });
        button_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(Activity_activity.this, SecurityScreen.class);
                startActivity(notification);
            }
        });
        ll_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(Activity_activity.this,CommunityActivity.class);
                startActivity(notification);
            }
        });
        ll_app_help.setOnClickListener(v -> {
            Intent notification=new Intent(Activity_activity.this, AppHelp.class);
            startActivity(notification);
        });

        rel_middle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (globalClass.getIs_tenant().equals("no")){
                    view_add_visitor.setVisibility(view_add_visitor.getVisibility()
                            == View.VISIBLE ? View.GONE : View.VISIBLE);
                }else {
                    TastyToast.makeText(getApplicationContext(),
                            "You shifted this features to your tenant",
                            TastyToast.LENGTH_LONG, TastyToast.INFO);
                }

            }
        });

        recycle_activity.setLayoutManager(new LinearLayoutManager(this));
        recycle_upcoming.setLayoutManager(new LinearLayoutManager(this));

       // tv_flat_name.setText(globalClass.getFlat_name());

        tv_digit_textview.setValue(globalClass.getFlat_name(), "up");


        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        String formattedDate = df_show.format(c);
        from_date = df_send.format(c);
        to_date = df_send.format(c);


        activityModelArrayList = new ArrayList<>();
        type_in_out = "in";

        mItems = new ArrayList<>();
        activityListAdapter = new ActivityListAdapterIN(Activity_activity.this,
                mItems);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 0){

                    setData(s.toString());

                }else {

                    setData("");
                }

            }
        });

        swipe_refresh.setOnRefreshListener(this);

        setSwipeAction();

        tabViews();

    }

    private void tabViews(){

        ImageView img_temp = findViewById(R.id.img_temp);
        ImageView img_temp1 = findViewById(R.id.img_temp1);
        ImageView img_temp3 = findViewById(R.id.img_temp3);
        ImageView img_temp4 = findViewById(R.id.img_temp4);

        TextView tv = findViewById(R.id.tv);
        TextView tv1 = findViewById(R.id.tv1);
        TextView tv3 = findViewById(R.id.tv3);
        TextView tv4 = findViewById(R.id.tv4);


        img_temp.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv.setTextColor(getResources().getColor(R.color.blue));

    }

    private void forAllVisitor(){

        getActivityList("all");
        recycle_activity.setVisibility(View.VISIBLE);
        recycle_upcoming.setVisibility(View.GONE);
        linear_nodata.setVisibility(View.GONE);
        tv_upcoming_visitor.setTypeface(null, Typeface.NORMAL); //only text style(only bold)
        tv_all_visitor.setTypeface(null, Typeface.BOLD);

        view_all_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        view_upcoming_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));

    }

    private void forUpcomingVisitor(){

        getActivityList("up");
        recycle_activity.setVisibility(View.GONE);
        linear_nodata.setVisibility(View.GONE);
        recycle_upcoming.setVisibility(View.VISIBLE);
        tv_upcoming_visitor.setTypeface(null, Typeface.BOLD); //only text style(only bold)
        tv_all_visitor.setTypeface(null, Typeface.NORMAL);

        view_all_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        view_upcoming_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
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

        getActivityList("all");

    }


    //// activity api call ....
    public void getActivityList(String from){
        loaderDialog.show();

        activityModelArrayList.clear();
        activityModelArrayList = new ArrayList<>();

        listDates = new ArrayList<>();

        String url = AppConfig.activity_list;

        final Map<String, String> params = new HashMap<>();
        params.put("type", from);
        params.put("flat_no", globalClass.getFlat_id());
        params.put("complex_id", globalClass.getComplex_id());
        params.put("user_id", globalClass.getId());


        Log.d(AppConfig.TAG , "activity_list- " + url);
        Log.d(AppConfig.TAG , "activity_list- " + params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(AppConfig.TAG , "activity_list- " + response);
                        loaderDialog.dismiss();
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
                                                child.setUser_id(obj.optString("user_id"));
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
                                                child.setGetpass_image(obj.optString("getpass_image"));
                                                child.setVendor_image(obj.optString("vendor_image"));
                                                child.setApprove_status(obj.optString("approve_status"));
                                                child.setApprove_by(obj.optString("approve_by"));
                                                child.setSecurity_mobile(obj.optString("security_mobile"));
                                                child.setLeave_at_gate_code(obj.optString("leave_at_gate_code"));
                                                child.setBlock(obj.optString("block"));
                                                child.setFlat_id(obj.optString("flat_id"));
                                                child.setFlat_no(obj.optString("flat_name"));
                                                child.setNote(obj.optString("note"));


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



                                    setData("");

                                }

                              loaderDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            swipe_refresh.setRefreshing(false);
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

    private void setData(String query){

        try {

            if (activityModelArrayList.size() == 0){
                linear_nodata.setVisibility(View.VISIBLE);
            }else {
                linear_nodata.setVisibility(View.GONE);
            }

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

            Collections.reverse(listDates);


            ArrayList<HashMap<String, ArrayList<ActivityChild>>> listMain = new ArrayList<>();
            for (int i = 0; i < listDates.size(); i++){

                HashMap<String, ArrayList<ActivityChild>> map = new HashMap<>();

                ArrayList<ActivityChild> arrayList2 = getChildArray(listDates.get(i), query);

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


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private ArrayList<ActivityChild> getChildArray(String date, String query){

        ArrayList<ActivityChild> list = new ArrayList<>();

        for (int i = 0; i < activityModelArrayList.size(); i++){

            ActivityModel activityModel = activityModelArrayList.get(i);

            if (date.equals(activityModel.getDate())){

                if (query.isEmpty()){
                    list.addAll(activityModel.getListChild());
                }else {

                    for (ActivityChild activityChild : activityModel.getListChild()){

                        if (activityChild.getName().toLowerCase().contains(query.toLowerCase())
                                || activityChild.getMobile().toLowerCase().contains(query.toLowerCase())
                                || activityChild.getVisitor_type().toLowerCase().contains(query.toLowerCase())

                        ) {

                            list.add(activityChild);
                        }

                    }

                }

            }

        }

        return list;
    }


    @Override
    public void onItemClickIN(ActivityChild activityChild) {

        Intent activity_details = new Intent(Activity_activity.this,
                Activity_details.class);
        activity_details.putExtra("info", activityChild);
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


    @Override
    protected void onResume() {
        forAllVisitor();

        getFlatListOwnerWise();

        super.onResume();
    }

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

            try {

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


                    getActivityList("all");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    };


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

        try {

            if (activityChild.getUser_id().equals("0")){
                status_updateNewCall(activityChild, status);
            }else {
                status_updateCall(activityChild, status);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void status_updateNewCall(ActivityChild activityChild, String status){

        loaderDialog.show();

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

                                    getActivityList("all");

                                }else {

                                    TastyToast.makeText(getApplicationContext(),
                                            message,
                                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                }

                                loaderDialog.dismiss();

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

        loaderDialog.show();

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

                                    getActivityList("all");

                                }else {

                                    TastyToast.makeText(getApplicationContext(),
                                            message,
                                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                }

                                loaderDialog.dismiss();

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



    //// on swipe ...
    DialogProfile dialogProfile;
    private void setSwipeAction(){

        listOwnerFlat = new ArrayList<>();

        tv_digit_textview.setOnClickListener(v -> {

            dialogProfile = new DialogProfile(Activity_activity.this, listOwnerFlat);

            if (!dialogProfile.isShowing()){

                dialogProfile.show();

                dialogProfile.setOnDismissListener(dialog -> {

                    if (dialogProfile.is_clicked){

                        tv_digit_textview.setValue(globalClass.getFlat_name(), "up");

                        TastyToast.makeText(getApplicationContext(),
                                "You are now flat "+globalClass.getFlat_name(),
                                TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);

                        getActivityList("all");
                    }

                });
            }


        });

        /*rl_profile.setOnClickListener(v -> {

            dialogProfile = new DialogProfile(Activity_activity.this, listOwnerFlat);

            if (!dialogProfile.isShowing()){

                dialogProfile.show();

                dialogProfile.setOnDismissListener(dialog -> {

                    if (dialogProfile.is_clicked){

                        tv_digit_textview.setValue(globalClass.getFlat_name(), "up");

                        TastyToast.makeText(getApplicationContext(),
                                "You are now flat "+globalClass.getFlat_name(),
                                TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);

                        getActivityList("all");
                    }

                });
            }

        });*/



        rl_profile.setOnTouchListener(new OnSwipeTouchListener(
                Activity_activity.this) {
            public void onSwipeTop() {
               // Toast.makeText(Activity_activity.this, "top", Toast.LENGTH_SHORT).show();
                swipeUp();
            }
            public void onSwipeRight() {
                //Toast.makeText(Activity_activity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
               // Toast.makeText(Activity_activity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
               // Toast.makeText(Activity_activity.this, "bottom", Toast.LENGTH_SHORT).show();
                swipeDown();
            }

        });


        RelativeLayout rl_profile22 = findViewById(R.id.rl_profile22);
        rl_profile22.setOnTouchListener(new OnSwipeTouchListener(
                Activity_activity.this) {
            public void onSwipeTop() {
                // Toast.makeText(Activity_activity.this, "top", Toast.LENGTH_SHORT).show();
                swipeUp();
            }
            public void onSwipeRight() {
                //Toast.makeText(Activity_activity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                // Toast.makeText(Activity_activity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                // Toast.makeText(Activity_activity.this, "bottom", Toast.LENGTH_SHORT).show();
                swipeDown();
            }

        });


        getFlatListOwnerWise();
    }


    private void swipeUp(){
        int past_position = 0;
        for (int i = 0; i < listOwnerFlat.size(); i++){
            HashMap<String, String> hashMap = listOwnerFlat.get(i);
            if (globalClass.getFlat_id().equals(hashMap.get("flat_id"))){
                past_position = i;
            }
        }

        try {

            past_position++;

            HashMap<String, String> hashMap = listOwnerFlat.get(past_position);
            globalClass.setFlat_id(hashMap.get("flat_id"));
            globalClass.setFlat_name(hashMap.get("flat_no"));

            globalClass.setUser_type(hashMap.get("user_type"));
            globalClass.setComplex_id(hashMap.get("complex_id"));
            globalClass.setComplex_name(hashMap.get("complex_name"));
            globalClass.setBlock(hashMap.get("block"));
            globalClass.setIs_tenant(hashMap.get("tenant"));
            globalClass.setPayment_system(hashMap.get("payment_system"));


            prefManager.savePrefrence();
            prefManager.loadPrefrence();


            tv_digit_textview.setValue(globalClass.getFlat_name(), "up");

            TastyToast.makeText(getApplicationContext(),
                    "You are now flat "+globalClass.getFlat_name(),
                    TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
            getActivityList("all");


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void swipeDown(){
        int past_position = 0;
        for (int i = 0; i < listOwnerFlat.size(); i++){
            HashMap<String, String> hashMap = listOwnerFlat.get(i);
            if (globalClass.getFlat_id().equals(hashMap.get("flat_id"))){
                past_position = i;
            }
        }

        try {

            past_position--;

            HashMap<String, String> hashMap = listOwnerFlat.get(past_position);
            globalClass.setFlat_id(hashMap.get("flat_id"));
            globalClass.setFlat_name(hashMap.get("flat_no"));

            globalClass.setUser_type(hashMap.get("user_type"));
            globalClass.setComplex_id(hashMap.get("complex_id"));
            globalClass.setComplex_name(hashMap.get("complex_name"));
            globalClass.setBlock(hashMap.get("block"));
            globalClass.setIs_tenant(hashMap.get("tenant"));
            globalClass.setPayment_system(hashMap.get("payment_system"));


            prefManager.savePrefrence();
            prefManager.loadPrefrence();


            tv_digit_textview.setValue(globalClass.getFlat_name(), "down");

            TastyToast.makeText(getApplicationContext(),
                    "You are now flat "+globalClass.getFlat_name(),
                    TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);

            getActivityList("all");

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    ArrayList<HashMap<String, String>> listOwnerFlat;
    private void getFlatListOwnerWise() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        listOwnerFlat = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.user_complex_flat_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "flat_list: " + response.toString());

                try {

                    JSONObject object = new JSONObject(response);

                    String status = object.optString("status");
                    String message = object.optString("message");

                    if(status.equals("1")) {

                        JSONObject data = object.getJSONObject("data");

                        JSONArray flat_details = data.getJSONArray("flat_details");

                        for (int j = 0; j < flat_details.length(); j++) {
                            JSONObject flat = flat_details.getJSONObject(j);

                            String complex_id = flat.optString("complex_id");
                            String user_type = flat.optString("user_type");
                            String complex_name = flat.optString("complex_name");
                            String flat_id = flat.optString("flat_id");
                            String flat_no = flat.optString("flat_no");
                            String block = flat.optString("block");
                            String floor = flat.optString("floor");
                            String tenant = flat.optString("tenant");
                            String payment_system = flat.optString("payment_system");

                            HashMap<String, String> map_ser = new HashMap<>();

                            map_ser.put("complex_id", complex_id);

                            map_ser.put("complex_name", complex_name);
                            map_ser.put("flat_id", flat_id);
                            map_ser.put("flat_no", flat_no);
                            map_ser.put("block", block);
                            map_ser.put("floor", floor);
                            map_ser.put("tenant", tenant);
                            map_ser.put("payment_system", payment_system);


                            // 1 = owner, 4 = member, 6 = tenant


                            if (user_type.equals("1")){
                                map_ser.put("user_type", "owner");
                            }else if (user_type.equals("4")){
                                map_ser.put("user_type", "member");
                            }else if (user_type.equals("6")){
                                map_ser.put("user_type", "tenant");
                            }

                            if (globalClass.getFlat_id().equals(flat_id)){

                                globalClass.setFlat_id(map_ser.get("flat_id"));
                                globalClass.setFlat_name(map_ser.get("flat_no"));

                                globalClass.setUser_type(map_ser.get("user_type"));
                                globalClass.setComplex_id(map_ser.get("complex_id"));
                                globalClass.setComplex_name(map_ser.get("complex_name"));
                                globalClass.setBlock(map_ser.get("block"));
                                globalClass.setIs_tenant(map_ser.get("tenant"));
                                globalClass.setPayment_system(map_ser.get("payment_system"));

                                prefManager.savePrefrence();
                                prefManager.loadPrefrence();

                            }


                            listOwnerFlat.add(map_ser);
                        }

                    }


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
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("user_type", globalClass.getUser_type());
                params.put("user_id", globalClass.getId());

                Log.d(TAG, "param: "+params);

                return params;
            }

        };
        // Adding request to request queue
        GlobalClass.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,
                10, 1.0f));

    }


    @Override
    public void onRefresh() {

        if (all_upcoming.equals("all")){
            forAllVisitor();
        }else {
            forUpcomingVisitor();
        }

    }





    //// in app update ...
    public static final int UPDATE_REQUEST_CODE = 411;
    private void inAppUpdate(){

        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(Activity_activity.this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                try {

                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            UPDATE_REQUEST_CODE);

                }catch (Exception e){
                    e.printStackTrace();
                }




            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.d(TAG, "onActivityResult: "+resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }
}
