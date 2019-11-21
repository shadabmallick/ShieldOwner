package com.sketch.securityowner.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class AddMultipleFlat extends AppCompatActivity {
    String TAG="RegistrationActivity";
    RecyclerView recyclerView;
    RelativeLayout rel_next,rel_login;
    AVLoadingIndicatorView avLoadingIndicatorView;
    GlobalClass globalClass;
    Shared_Preference preference;
    TextView tv_back;
    ProgressDialog pd;
    ArrayList<HashMap<String,String>> cityList;
    ArrayList<HashMap<String,String>> complexList;
    ArrayList<HashMap<String,String>> blockList;
    ArrayList<HashMap<String,String>>flatList;
    ArrayList<String> array1,array2,array3,array4;
    ArrayAdapter<String> dataAdapter1,dataadpter2,dataadpater3,dataadapter4,dataadapter5;
    Spinner edt_floor_no, edt_city_name, edt_complex_name, edt_block_name, edt_flat_no;
    String city_id,item,complex_id,block_name,flat_no,complex_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_multiple_complex);

        globalClass = (GlobalClass) getApplicationContext();
        preference = new Shared_Preference(AddMultipleFlat.this);
        preference.loadPrefrence();

        pd = new ProgressDialog(AddMultipleFlat.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));
        rel_next=findViewById(R.id.rel_next);
        edt_city_name=findViewById(R.id.edt_city_name);
       // edt_floor_no=findViewById(R.id.edt_floor_no);
        edt_complex_name=findViewById(R.id.edt_complex_name);
        edt_block_name=findViewById(R.id.edt_block_name);
        edt_flat_no=findViewById(R.id.edt_flat_no);
        avLoadingIndicatorView=findViewById(R.id.avi);
        rel_login=findViewById(R.id.rel_login);
        tv_back=findViewById(R.id.tv_back);

        cityList=new ArrayList<>();
        complexList=new ArrayList<>();
        blockList=new ArrayList<>();
        flatList=new ArrayList<>();


        if (globalClass.isNetworkAvailable()) {

            BrowseCity();

        } else {


        }
        tv_back.setOnClickListener(v -> {

            finish();

        });

        rel_next.setOnClickListener(v -> AddComplex());


        edt_city_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int position, long arg3) {

                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position !=0){
                    city_id = cityList.get(position-1).get("id");

                    BrowseComplex(city_id);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        edt_complex_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int position, long arg3) {

                String selectedItemText = (String) parent.getItemAtPosition(position);
                    if (position != 0) {
                        complex_id = complexList.get(position-1).get("id");
                        complex_name = complexList.get(position-1).get("name");

                        BrowseBlock(complex_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        edt_block_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int position, long arg3) {

                String selectedItemText = (String) parent.getItemAtPosition(position);

                if(position !=0){
                    block_name = blockList.get(position-1).get("block");

                    Toast.makeText(getApplicationContext(),block_name,Toast.LENGTH_LONG).show();

                    FlatComplex(complex_id,block_name);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        edt_flat_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int position, long arg3) {

                String selectedItemText = (String) parent.getItemAtPosition(position);

                if(position != 0){

                    flat_no = flatList.get(position-1).get("id");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


    }



    private void BrowseCity() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        cityList.clear();
        startAnim();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.city_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                stopAnim();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        Log.d("jobj", "" + jobj);
                        ArrayList<String> state_array = new ArrayList<String>();
                        state_array.add("Select State");

                        //array.add("Select Location");
                        JsonArray jarray = jobj.getAsJsonArray("data");
                        Log.d("jarray", "" + jarray.toString());
                         array1 = new ArrayList<>();
                        array1.add("City Name");
                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            //get the object

                            //JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            String id = jobj1.get("id").toString().replaceAll("\"", "");
                            String name = jobj1.get("name").toString().replaceAll("\"", "");
                            String is_active = jobj1.get("is_active").toString().replaceAll("\"", "");
                            String delete_flag = jobj1.get("delete_flag").toString().replaceAll("\"", "");
                            String state_id = jobj1.get("state_id").toString().replaceAll("\"", "");
                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("id", id);
                            map_ser.put("name", name);
                            map_ser.put("is_active", is_active);
                            map_ser.put("delete_flag", delete_flag);
                            map_ser.put("state_id", state_id);

                            cityList.add(map_ser);

                            array1.add(name);


                        }

                        dataAdapter1 = new ArrayAdapter(AddMultipleFlat.this, R.layout.item_spinner, R.id.tvCust, array1);
                        edt_city_name.setAdapter(dataAdapter1);
                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {



        };

        // Adding request to request queue
        VolleySingleton.getInstance(AddMultipleFlat.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void BrowseComplex(final String city_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        startAnim();
        complexList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.complex_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                stopAnim();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d("jobj", "" + jobj);

                    if(status.equals("1")) {
                        //array.add("Select Location");
                        JsonArray jarray = jobj.getAsJsonArray("data");
                        Log.d("jarray", "" + jarray.toString());
                         array2 = new ArrayList<>();
                        array2.add("Complex Name");
                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            //get the object

                            //JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            String id = jobj1.get("id").toString().replaceAll("\"", "");
                            String name = jobj1.get("name").toString().replaceAll("\"", "");

                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("id", id);
                            map_ser.put("name", name);

                            complexList.add(map_ser);

                            array2.add(name);


                        }

                        dataadpter2 = new ArrayAdapter(AddMultipleFlat.this, R.layout.item_spinner, R.id.tvCust, array2);
                        edt_complex_name.setAdapter(dataadpter2);

                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();



                params.put("city_id",city_id);
                Log.d(TAG, "city_id: "+params);

                return params;
            }

        };
        // Adding request to request queue
        GlobalClass.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));

    }

    private void BrowseBlock(final String complex_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        startAnim();
        blockList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.block_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                stopAnim();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d("jobj", "" + jobj);
                  if(status.equals("1")) {

                      //array.add("Select Location");
                      JsonArray jarray = jobj.getAsJsonArray("data");
                      Log.d("jarray", "" + jarray.toString());
                       array3 = new ArrayList<>();
                      array3.add("Block Name");
                      for (int i = 0; i < jarray.size(); i++) {
                          JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                          //get the object

                          //JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                          String block = jobj1.get("block").toString().replaceAll("\"", "");

                          HashMap<String, String> map_ser = new HashMap<>();


                          map_ser.put("block", block);
                          // map_ser.put("name", name);

                          blockList.add(map_ser);

                          array3.add(block);


                      }

                      dataadpater3 = new ArrayAdapter(AddMultipleFlat.this, R.layout.item_spinner, R.id.tvCust, array3);
                      edt_block_name.setAdapter(dataadpater3);

                  }
                  else {
                      TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                  }



                } catch (Exception e) {
                    TastyToast.makeText(getApplicationContext(), "DATA NOT FOUND", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();



                params.put("complex_id",complex_id);
                Log.d(TAG, "complex_id: "+params);

                return params;
            }

        };
        // Adding request to request queue
        GlobalClass.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));

    }

    private void FlatComplex(final String complex_id,final String block_name) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        startAnim();
        flatList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.flat_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "flat_list: " + response.toString());

                stopAnim();

                Gson gson = new Gson();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d("flat_list", "" + jobj);

                    if(status.equals("1")) {
                        //array.add("Select Location");
                        JsonArray all_data = jobj.getAsJsonArray("all_data");

                        array4 = new ArrayList<>();
                        array4.add("Flat Number");

                        for (int j = 0; j < all_data.size(); j++) {
                            JsonObject flat = all_data.get(j).getAsJsonObject();

                            String floor = flat.get("floor").getAsString().replaceAll("\"", "");
                            JsonArray data = flat.getAsJsonArray("data");


                            for (int i = 0; i < data.size(); i++) {
                                JsonObject jobj1 = data.get(i).getAsJsonObject();
                                //get the object

                                //JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                                String id = jobj1.get("id").toString().replaceAll("\"", "");
                                String flat_no = jobj1.get("flat_no").toString().replaceAll("\"", "");
                                String name = jobj1.get("name").toString().replaceAll("\"", "");
                                String mobile = jobj1.get("mobile").toString().replaceAll("\"", "");
                                String emailid = jobj1.get("emailid").toString().replaceAll("\"", "");

                                HashMap<String, String> map_ser = new HashMap<>();

                                map_ser.put("id", id);
                                map_ser.put("name", name);
                                map_ser.put("flat_no", flat_no);
                                map_ser.put("mobile", mobile);
                                map_ser.put("emailid", emailid);

                                flatList.add(map_ser);

                                array4.add(flat_no);

                            }

                        }

                        dataadapter4 = new ArrayAdapter(AddMultipleFlat.this,
                                R.layout.item_spinner, R.id.tvCust, array4);
                        edt_flat_no.setAdapter(dataadapter4);


                    } else {
                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                pd.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("complex_id",complex_id);
                params.put("block",block_name);

                Log.d(TAG, "city_id: "+params);

                return params;
            }

        };
        // Adding request to request queue
        GlobalClass.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));

    }

    private void FLoorNumber(final String complex_id,final String block_name) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        startAnim();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.registration_flat_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                stopAnim();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d("jobj", "" + jobj);

                    if(status.equals("1")) {
                        //array.add("Select Location");
                        JsonArray all_data = jobj.getAsJsonArray("all_data");

                        for (int j = 0; j < all_data.size(); j++) {

                            JsonObject flat = all_data.get(j).getAsJsonObject();
                            String floor = flat.get("floor").getAsString().replaceAll("\"", "");
                            JsonArray data = flat.getAsJsonArray("all_data");

                            Log.d("jarray", "" + flat.toString());
                            array4 = new ArrayList<>();
                            array4.add("Flat Number");
                            for (int i = 0; i < data.size(); i++) {
                                JsonObject jobj1 = data.get(i).getAsJsonObject();
                                //get the object

                                //JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                                String id = jobj1.get("id").toString().replaceAll("\"", "");
                                String flat_no = jobj1.get("flat_no").toString().replaceAll("\"", "");
                                String name = jobj1.get("name").toString().replaceAll("\"", "");
                                String mobile = jobj1.get("mobile").toString().replaceAll("\"", "");
                                String emailid = jobj1.get("emailid").toString().replaceAll("\"", "");

                                HashMap<String, String> map_ser = new HashMap<>();


                                map_ser.put("id", id);
                                map_ser.put("name", name);
                                map_ser.put("flat_no", flat_no);
                                map_ser.put("mobile", mobile);
                                map_ser.put("emailid", emailid);


                                array4.add(flat_no);


                            }

                            dataadapter5 = new ArrayAdapter(AddMultipleFlat.this,
                                    R.layout.item_spinner, R.id.tvCust, array4);
                            edt_flat_no.setAdapter(dataadapter4);
                        }

                    } else {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();



                params.put("complex_id",complex_id);
                params.put("block",block_name);
                Log.d(TAG, "city_id: "+params);

                return params;
            }

        };
        // Adding request to request queue
        GlobalClass.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));

    }

    void startAnim(){
        avLoadingIndicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }

    public void AddComplex(){
        String tag_string_req = "req_login";
        avLoadingIndicatorView.setVisibility(View.VISIBLE);

        startAnim();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.multi_registration_login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                stopAnim();


                Gson gson = new Gson();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d("jobj", "" + jobj);
                    if(status.equals("1")) {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                         finish();
                        Log.d(TAG, "onResponse: "+message);

                    } else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "DATA NOT FOUND", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();


                params.put("complex_id",complex_id);
                params.put("user_id",globalClass.getId());
                params.put("flat_id",flat_no);
                Log.d(TAG, "params "+params);

                return params;
            }

        };
        // Adding request to request queue
        VolleySingleton.getInstance(AddMultipleFlat.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }


}