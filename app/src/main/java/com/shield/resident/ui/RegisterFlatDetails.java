package com.shield.resident.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class RegisterFlatDetails extends AppCompatActivity {
    String TAG="RegisterFlatDetails";
    RelativeLayout rel_back,rel_next,rel_login;
    Spinner edt_city_name,edt_complex_name,edt_block_name,edt_flat_no;

    GlobalClass globalClass;
    Shared_Preference preference;
    LoaderDialog loaderDialog;

    ArrayList<HashMap<String,String>> cityList;
    ArrayList<HashMap<String,String>> complexList;
    ArrayList<HashMap<String,String>> blockList;
    ArrayList<HashMap<String,String>>flatList;
    ArrayList<String> array1,array2,array3,array4;
    ArrayAdapter<String> dataAdapter1,dataadpter2,dataadpater3,dataadapter4;


    String deviceId,city_id,complex_id,block_name,flat_no, flat_id,complex_name;
    String user_name, user_phone, user_email, user_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_flat_details);

        globalClass = (GlobalClass) getApplicationContext();
        preference = new Shared_Preference(RegisterFlatDetails.this);
        preference.loadPrefrence();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        rel_back=findViewById(R.id.rel_back);
        rel_next=findViewById(R.id.rel_next);
        edt_city_name=findViewById(R.id.edt_city_name);
        edt_complex_name=findViewById(R.id.edt_complex_name);
        edt_block_name=findViewById(R.id.edt_block_name);
        edt_flat_no=findViewById(R.id.edt_flat_no);
        rel_login=findViewById(R.id.rel_login);

        cityList=new ArrayList<>();
        complexList=new ArrayList<>();
        blockList=new ArrayList<>();
        flatList=new ArrayList<>();

        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            user_name = bundle.getString("name");
            user_phone = bundle.getString("phone");
            user_email = bundle.getString("email");
            user_type = bundle.getString("user_type");


        }


        if (globalClass.isNetworkAvailable()) {
            BrowseCity();
        }



        rel_back.setOnClickListener(v -> {

            finish();
        });

        rel_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterFlatDetails.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        edt_city_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int position, long arg3) {
                if(position !=0){
                    city_id = cityList.get(position-1).get("id");

                    BrowseComplex(city_id);

                }else {

                    city_id = null;

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
                if (position != 0) {
                    complex_id = complexList.get(position-1).get("id");
                    complex_name = complexList.get(position-1).get("name");

                    BrowseBlock(complex_id);
                }else {

                    complex_id = null;
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
                if(position !=0){
                    block_name = blockList.get(position-1).get("block");
                    Toast.makeText(getApplicationContext(),block_name,Toast.LENGTH_LONG).show();
                    getFlatList(complex_id, block_name);

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
                if(position !=0){
                    flat_no = flatList.get(position-1).get("flat_no");
                    flat_id = flatList.get(position-1).get("id");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        rel_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(city_id!=null && complex_id!=null && block_name!=null && flat_id!=null ){

                    checkRegister();

                } else {
                    TastyToast.makeText(getApplicationContext(),
                            "Please select the required field",
                            TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                }

            }
        });

    }



    private void BrowseCity() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        cityList.clear();
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.city_list, new Response.Listener<String>() {

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

                        ArrayList<String> state_array = new ArrayList<String>();
                        state_array.add("Select State");

                        JsonArray jarray = jobj.getAsJsonArray("data");
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

                        dataAdapter1 = new ArrayAdapter(RegisterFlatDetails.this,
                                R.layout.item_spinner, R.id.tvCust, array1);
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
                loaderDialog.dismiss();
            }
        }) {



        };

        // Adding request to request queue
        VolleySingleton.getInstance(RegisterFlatDetails.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void BrowseComplex(final String city_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        loaderDialog.show();
        complexList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.complex_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                loaderDialog.dismiss();
                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d("jobj", "" + jobj);

                    if(status.equals("1")) {

                        JsonArray jarray = jobj.getAsJsonArray("data");

                        //Log.d("jarray", "" + jarray.toString());

                        array2 = new ArrayList<>();
                        array2.add("Complex Name");
                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobj1 = jarray.get(i).getAsJsonObject();

                            String id = jobj1.get("id").toString().replaceAll("\"", "");
                            String name = jobj1.get("name").toString().replaceAll("\"", "");

                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("id", id);
                            map_ser.put("name", name);

                            complexList.add(map_ser);

                            array2.add(name);

                        }

                        dataadpter2 = new ArrayAdapter(RegisterFlatDetails.this, R.layout.item_spinner, R.id.tvCust, array2);
                        edt_complex_name.setAdapter(dataadpter2);

                    } else {
                        TastyToast.makeText(getApplicationContext(), message,
                                TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    }

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

        loaderDialog.show();
        blockList.clear();
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

                      dataadpater3 = new ArrayAdapter(RegisterFlatDetails.this,
                              R.layout.item_spinner, R.id.tvCust, array3);
                      edt_block_name.setAdapter(dataadpater3);

                  } else {
                      TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                  }

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

                params.put("complex_id",complex_id);
                Log.d(TAG, "complex_id: "+params);

                return params;
            }

        };
        // Adding request to request queue
        GlobalClass.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));

    }


    private void getFlatList(final String complex_id, final String block_name) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        loaderDialog.show();
        array4 = new ArrayList<>();
        flatList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.registration_flat_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                loaderDialog.dismiss();

                Gson gson = new Gson();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d("jobj", "" + jobj);

                    if(status.equals("1")) {
                        //array.add("Select Location");
                        JsonArray all_data = jobj.getAsJsonArray("all_data");

                        array4.add("Flat Number");

                        for (int j = 0; j < all_data.size(); j++) {

                            JsonObject flat = all_data.get(j).getAsJsonObject();
                            String floor = flat.get("floor").getAsString().replaceAll("\"", "");
                            JsonArray data = flat.getAsJsonArray("data");

                            for (int i = 0; i < data.size(); i++) {
                                JsonObject jobj1 = data.get(i).getAsJsonObject();
                                //get the object

                                String id = jobj1.get("id").toString().replaceAll("\"", "");
                                String flat_no = jobj1.get("flat_no").toString().replaceAll("\"", "");

                                HashMap<String, String> map_ser = new HashMap<>();

                                map_ser.put("id", id);
                                map_ser.put("flat_no", flat_no);

                                flatList.add(map_ser);
                                array4.add(flat_no + "(Floor : "+floor+")");

                            }

                        }

                        dataadapter4 = new ArrayAdapter(RegisterFlatDetails.this,
                                R.layout.item_spinner, R.id.tvCust, array4);
                        edt_flat_no.setAdapter(dataadapter4);

                    }

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

                params.put("complex_id", complex_id);
                params.put("block", block_name);
                params.put("type", user_type);

               // Log.d(TAG, "city_id: "+params);

                return params;
            }

        };
        // Adding request to request queue
        GlobalClass.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,
                10, 1.0f));

    }



    public void checkRegister(){
        String tag_string_req = "req_login";
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.registration, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "registration RESPONSE: " + response.toString());

                Gson gson = new Gson();

                loaderDialog.dismiss();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    if(status.equals("1")) {

                        //array.add("Select Location");

                       /* String otp = jobj.get("otp").getAsString()
                                .replaceAll("\"", "");

                        Intent otpScreen = new Intent(getApplicationContext(),OtpScreen.class);

                        otpScreen.putExtra("phone", phone);
                        otpScreen.putExtra("otp", otp);
                        otpScreen.putExtra("device_id", deviceId);
                        otpScreen.putExtra("fcm_token", fcm_token);
                        otpScreen.putExtra("from", "registration");

                        otpScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(otpScreen);*/


                        showDialog(message);

                    } else {
                        TastyToast.makeText(getApplicationContext(),
                                message, TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(),
                            "DATA NOT FOUND", TastyToast.LENGTH_LONG, TastyToast.WARNING);

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


                params.put("name", user_name);
                params.put("emailid", user_email);
                params.put("device_type", "android");
                params.put("device_id", deviceId);
                params.put("phone_Number", user_phone);
                params.put("complex_Number", complex_id);
                params.put("flat_id", flat_id);
                params.put("fcm_token", globalClass.getFcm_reg_token());
                params.put("city_id", city_id);
                params.put("complex_name", complex_name);
                params.put("user_type", user_type);

                Log.d(TAG, "params "+params);

                return params;
            }

        };
        // Adding request to request queue
        VolleySingleton.getInstance(RegisterFlatDetails.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));



    }


    private void showDialog(String text){

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.datepicker);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(text);
        builder.setIcon(R.mipmap.sand_clock);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(RegisterFlatDetails.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
