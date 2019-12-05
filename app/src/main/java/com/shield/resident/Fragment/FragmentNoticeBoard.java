package com.shield.resident.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shield.resident.Adapter.AdapterHelp;
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

public class FragmentNoticeBoard extends Fragment {

    RecyclerView recyclerView;
    ArrayList<HashMap<String,String>> noticeList;
    GlobalClass globalClass;
    AdapterHelp adapter;

    LoaderDialog loaderDialog;
    LinearLayout linear_nodata;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalClass=(GlobalClass)getActivity().getApplicationContext();
        noticeList=new ArrayList<>();

        loaderDialog = new LoaderDialog(getActivity(), android.R.style.Theme_Translucent,
                false, "");

        recyclerView = view.findViewById(R.id.recycler_view);
        linear_nodata = view.findViewById(R.id.linear_nodata);
        linear_nodata.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        BrowseNotice();


    }
    private void BrowseNotice() {
        String tag_string_req = "req_login";
        noticeList.clear();
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.notice, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE notice : " + response.toString());


                loaderDialog.dismiss();

                try {

                    JSONObject jobj = new JSONObject(response);
                    String status = jobj.optString("status");
                    String message = jobj.optString("message");


                    if(status.equals("1")) {

                        JSONArray jarray = jobj.getJSONArray("data");

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            String subject = jobj1.optString("subject");
                            String content = jobj1.optString("content");
                            String added_by = jobj1.optString("added_by");

                            JSONArray files = jobj1.getJSONArray("files");

                            HashMap<String, String> map_ser = new HashMap<>();

                            map_ser.put("subject", subject);
                            map_ser.put("content", content);
                            map_ser.put("added_by", added_by);
                            map_ser.put("files", files.toString());

                            noticeList.add(map_ser);
                          //  Log.d(TAG, "notice: "+noticeList);

                        }

                        adapter = new AdapterHelp(getActivity(), noticeList);
                        recyclerView.setAdapter(adapter);


                    }

                    if (noticeList.size() == 0){
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
                Log.d(TAG, "getParams: "+params);

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