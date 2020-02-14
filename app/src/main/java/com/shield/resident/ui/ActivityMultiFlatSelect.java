package com.shield.resident.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shield.resident.Adapter.ProfileFlatAdapter_1st;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;
import com.shield.resident.util.Commons;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityMultiFlatSelect extends AppCompatActivity implements
        ProfileFlatAdapter_1st.ViewClickListener {

    String TAG="Login";

    RecyclerView recycler_view;
    RelativeLayout rl_continue;
    LinearLayout linear_main;


    GlobalClass globalClass;
    Shared_Preference prefrence;
    LoaderDialog loaderDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiflatselect);

        globalClass = (GlobalClass)getApplicationContext();
        prefrence = new Shared_Preference(ActivityMultiFlatSelect.this);
        prefrence.loadPrefrence();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        rl_continue = findViewById(R.id.rl_continue);
        linear_main = findViewById(R.id.linear_main);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        linear_main.setVisibility(View.GONE);

        rl_continue.setOnClickListener(v -> {

            globalClass.setLogin_status(true);

            prefrence.savePrefrence();
            prefrence.loadPrefrence();

            if (globalClass.getFirst_time_login().equalsIgnoreCase("Y")){
                Intent setting=new Intent(getApplicationContext(),SettingActivity.class);
                setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(setting);
                finish();
            }else {
                Intent setting=new Intent(getApplicationContext(),Activity_activity.class);
                setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(setting);
                finish();
            }

        });


        getFlatListOwnerWise();
    }


    ArrayList<HashMap<String, String>> listOwnerFlat;
    private void getFlatListOwnerWise() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        listOwnerFlat = new ArrayList<>();

        loaderDialog.show();

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
                            String payu_key = flat.optString("payu_key");
                            String payu_mid = flat.optString("payu_mid");
                            String payu_salt = flat.optString("payu_salt");
                            String owner = flat.optString("owner");

                            String complex_address = flat.optString("complex_address");
                            String city = flat.optString("city");
                            String state = flat.optString("state");
                            String pin = flat.optString("pin");
                            String parking_no = flat.optString("parking_no");
                            String parking_id = flat.optString("parking_id");

                            StringBuilder csvBuilder = new StringBuilder();
                            csvBuilder.append(complex_address).append(",");
                            csvBuilder.append(city).append(",");
                            csvBuilder.append(state).append(",");
                            csvBuilder.append(pin);

                            HashMap<String, String> map_ser = new HashMap<>();

                            map_ser.put("complex_id", complex_id);

                            map_ser.put("complex_name", complex_name);
                            map_ser.put("flat_id", flat_id);
                            map_ser.put("flat_no", flat_no);
                            map_ser.put("block", block);
                            map_ser.put("floor", floor);
                            map_ser.put("tenant", tenant);
                            map_ser.put("payment_system", payment_system);
                            map_ser.put("address", csvBuilder.toString());
                            map_ser.put("payu_key", payu_key);
                            map_ser.put("payu_mid", payu_mid);
                            map_ser.put("payu_salt", payu_salt);
                            map_ser.put("parking_no", parking_no);
                            map_ser.put("parking_id", parking_id);
                            map_ser.put("owner", owner);

                            // 1 = owner, 4 = member, 6 = tenant
                            map_ser.put("user_type", user_type);


                            listOwnerFlat.add(map_ser);
                        }


                        setFlatData();
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
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
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


    private void setFlatData(){

        if (listOwnerFlat.size() == 1){

            HashMap<String, String> hashMap = listOwnerFlat.get(0);

            globalClass.setFlat_id(hashMap.get("flat_id"));
            globalClass.setFlat_name(hashMap.get("flat_no"));

            globalClass.setUser_type(hashMap.get("user_type"));
            globalClass.setComplex_id(hashMap.get("complex_id"));
            globalClass.setComplex_name(hashMap.get("complex_name"));
            globalClass.setBlock(hashMap.get("block"));
            globalClass.setIs_tenant(hashMap.get("tenant"));
            globalClass.setPayment_system(hashMap.get("payment_system"));
            globalClass.setComplex_address(hashMap.get("address"));

            globalClass.setPayu_mkey(hashMap.get("payu_key"));
            globalClass.setPayu_mid(hashMap.get("payu_mid"));
            globalClass.setPayu_salt(hashMap.get("payu_salt"));
            globalClass.setParking_no(hashMap.get("parking_no"));
            globalClass.setParking_id(hashMap.get("parking_id"));
            globalClass.setOwner(hashMap.get("owner"));


            globalClass.setLogin_status(true);

            prefrence.savePrefrence();
            prefrence.loadPrefrence();

            if (globalClass.getFirst_time_login().equalsIgnoreCase("Y")){
                Intent setting=new Intent(getApplicationContext(),SettingActivity.class);
                setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(setting);
                finish();
            }else {
                Intent setting=new Intent(getApplicationContext(),Activity_activity.class);
                setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(setting);
                finish();
            }

        }else {

            ProfileFlatAdapter_1st adapter_1st =
                    new ProfileFlatAdapter_1st(ActivityMultiFlatSelect.this,
                            listOwnerFlat);

            recycler_view.setAdapter(adapter_1st);
            adapter_1st.setViewClickListener(this);

            linear_main.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClicked(HashMap<String, String> hashMap) {


        globalClass.setFlat_id(hashMap.get("flat_id"));
        globalClass.setFlat_name(hashMap.get("flat_no"));

        globalClass.setUser_type(hashMap.get("user_type"));
        globalClass.setComplex_id(hashMap.get("complex_id"));
        globalClass.setComplex_name(hashMap.get("complex_name"));
        globalClass.setBlock(hashMap.get("block"));
        globalClass.setIs_tenant(hashMap.get("tenant"));
        globalClass.setPayment_system(hashMap.get("payment_system"));
        globalClass.setComplex_address(hashMap.get("address"));

        globalClass.setPayu_mkey(hashMap.get("payu_key"));
        globalClass.setPayu_mid(hashMap.get("payu_mid"));
        globalClass.setPayu_salt(hashMap.get("payu_salt"));
        globalClass.setParking_no(hashMap.get("parking_no"));
        globalClass.setParking_id(hashMap.get("parking_id"));
        globalClass.setOwner(hashMap.get("owner"));

    }
}
