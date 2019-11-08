package com.sketch.securityowner.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Adapter.AdapterSecurityGuard;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class FragmentNoticeBoard extends Fragment {

    RecyclerView recyclerView;
    ListView list;
    ArrayList<HashMap<String,String>> noticeList;
    ProgressDialog pd;
    File p_image;
    GlobalClass globalClass;
    AdapterSecurityGuard adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment, container, false);
        return rootView;


        /**The below code was when the ListView was used in place of RecyclerView. **/





    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalClass=(GlobalClass)getActivity().getApplicationContext();
        noticeList=new ArrayList<>();
        pd = new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));
        String[] items = getResources().getStringArray(R.array.tab_A);
      //  AdapterNoticeBoard adapter = new AdapterNoticeBoard(getActivity(),items);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        BrowseNotice();
    }
    private void BrowseNotice() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        noticeList.clear();
        //  startAnim();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.notice, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE Help : " + response.toString());


                // stopAnim();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {




                        JsonArray jarray = jobj.getAsJsonArray("data");


                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            //get the object


                            String help_id = jobj1.get("help_id").toString().replaceAll("\"", "");
                            String content = jobj1.get("content").toString().replaceAll("\"", "");
                            String image = jobj1.get("image").toString().replaceAll("\"", "");
                            String date = jobj1.get("date").toString().replaceAll("\"", "");
                            String time = jobj1.get("time").toString().replaceAll("\"", "");
                            String status1 = jobj1.get("status").toString().replaceAll("\"", "");



                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("help_id", help_id);
                            map_ser.put("content", content);
                            map_ser.put("image", image);
                            map_ser.put("date", date);
                            map_ser.put("time", time);
                            map_ser.put("status", status1);



                            noticeList.add(map_ser);
                            Log.d(TAG, "cityList: "+noticeList);




                        }

                        adapter = new AdapterSecurityGuard(getActivity(), noticeList);
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getActivity(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getActivity(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

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