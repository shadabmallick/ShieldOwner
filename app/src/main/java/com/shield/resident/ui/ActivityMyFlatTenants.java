package com.shield.resident.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Adapter.AdapterTenant;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class ActivityMyFlatTenants extends AppCompatActivity implements
        AdapterTenant.onItemClickListner{

    RelativeLayout rl_add_tenants;
    RecyclerView recycler_view;
    Dialog dialog;
    ImageView img_back;
    GlobalClass globalClass;
    Shared_Preference prefManager;
    AdapterTenant adapterTenant;
    ImageView image_member;
    EditText edit_phone, edit_name;
    LinearLayout ll_add_tenant, ll_delete_flat;
    CardView card_list_tenant, cardview_delete_flat;
    TextView tv_title;
    private final int PICK_IMAGE_CAMERA_STAFF = 7, PICK_IMAGE_GALLERY_STAFF = 6;
    String tenant_id;
    File p_image;
    ArrayList<HashMap<String,String>> tenantList;
    LoaderDialog loaderDialog;

    HashMap<String,String> hashMap_selected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tenant);

        rl_add_tenants=findViewById(R.id.rl_add_tenants);
        img_back=findViewById(R.id.img_back);
        recycler_view=findViewById(R.id.recycler_view);
        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);

        ll_add_tenant = findViewById(R.id.ll_add_tenant);
        card_list_tenant = findViewById(R.id.card_list_tenant);
        ll_delete_flat = findViewById(R.id.ll_delete_flat);
        cardview_delete_flat = findViewById(R.id.cardview_delete_flat);
        tv_title = findViewById(R.id.tv_title);
        card_list_tenant.setVisibility(View.GONE);


        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        tenantList=new ArrayList<>();
        recycler_view.setLayoutManager(new LinearLayoutManager(this));


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            String name = bundle.getString("name");
            tv_title.setText(name);

            hashMap_selected = (HashMap<String, String>) bundle.getSerializable("datas");

        }


        if (globalClass.getUser_type().equals("1")
                || globalClass.getUser_type().equals("4")){
            ll_add_tenant.setVisibility(View.VISIBLE);
        }else {
            ll_add_tenant.setVisibility(View.GONE);
        }


        ll_add_tenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTenantDialog();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });




        if (!globalClass.getFlat_id().equals(hashMap_selected.get("flat_id"))){
            cardview_delete_flat.setVisibility(View.GONE);
        }

        if (!hashMap_selected.get("user_type").equals("1")){
            cardview_delete_flat.setVisibility(View.GONE);
        }

        ll_delete_flat.setOnClickListener(v -> {

            deleteFlat(hashMap_selected.get("flat_id"));

        });


        TenantList();

    }


    public void addTenantDialog(){
        dialog = new Dialog(this, R.style.datepicker);
        dialog.setContentView(R.layout.tenants_info);
        ImageView img_edit=dialog.findViewById(R.id.img_edit_member);
        image_member=dialog.findViewById(R.id.image_member);
        edit_name=dialog.findViewById(R.id.edit_name);
        edit_phone=dialog.findViewById(R.id.edit_phone);


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        LinearLayout ll_save=dialog.findViewById(R.id.ll_save);

        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= edit_name.getText().toString();
                String phone= edit_phone.getText().toString();

                if (name.trim().length() == 0){
                    TastyToast.makeText(ActivityMyFlatTenants.this,
                            "Enter tenant name",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (phone.trim().length() == 0){
                    TastyToast.makeText(ActivityMyFlatTenants.this,
                            "Enter tenant phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (phone.trim().length() < 10){
                    TastyToast.makeText(ActivityMyFlatTenants.this,
                            "Enter 10-digit phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }


                AddTenant(name,phone);

            }
        });

        dialog.show();

    }


    private void TenantList() {
        // Tag used to cancel the request
        tenantList.clear();
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.tenant_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                loaderDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");

                    if(status.equals("1")) {

                        JSONArray jarray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            //get the object

                            String tenant_id = jobj1.optString("tenant_id");
                            String tenant_name = jobj1.optString("tenant_name");
                            String profile_pic = jobj1.optString("profile_pic");
                            String tenant_mobile = jobj1.optString("tenant_mobile");

                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("tenant_id", tenant_id);
                            map_ser.put("tenant_name", tenant_name);
                            map_ser.put("profile_pic", profile_pic);
                            map_ser.put("tenant_mobile", tenant_mobile);

                            tenantList.add(map_ser);

                        }

                        adapterTenant = new AdapterTenant(ActivityMyFlatTenants.this,
                                tenantList, ActivityMyFlatTenants.this);
                        recycler_view.setAdapter(adapterTenant);

                        if (tenantList.size() > 0){
                            card_list_tenant.setVisibility(View.VISIBLE);
                        }

                    } else {

                        TastyToast.makeText(getApplicationContext(), message,
                                TastyToast.LENGTH_LONG, TastyToast.INFO);

                        adapterTenant = new AdapterTenant(ActivityMyFlatTenants.this,
                                tenantList, ActivityMyFlatTenants.this);
                        recycler_view.setAdapter(adapterTenant);

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
                Map<String, String> params = new HashMap<>();

                params.put("user_id", globalClass.getId());
                params.put("flat_id", globalClass.getFlat_id());

                Log.d(TAG, "getParams: "+params);
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(ActivityMyFlatTenants.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMyFlatTenants.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA_STAFF);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY_STAFF);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(ActivityMyFlatTenants.this, "Camera Permission error",
                        Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ActivityMyFlatTenants.this, "Camera Permission error",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_GALLERY_STAFF
                && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri uri = data.getData();

            //p_image = new File(getRealPathFromURI(uri));
            //Log.d(TAG, "image = " + p_image);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image_member.setImageBitmap(bitmap);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);


                String path = Environment.getExternalStorageDirectory() + File.separator;
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {
                    p_image = file;
                    Log.d(TAG, "image: " + p_image);

                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_IMAGE_CAMERA_STAFF && resultCode == RESULT_OK) {

            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);


                Log.d(TAG, "bitmap: " + bitmap);
                image_member.setImageBitmap(bitmap);

                String path = Environment.getExternalStorageDirectory() + File.separator;
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {
                    p_image = file;
                    Log.d(TAG, "image: " + p_image);

                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void AddTenant(final String name,final String phone){

        loaderDialog.show();

        String url = AppConfig.add_tenant;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("user_id", globalClass.getId());
        params.put("flat_no", globalClass.getFlat_id());
        params.put("complex_id", globalClass.getComplex_id());
        params.put("tenant_name", name);
        params.put("tenant_mobile", phone);

        try{

            params.put("profileImage", p_image);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


       // Log.d(TAG , "URL "+url);
       // Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "add_tenant- " + response.toString());
                    try {
                        loaderDialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            dialog.dismiss();

                            TastyToast.makeText(getApplicationContext(),
                                    message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            TenantList();

                        } else {
                            TastyToast.makeText(getApplicationContext(),
                                    message, TastyToast.LENGTH_LONG, TastyToast.WARNING);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loaderDialog.dismiss();

                Log.d(TAG, "add tenant - " + responseString);
            }
        });


    }

    public void DeleteTenant(final String tenant_id){

        loaderDialog.show();

        String url = AppConfig.delete_tenant;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("tenant_id", tenant_id);
        params.put("flat_id", globalClass.getFlat_id());


       // Log.d(TAG , "URL "+url);
       // Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 15 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "delete_tenant- " + response.toString());
                    try {
                        loaderDialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {
                            TenantList();
                            TastyToast.makeText(getApplicationContext(),
                                    message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        } else {
                            TastyToast.makeText(getApplicationContext(),
                                    message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        TastyToast.makeText(getApplicationContext(),
                                "Error Connection", TastyToast.LENGTH_LONG,
                                TastyToast.ERROR);
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

    @Override
    public void onItemClick(String s) {
        tenant_id=s;
        DeleteTenant(tenant_id);
    }


    private void deleteFlat(String flat_id) {
        // Tag used to cancel the request
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.delete_own_property, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                loaderDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");

                    if(status.equals("1")) {



                    } else {

                        TastyToast.makeText(getApplicationContext(), message,
                                TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
                Map<String, String> params = new HashMap<>();

                params.put("user_id", globalClass.getId());
                params.put("flat_id", flat_id);
                params.put("user_type", globalClass.getUser_type());

                Log.d(TAG, "getParams: "+params);

                return params;
            }


        };

        // Adding request to request queue
        VolleySingleton.getInstance(ActivityMyFlatTenants.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }



}
