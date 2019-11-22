package com.sketch.securityowner.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Adapter.ProfileFlatAdapter;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.R;
import com.sketch.securityowner.ui.InvoiceList;
import com.sketch.securityowner.ui.SettingActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DialogProfile extends Dialog implements ProfileFlatAdapter.ViewClickListener {

    private Context context;
    private GlobalClass globalClass;
    private Shared_Preference preference;

    private ImageView profile_image;
    private TextView user_name,block,address;
    private LinearLayout ll_setting,ll_invoice;
    private RecyclerView recycler_user_flats;


    private LoaderDialog loaderDialog;
    ArrayList<HashMap<String, String>> mapArrayList;

    public boolean is_clicked = false;

    public DialogProfile(@NonNull Context context) {
        super(context, R.style.datepicker);
        this.context = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dailog_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        globalClass = (GlobalClass) context.getApplicationContext();
        preference = new Shared_Preference(context);

        loaderDialog = new LoaderDialog(context, android.R.style.Theme_Translucent,
                false, "");


        profile_image = findViewById(R.id.profile_image);
        ll_setting = findViewById(R.id.ll_setting);
        ll_invoice = findViewById(R.id.ll_invoice);
        user_name = findViewById(R.id.user_name);
        block = findViewById(R.id.block);
        address = findViewById(R.id.address);
        recycler_user_flats = findViewById(R.id.recycler_user_flats);


        if (!globalClass.getProfil_pic().isEmpty()){
            Picasso.with(context)
                    .load(globalClass.getProfil_pic())
                    .fit().centerInside()
                    .rotate(90)
                    .error(R.mipmap.profile_image)
                    .placeholder(R.mipmap.profile_image)
                    .into(profile_image);
        }

        user_name.setText(globalClass.getName());
        address.setText(globalClass.getComplex_name());
        block.setText(globalClass.getFlat_name()+" "
                +globalClass.getBlock()+" "+"block");

        // if button is clicked, close the custom dialog
        ll_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent setting=new Intent(context, SettingActivity.class);
                context.startActivity(setting);
            }
        });

        ll_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent setting=new Intent(context, InvoiceList.class);
                context.startActivity(setting);

            }
        });


        mapArrayList = new ArrayList<>();
        recycler_user_flats.setLayoutManager(new LinearLayoutManager(context));


        getFlatListOwnerWise();

    }


    private void getFlatListOwnerWise() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.user_complex_flat_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "flat_list: " + response.toString());

                try {

                    JSONObject object = new JSONObject(response);

                    String status = object.optString("status");
                    String message = object.optString("message");

                    if(status.equals("1")) {

                        JSONObject data = object.getJSONObject("data");

                        JSONArray flat_details = data.getJSONArray("flat_details");

                        for (int j = 0; j < flat_details.length(); j++) {
                            JSONObject flat = flat_details.getJSONObject(j);

                            String complex_id = flat.optString("complex_id");
                            String user_type = flat.optString("user_type");
                            String complex_name = flat.optString("complex_name");
                            String flat_id = flat.optString("flat_id");
                            String flat_no = flat.optString("flat_no");
                            String block = flat.optString("block");
                            String floor = flat.optString("floor");

                            HashMap<String, String> map_ser = new HashMap<>();

                            map_ser.put("complex_id", complex_id);
                            map_ser.put("user_type", user_type);
                            map_ser.put("complex_name", complex_name);
                            map_ser.put("flat_id", flat_id);
                            map_ser.put("flat_no", flat_no);
                            map_ser.put("block", block);
                            map_ser.put("floor", floor);

                            mapArrayList.add(map_ser);
                        }


                        setData();

                    } else {
                        TastyToast.makeText(context,
                                message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                    loaderDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                loaderDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("user_type", "Flat Owner");
                params.put("user_id", globalClass.getId());

                Log.d(TAG, "param: "+params);

                return params;
            }

        };
        // Adding request to request queue
        GlobalClass.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,
                10, 1.0f));

    }

    private void setData(){
        ProfileFlatAdapter profileFlatAdapter =
                new ProfileFlatAdapter(context, mapArrayList);
        recycler_user_flats.setAdapter(profileFlatAdapter);
        profileFlatAdapter.setViewClickListener(this);

    }

    @Override
    public void onClicked(HashMap<String, String> hashMap) {

        is_clicked = true;

        globalClass.setFlat_no(hashMap.get("flat_id"));
        globalClass.setFlat_name(hashMap.get("flat_no"));

        preference.savePrefrence();
        preference.loadPrefrence();

        dismiss();

    }
}
