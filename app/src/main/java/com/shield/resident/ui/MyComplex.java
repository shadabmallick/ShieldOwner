package com.shield.resident.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shield.resident.Adapter.AdapterMyFlat;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyComplex extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    ImageView img_back;
    LinearLayout ll_add_my_complex;
    RecyclerView recycler_view;
    SwipeRefreshLayout swipe_refresh;

    GlobalClass globalClass;
    Shared_Preference preference;
    LoaderDialog loaderDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_complex);

        globalClass = (GlobalClass) getApplicationContext();
        preference = new Shared_Preference(this);
        preference.loadPrefrence();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        swipe_refresh = findViewById(R.id.swipe_refresh);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        swipe_refresh.setOnRefreshListener(this);

        img_back = findViewById(R.id.img_back);
        ll_add_my_complex = findViewById(R.id.ll_add_my_complex);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ll_add_my_complex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tenant = new Intent(getApplicationContext(), AddMultipleFlat.class);
                startActivity(tenant);
            }
        });



        loaderDialog.show();
        getFlatListOwnerWise();
    }

    @Override
    public void onRefresh() {
        getFlatListOwnerWise();
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
                Log.d("TAG", "flat_list: " + response.toString());

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
                            map_ser.put("user_type", user_type);
                            map_ser.put("address", csvBuilder.toString());
                            map_ser.put("payu_key", payu_key);
                            map_ser.put("payu_mid", payu_mid);
                            map_ser.put("payu_salt", payu_salt);
                            map_ser.put("parking_no", parking_no);
                            map_ser.put("parking_id", parking_id);
                            map_ser.put("owner", owner);

                            // 1 = owner, 4 = member, 6 = tenant


                            if (globalClass.getFlat_id().equals(flat_id)){

                                globalClass.setFlat_id(map_ser.get("flat_id"));
                                globalClass.setFlat_name(map_ser.get("flat_no"));

                                globalClass.setUser_type(map_ser.get("user_type"));
                                globalClass.setComplex_id(map_ser.get("complex_id"));
                                globalClass.setComplex_name(map_ser.get("complex_name"));
                                globalClass.setBlock(map_ser.get("block"));
                                globalClass.setIs_tenant(map_ser.get("tenant"));
                                globalClass.setPayment_system(map_ser.get("payment_system"));
                                globalClass.setComplex_address(csvBuilder.toString());

                                globalClass.setPayu_mkey(map_ser.get("payu_key"));
                                globalClass.setPayu_mid(map_ser.get("payu_mid"));
                                globalClass.setPayu_salt(map_ser.get("payu_salt"));
                                globalClass.setParking_no(map_ser.get("parking_no"));
                                globalClass.setParking_id(map_ser.get("parking_id"));
                                globalClass.setOwner(map_ser.get("owner"));

                                preference.savePrefrence();
                                preference.loadPrefrence();

                            }

                            listOwnerFlat.add(map_ser);
                        }

                        AdapterMyFlat adapterMyFlat =
                                new AdapterMyFlat(MyComplex.this, listOwnerFlat);
                        recycler_view.setAdapter(adapterMyFlat);

                    }

                    loaderDialog.dismiss();
                    if (swipe_refresh.isRefreshing()){
                        swipe_refresh.setRefreshing(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "DATA NOT FOUND: " + error.getMessage());
                loaderDialog.dismiss();
                if (swipe_refresh.isRefreshing()){
                    swipe_refresh.setRefreshing(false);
                }
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<>();

                params.put("user_type", globalClass.getUser_type());
                params.put("user_id", globalClass.getId());

                Log.d("TAG", "param: "+params);

                return params;
            }

        };
        // Adding request to request queue
        GlobalClass.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,
                10, 1.0f));

    }




}