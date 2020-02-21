package com.shield.resident.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

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
import com.shield.resident.Adapter.ComMemberListAdapter;
import com.shield.resident.Adapter.OwnerListAdapter;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;
import com.shield.resident.model.ComMember;
import com.shield.resident.model.ComMemberItem;
import com.shield.resident.model.Committee;
import com.shield.resident.model.HeaderItem;
import com.shield.resident.model.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class CommitteeList extends AppCompatActivity implements
        ComMemberListAdapter.ViewClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    @BindView(R.id.linear_nodata)
    LinearLayout linear_nodata;

    LoaderDialog loaderDialog;
    GlobalClass globalClass;
    Shared_Preference prefManager;

    ArrayList<Committee> committeeArrayList;
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
        getSupportActionBar().setTitle("Committee");

        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        linear_nodata.setVisibility(View.GONE);


        getCommitteeDetails();

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


    public void getCommitteeDetails(){
        loaderDialog.show();

        committeeArrayList = new ArrayList<>();

        String url = AppConfig.committee_details;

        final Map<String, String> params = new HashMap<>();
        params.put("complex_id", globalClass.getComplex_id());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(AppConfig.TAG , "committee_details- " +response);

                if (response != null){
                    try {

                        JSONObject main_object = new JSONObject(response);

                        int status = main_object.optInt("status");
                        String  message = main_object.optString("message");
                        if (status == 1){

                            JSONArray data = main_object.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++){
                                JSONObject object = data.getJSONObject(i);

                                String commitee_name = object.optString("commitee_name");
                                String description = object.optString("description");

                                Committee committee = new Committee();
                                committee.setCommittee_name(commitee_name);
                                committee.setDescription(description);


                                ArrayList<ComMember> arrayList = new ArrayList<>();
                                JSONArray member = object.getJSONArray("member");
                                for (int j = 0; j < member.length(); j++){
                                    JSONObject obj1 = member.getJSONObject(j);

                                    ComMember comMember = new ComMember();
                                    comMember.setName(obj1.optString("name"));
                                    comMember.setMobile(obj1.optString("mobile"));
                                    comMember.setRoll(obj1.optString("roll"));
                                    comMember.setImage(obj1.optString("image"));

                                    arrayList.add(comMember);

                                }

                                committee.setComMemberArrayList(arrayList);

                                committeeArrayList.add(committee);

                            }

                        }

                        setCommitteeData();

                        if (committeeArrayList.size() == 0){
                            linear_nodata.setVisibility(View.VISIBLE);
                        }


                        loaderDialog.dismiss();

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

        VolleySingleton.getInstance(CommitteeList.this)
                .addToRequestQueue(stringRequest
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }


    private void setCommitteeData(){

        mListItem = new ArrayList<>();
        for (Committee committee : committeeArrayList){

            HeaderItem header = new HeaderItem();
            header.setHeader(committee.getCommittee_name());
            mListItem.add(header);

            for (ComMember comMember : committee.getComMemberArrayList()) {
                ComMemberItem comMemberItem = new ComMemberItem();
                comMemberItem.setComMember(comMember);
                mListItem.add(comMemberItem);

            }

        }

        ComMemberListAdapter comMemberListAdapter =
                new ComMemberListAdapter(CommitteeList.this, mListItem);
        recycler_view.setAdapter(comMemberListAdapter);
        comMemberListAdapter.setViewClickListener(this);


    }

    @Override
    public void onCallClicked(ComMember comMember) {

        checkCallPermission(comMember.getMobile());

    }

    private static final int REQUEST_PHONE_CALL = 1212;
    private void checkCallPermission(String number){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(CommitteeList.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(CommitteeList.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

            } else {
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
