package com.sketch.securityowner.Fragment;


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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Adapter.AdapterSecurityGuard;
import com.sketch.securityowner.Adapter.AdapterSecurityList;
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

public class FragmentB extends Fragment {
    ListView list;
    GlobalClass globalClass;
    AdapterSecurityList adapter;
    ArrayList<HashMap<String,String>> blockList;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_guard, container, false);
        return rootView;

        /**The below code was when the ListView was used in place of RecyclerView. **/

        /*View view = inflater.inflate(R.layout.fragment_list, container, false);

        list = (ListView) view.findViewById(R.id.list);
        ArrayList stringList= new ArrayList();

        stringList.add("Item 2A");
        stringList.add("Item 2B");
        stringList.add("Item 2C");
        stringList.add("Item 2D");
        stringList.add("Item 2E");
        stringList.add("Item 2F");
        stringList.add("Item 2G");
        stringList.add("Item 2H");
        stringList.add("Item 2I");
        stringList.add("Item 2J");
        stringList.add("Item 2K");
        stringList.add("Item 2L");
        stringList.add("Item 2M");
        stringList.add("Item 2N");
        stringList.add("Item 2O");
        stringList.add("Item 2P");
        stringList.add("Item 2Q");
        stringList.add("Item 2R");
        stringList.add("Item 2S");
        stringList.add("Item 2T");
        stringList.add("Item 2U");
        stringList.add("Item 2V");
        stringList.add("Item 2W");
        stringList.add("Item 2X");
        stringList.add("Item 2Y");
        stringList.add("Item 2Z");

        CustomAdapter adapter = new CustomAdapter(stringList,getActivity());
        list.setAdapter(adapter);

        return view;*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalClass=(GlobalClass)getActivity().getApplicationContext();
        blockList=new ArrayList<>();
       // String[] items = getResources().getStringArray(R.array.tab_B);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        BrowseBlock();
    }

    private void BrowseBlock() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        blockList.clear();
        //  startAnim();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DEV+"security_list", new Response.Listener<String>() {

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


                            String id = jobj1.get("id").toString().replaceAll("\"", "");
                            String name = jobj1.get("name").toString().replaceAll("\"", "");
                            String image = jobj1.get("image").toString().replaceAll("\"", "");
                            String emailid = jobj1.get("emailid").toString().replaceAll("\"", "");
                            String mobile = jobj1.get("mobile").toString().replaceAll("\"", "");



                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("id", id);
                            map_ser.put("name", name);
                            map_ser.put("image", image);
                            map_ser.put("emailid", emailid);
                            map_ser.put("mobile", mobile);



                            blockList.add(map_ser);
                            Log.d(TAG, "cityList: "+blockList);




                        }

                        adapter = new AdapterSecurityList(getActivity(), blockList);
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



                params.put("complex_id","277");


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