package com.sketch.securityowner.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.sketch.securityowner.Adapter.AdapterHelpDesk;
import com.sketch.securityowner.Adapter.AdapterHelpList;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.sketch.securityowner.dialogs.LoaderDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class HelpListDetails extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    GlobalClass globalClass;
    Shared_Preference prefManager;
    AdapterHelpList adapterHelpList;
    ArrayList<HashMap<String,String>> cityList;
    LinearLayout ll_data_not_found;

    LoaderDialog loaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_list);
        ButterKnife.bind(this);

        ll_data_not_found=findViewById(R.id.ll_data_not_found);

        actionViews();
    }
    private void actionViews()  {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);


        cityList=new ArrayList<>();


        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            String block = bundle.getString("category");

            getSupportActionBar().setTitle(block);

            HelpListDetails(block);
        }



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

    private void HelpListDetails(final String category) {
        // Tag used to cancel the request
        cityList.clear();
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.local_help_list, new Response.Listener<String>() {

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
                            //get the object


                            String id = jobj1.get("id").toString().replaceAll("\"", "");
                            String name = jobj1.get("name").toString().replaceAll("\"", "");
                            String image = jobj1.get("image").toString().replaceAll("\"", "");
                            String category = jobj1.get("category").toString().replaceAll("\"", "");
                            String mobile = jobj1.get("mobile").toString().replaceAll("\"", "");

                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("id", id);
                            map_ser.put("name", name);
                            map_ser.put("image", image);
                            map_ser.put("category", category);
                            map_ser.put("mobile", mobile);


                            cityList.add(map_ser);
                            Log.d(TAG, "cityList: "+cityList);




                        }

                        adapterHelpList = new AdapterHelpList(HelpListDetails.this, cityList);
                        recycler_view.setAdapter(adapterHelpList);
                    }
                    else {
                        ll_data_not_found.setVisibility(View.VISIBLE);
                        recycler_view.setVisibility(View.GONE);
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    ll_data_not_found.setVisibility(View.VISIBLE);
                    recycler_view.setVisibility(View.GONE);
                   // TastyToast.makeText(HelpListDetails.this, "DATA NOT FOUND", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(HelpListDetails.this, "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("category", category);
                Log.d(TAG, "getParams: "+params);

                return params;
            }


        };

        // Adding request to request queue
        VolleySingleton.getInstance(HelpListDetails.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

}
