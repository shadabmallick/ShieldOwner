package com.sketch.securityowner.Fragment;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
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
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Adapter.AdapterSecurityList;
import com.sketch.securityowner.Adapter.AlertAdapter;
import com.sketch.securityowner.Adapter.RecyclerViewAdapter;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;


public class FragmentC extends Fragment {

    RecyclerView recyclerView;
    GlobalClass globalClass;
    AlertAdapter adapter;
    ArrayList<HashMap<String,String>> blockList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_alert, container, false);
        return rootView;

        /**The below code was when the ListView was used in place of RecyclerView. **/

        /*View view = inflater.inflate(R.layout.fragment_list, container, false);

        list = (ListView) view.findViewById(R.id.list);
        ArrayList stringList= new ArrayList();

        stringList.add("Item 3A");
        stringList.add("Item 3B");
        stringList.add("Item 3C");
        stringList.add("Item 3D");
        stringList.add("Item 3E");
        stringList.add("Item 3F");
        stringList.add("Item 3G");
        stringList.add("Item 3H");
        stringList.add("Item 3I");
        stringList.add("Item 3J");
        stringList.add("Item 3K");
        stringList.add("Item 3L");
        stringList.add("Item 3M");
        stringList.add("Item 3N");
        stringList.add("Item 3O");
        stringList.add("Item 3P");
        stringList.add("Item 3Q");
        stringList.add("Item 3R");
        stringList.add("Item 3S");
        stringList.add("Item 3T");
        stringList.add("Item 3U");
        stringList.add("Item 3V");
        stringList.add("Item 3W");
        stringList.add("Item 3X");
        stringList.add("Item 3Y");
        stringList.add("Item 3Z");

        CustomAdapter adapter = new CustomAdapter(stringList,getActivity());
        list.setAdapter(adapter);

        return view;*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalClass=(GlobalClass)getActivity().getApplicationContext();
        blockList=new ArrayList<>();
       // String[] items = getResources().getStringArray(R.array.tab_C);
      //  RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),items);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        BrowseAlertList();
    }
    private void BrowseAlertList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        blockList.clear();
        //  startAnim();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"user_panic_list", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());


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


                            String panic_id = jobj1.get("panic_id").toString().replaceAll("\"", "");
                            String category = jobj1.get("category").toString().replaceAll("\"", "");
                            String date = jobj1.get("date").toString().replaceAll("\"", "");




                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("panic_id", panic_id);
                            map_ser.put("category", category);
                            map_ser.put("date", date);




                            blockList.add(map_ser);
                            Log.d(TAG, "cityList: "+blockList);




                        }

                        adapter = new AlertAdapter(getActivity(), blockList);
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



                params.put("user_id","278");


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