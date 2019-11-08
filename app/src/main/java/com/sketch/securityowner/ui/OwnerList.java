package com.sketch.securityowner.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.sketch.securityowner.Adapter.OwnerListAdapter;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.sketch.securityowner.model.FloorOwner;
import com.sketch.securityowner.model.HeaderItem;
import com.sketch.securityowner.model.ListItem;
import com.sketch.securityowner.model.Owner;
import com.sketch.securityowner.model.OwnerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class OwnerList extends AppCompatActivity implements
        OwnerListAdapter.ViewClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;


    GlobalClass globalClass;
    Shared_Preference prefManager;
    ProgressDialog progressDialog;

    ArrayList<FloorOwner> listFloorOwner;
    ArrayList<ListItem> mListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_list);
        ButterKnife.bind(this);
        actionViews();


    }

    private void actionViews(){

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");



        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            String block = bundle.getString("block");

            getSupportActionBar().setTitle(block+" 's Owner");

            getFlatOwnerList(block);
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


    public void getFlatOwnerList(String block){
        progressDialog.show();

        listFloorOwner = new ArrayList<>();

        String url = AppConfig.URL_DEV+"flat_list";

        final Map<String, String> params = new HashMap<>();
        params.put("complex_id", globalClass.getComplex_id());
        params.put("block", block);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(AppConfig.TAG , "flat_list- " +response);

                if (response != null){
                    try {

                        JSONObject main_object = new JSONObject(response);

                        int status = main_object.optInt("status");
                        String  message = main_object.optString("message");
                        if (status == 1){

                            JSONArray all_data = main_object.getJSONArray("all_data");

                            for (int i = 0; i < all_data.length(); i++){
                                JSONObject object = all_data.getJSONObject(i);

                                String floor = object.optString("floor");

                                FloorOwner floorOwner = new FloorOwner();
                                floorOwner.setFloor(floor);


                                ArrayList<Owner> arrayList = new ArrayList<>();
                                JSONArray data = object.getJSONArray("data");
                                for (int j = 0; j < data.length(); j++){
                                    JSONObject obj1 = data.getJSONObject(j);

                                    Owner owner = new Owner();
                                    owner.setId(obj1.optString("id"));
                                    owner.setFlat_no(obj1.optString("flat_no"));
                                    owner.setFloor(floor);
                                    owner.setName(obj1.optString("name"));
                                    owner.setMobile(obj1.optString("mobile"));
                                    owner.setEmailid(obj1.optString("emailid"));


                                    arrayList.add(owner);

                                }

                                floorOwner.setOwnerArrayList(arrayList);


                                listFloorOwner.add(floorOwner);

                            }

                            setOwnerData();

                        }

                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //on error storing the name to sqlite with status unsynced
                Log.e(AppConfig.TAG, "error - "+error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };

        VolleySingleton.getInstance(OwnerList.this)
                .addToRequestQueue(stringRequest
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }


    private void setOwnerData(){

     /*   ArrayList<HashMap<String, ArrayList<Owner>>> listMain = new ArrayList<>();
        for (int i = 0; i < listFloorOwner.size(); i++){

            HashMap<String, ArrayList<Owner>> map = new HashMap<>();

            ArrayList<Owner> arrayList2 = listFloorOwner.get(i).getOwnerArrayList();

            if (arrayList2.size() > 0){
                map.put(listFloorOwner.get(i).getFloor(), arrayList2);
                listMain.add(map);
            }

        }

        mListItem = new ArrayList<>();
        for (HashMap<String, ArrayList<Owner>> map1 : listMain){

            for (String string : map1.keySet()) {

                HeaderItem header = new HeaderItem();
                header.setHeader(string);
                mListItem.add(header);

                for (Owner owner : map1.get(string)) {
                    OwnerItem item = new OwnerItem();
                    item.setOwner(owner);
                    mListItem.add(item);
                }

            }

        }*/



        mListItem = new ArrayList<>();
        for (FloorOwner floorOwner : listFloorOwner){

            HeaderItem header = new HeaderItem();
            header.setHeader(floorOwner.getFloor());
            mListItem.add(header);

            for (Owner owner : floorOwner.getOwnerArrayList()) {
                OwnerItem ownerItem = new OwnerItem();
                ownerItem.setOwner(owner);
                mListItem.add(ownerItem);

            }

        }

        OwnerListAdapter ownerListAdapter =
                new OwnerListAdapter(OwnerList.this, mListItem);
        recycler_view.setAdapter(ownerListAdapter);
        ownerListAdapter.setViewClickListener(this);

    }

    @Override
    public void onCallClicked(Owner owner) {

        checkCallPermission(owner.getMobile());

    }
    private static final int REQUEST_PHONE_CALL = 1212;
    private void checkCallPermission(String number){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(OwnerList.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(OwnerList.this,
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

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));// Initiates the Intent
        startActivity(intent);

    }



}
