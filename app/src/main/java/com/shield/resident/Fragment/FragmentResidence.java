package com.shield.resident.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.shield.resident.Adapter.AdapterResidence;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;
import com.shield.resident.ui.CommitteeList;
import com.shield.resident.util.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class FragmentResidence extends Fragment {

    private RecyclerView recyclerView;
    private CardView carview;

    private ArrayList<HashMap<String,String>> blockList;
    private AdapterResidence adapter;
    private GlobalClass globalClass;

    private LoaderDialog loaderDialog;
    private LinearLayout linear_nodata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resident, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loaderDialog = new LoaderDialog(getActivity(), android.R.style.Theme_Translucent,
                false, "");

        globalClass = (GlobalClass)getActivity().getApplicationContext();
        blockList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view);
        carview = view.findViewById(R.id.carview);
        linear_nodata = view.findViewById(R.id.linear_nodata);
        linear_nodata.setVisibility(View.GONE);


        carview.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CommitteeList.class);
            startActivity(intent);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        BrowseBlock();

    }

    private void BrowseBlock() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        blockList.clear();
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.block_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                loaderDialog.dismiss();

                Gson gson = new Gson();

                try {
                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    if(status.equals("1")) {

                        JsonArray jarray = jobj.getAsJsonArray("data");


                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobj1 = jarray.get(i).getAsJsonObject();

                            String block = jobj1.get("block").toString().replaceAll("\"", "");

                            HashMap<String, String> map_ser = new HashMap<>();
                            map_ser.put("block", block);

                            blockList.add(map_ser);

                        }

                        adapter = new AdapterResidence(getActivity(), blockList);
                        recyclerView.setAdapter(adapter);

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
                // Posting parameters to activity_login url
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

}