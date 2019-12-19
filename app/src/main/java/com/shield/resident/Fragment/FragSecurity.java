package com.shield.resident.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.shield.resident.Adapter.AdapterSecurityList;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class FragSecurity extends Fragment implements AdapterSecurityList.onItemClickListner{

    private GlobalClass globalClass;
    private AdapterSecurityList adapter;
    private ArrayList<HashMap<String,String>> blockList;
    private RecyclerView recyclerView;
    private LinearLayout linear_nodata;

    private LoaderDialog loaderDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_guard, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalClass=(GlobalClass)getActivity().getApplicationContext();
        blockList=new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        linear_nodata = view.findViewById(R.id.linear_nodata);
        linear_nodata.setVisibility(View.GONE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        loaderDialog = new LoaderDialog(getActivity(), android.R.style.Theme_Translucent,
                false, "");


        BrowseBlock();
    }

    private void BrowseBlock() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        blockList.clear();

        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.security_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());
                loaderDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");

                    if(status.equals("1")) {

                        JSONArray jarray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            String id = jobj1.optString("id");
                            String name = jobj1.optString("name");
                            String image = jobj1.optString("image");
                            String emailid = jobj1.optString("emailid");
                            String mobile = jobj1.optString("mobile");

                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("id", id);
                            map_ser.put("name", name);
                            map_ser.put("image", image);
                            map_ser.put("emailid", emailid);
                            map_ser.put("mobile", mobile);

                            blockList.add(map_ser);

                        }

                       setData();

                    }

                    if (blockList.size() == 0){
                        linear_nodata.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("complex_id",globalClass.getComplex_id());

                return params;
            }


        };

        // Adding request to request queue
        VolleySingleton.getInstance(getActivity())
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void setData(){

        adapter = new AdapterSecurityList(getActivity(), blockList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String number) {
        checkCallPermission(number);
    }


    private static final int REQUEST_PHONE_CALL = 1212;
    private void checkCallPermission(String number){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

            }
            else {
                callPhone(number);
            }
        }
        else {
            callPhone(number);
        }

    }

    private void callPhone(String number){

        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + number));// Initiates the Intent
        startActivity(intent);

    }
}