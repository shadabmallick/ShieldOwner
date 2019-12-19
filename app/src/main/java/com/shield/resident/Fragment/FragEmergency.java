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
import com.shield.resident.Adapter.EmergencyAdapter;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;


public class FragEmergency extends Fragment implements EmergencyAdapter.onItemClickListner {

    RecyclerView recyclerView;
    GlobalClass globalClass;
    ArrayList<HashMap<String,String>> listEmergency;

    LoaderDialog loaderDialog;

    LinearLayout linear_nodata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_alert, container, false);
        return rootView;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalClass=(GlobalClass)getActivity().getApplicationContext();
        listEmergency =new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        linear_nodata = view.findViewById(R.id.linear_nodata);
        linear_nodata.setVisibility(View.GONE);

        loaderDialog = new LoaderDialog(getActivity(), android.R.style.Theme_Translucent,
                false, "");


        BrowseAlertList();
    }
    private void BrowseAlertList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        listEmergency.clear();
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.emergency_contact, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "emergency_contact RESPONSE: " + response.toString());


                loaderDialog.dismiss();

                Gson gson = new Gson();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        JsonArray data = jobj.getAsJsonArray("data");


                        for (int i = 0; i < data.size(); i++) {
                            JsonObject jobj1 = data.get(i).getAsJsonObject();

                            String name = jobj1.get("name").toString().replaceAll("\"", "");
                            String contact = jobj1.get("contact").toString().replaceAll("\"", "");

                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("name", name);
                            map_ser.put("contact", contact);

                            listEmergency.add(map_ser);

                        }
                        setData();

                    }


                    if (listEmergency.size() == 0){
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

                params.put("complex_id", globalClass.getComplex_id());

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

        EmergencyAdapter adapter = new EmergencyAdapter(getActivity(), listEmergency,
                this);
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