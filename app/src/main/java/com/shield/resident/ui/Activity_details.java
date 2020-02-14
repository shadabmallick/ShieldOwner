package com.shield.resident.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.Config;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;
import com.shield.resident.model.ActivityChild;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

public class Activity_details extends AppCompatActivity {

    String TAG="Activity Details";

    RelativeLayout rl_top, rl_call_visitor;
    ImageView img_back, iv_profile, iv_vendor_image, iv_visitor_type, img_delete;
    TextView tv_name, tv_date_time, tv_status, visitor_type_name,
            tv_vendor_name, tv_message, tv_txt_stayOvernight;
    LinearLayout ll_call_security, ll_generate_passcode, ll_vendors, ll_stay_over_night;

    ActivityChild activityChild;

    GlobalClass globalClass;

    LoaderDialog loaderDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();

    }


    private void initViews(){

        rl_top=findViewById(R.id.rl_top);
        img_back=findViewById(R.id.img_back);
        rl_call_visitor=findViewById(R.id.rl_call_visitor);
        iv_profile=findViewById(R.id.iv_profile);
        iv_vendor_image=findViewById(R.id.iv_vendor_image);
        tv_name=findViewById(R.id.tv_name);
        tv_date_time=findViewById(R.id.tv_date_time);
        tv_status=findViewById(R.id.tv_status);
        visitor_type_name=findViewById(R.id.visitor_type_name);
        tv_vendor_name=findViewById(R.id.tv_vendor_name);
        ll_call_security=findViewById(R.id.ll_call_security);
        ll_generate_passcode=findViewById(R.id.ll_generate_passcode);
        ll_vendors=findViewById(R.id.ll_vendors);
        iv_visitor_type=findViewById(R.id.iv_visitor_type);
        tv_message=findViewById(R.id.tv_message);
        img_delete=findViewById(R.id.img_delete);
        ll_stay_over_night=findViewById(R.id.ll_stay_over_night);
        tv_txt_stayOvernight=findViewById(R.id.tv_txt_stayOvernight);

        ll_vendors.setVisibility(View.GONE);
        ll_stay_over_night.setVisibility(View.GONE);

        globalClass = (GlobalClass) getApplicationContext();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            activityChild = (ActivityChild) bundle.getSerializable("info");

            tv_name.setText(activityChild.getName());

            //// status check ...
            if (activityChild.getApprove_status().equals("new")){

                tv_status.setText("New Added");

                showTime(tv_date_time,
                        (activityChild.getVisiting_date() + " "
                                + activityChild.getVisiting_time()));

            }else if (activityChild.getApprove_status().equals("w")){

                tv_status.setText("Waiting for approval");

                showTime(tv_date_time,
                        (activityChild.getVisiting_date() + " "
                                + activityChild.getVisiting_time()));



            }else if (activityChild.getApprove_status().equals("y")){

                tv_status.setText("Approved");

                showTime(tv_date_time, activityChild.getActual_in_time());

                tv_message.setText("Approved by : "
                        +activityChild.getApprove_by());

            }else if (activityChild.getApprove_status().equals("n")){

                tv_status.setText("Rejected");

                showTime(tv_date_time, activityChild.getActual_in_time());


                tv_message.setText("Rejected by : "
                        +activityChild.getApprove_by());

            }else if (activityChild.getApprove_status().equals("l")){

                tv_status.setText("Leave At Gate");

                showTime(tv_date_time, activityChild.getActual_out_time());


                tv_message.setText("Leave at gate code : "
                        +activityChild.getLeave_at_gate_code());

            }else if(activityChild.getApprove_status().equals("sr")){


                ll_generate_passcode.setVisibility(View.GONE);

            }



            if (activityChild.getApprove_status().equals("y")
                || activityChild.getApprove_status().equals("n")
                    || activityChild.getApprove_status().equals("l")
                    || activityChild.getApprove_status().equals("sr")
            ){
                img_delete.setVisibility(View.GONE);
            }else {
                img_delete.setVisibility(View.VISIBLE);
            }



            //// visitor type ...
            if (activityChild.getVisitor_type().equals(AppConfig.guest)){

                visitor_type_name.setText("Guest");

                iv_visitor_type.setImageResource(R.mipmap.guest_white);

                ll_stay_over_night.setVisibility(View.VISIBLE);

            }else  if (activityChild.getVisitor_type().equals(AppConfig.delivery)){

                visitor_type_name.setText("Delivery");

                ll_vendors.setVisibility(View.VISIBLE);

                tv_vendor_name.setText(activityChild.getVendor_name());
                if (!activityChild.getVendor_image().isEmpty()){
                    Picasso.with(Activity_details.this)
                            .load(activityChild.getVendor_image()) // web image url
                            .into(iv_vendor_image);
                }

                iv_visitor_type.setImageResource(R.mipmap.deliveryman_white);

            }else  if (activityChild.getVisitor_type().equals(AppConfig.staff)){

                visitor_type_name.setText("Staff");

                //iv_visitor_type.setImageResource(R.mipmap.guest_white);

            } else  if (activityChild.getVisitor_type().equals(AppConfig.cab)){

                visitor_type_name.setText("Cab");

                ll_vendors.setVisibility(View.VISIBLE);

                tv_vendor_name.setText(activityChild.getVendor_name());
                if (!activityChild.getVendor_image().isEmpty()){
                    Picasso.with(Activity_details.this)
                            .load(activityChild.getVendor_image()) // web image url
                            .into(iv_vendor_image);
                }

                iv_visitor_type.setImageResource(R.mipmap.cab_white);

            }else  if (activityChild.getVisitor_type().equals(AppConfig.visiting_help)){

                visitor_type_name.setText("Visiting Help ("
                        + activityChild.getVisiting_help_cat()+")");

                iv_visitor_type.setImageResource(R.mipmap.visiting_help_white);


            }


            if (activityChild.getType().equals("master")){

                Log.d(TAG, "getpase > "+activityChild.getGetpass());
                Log.d(TAG, "Actual_out_time > "+activityChild.getActual_out_time());

                if (activityChild.getGetpass().equals("0")
                        && ((activityChild.getActual_out_time() == null)
                        || activityChild.getActual_out_time().equals("null")
                        || activityChild.getActual_out_time().equals(null))){

                    ll_generate_passcode.setVisibility(View.VISIBLE);

                }else {

                    ll_generate_passcode.setVisibility(View.GONE);

                    ll_stay_over_night.setVisibility(View.GONE);
                }

            }else {

                ll_generate_passcode.setVisibility(View.GONE);
            }



            if (!activityChild.getProfile_image().isEmpty()){
                Picasso.with(Activity_details.this)
                        .load(activityChild.getProfile_image()) // web image url
                        .fit().centerInside()
                        .error(R.mipmap.profile_image)
                        .placeholder(R.mipmap.profile_image)
                        .into(iv_profile);
            }


            if (!activityChild.getGuest_no_of_stay().isEmpty()
                && !activityChild.getGuest_type_of_stay().isEmpty()){
                tv_txt_stayOvernight.setText("Update stay overnight");

                tv_status.setText(activityChild.getGuest_no_of_stay()
                        + " "+activityChild.getGuest_type_of_stay()
                        + " stay");
            }


            if (activityChild.getSecurity_mobile() == null
                    || activityChild.getSecurity_mobile().isEmpty()){
                ll_call_security.setVisibility(View.GONE);
            }


        }


        rl_call_visitor.setOnClickListener(v -> {

            checkCallPermission(activityChild.getMobile());

        });

        ll_call_security.setOnClickListener(v -> {

            checkCallPermission(activityChild.getSecurity_mobile());
        });

        ll_generate_passcode.setOnClickListener(v -> {

            dialogGatePassGenerate();
        });

        img_back.setOnClickListener(v -> finish());

        img_delete.setOnClickListener(v -> {

            deleteActivity();

        });

        ll_stay_over_night.setOnClickListener(v -> {

            dialogOvernightStay();

        });
    }

    private void showTime(TextView textView, String sTime){

        try {

            DateFormat originalFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat =
                    new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);

            Date date = originalFormat.parse(sTime);

            String formattedDate = targetFormat.format(date);

            textView.setText(formattedDate);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //// call user ....

    private static final int REQUEST_PHONE_CALL = 1212;
    private void checkCallPermission(String number){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Activity_details.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Activity_details.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

            } else {
                callPhone(number);
            }
        } else {
            callPhone(number);
        }

    }

    private void callPhone(String number){

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));// Initiates the Intent
        startActivity(intent);

    }


    /// generate passcode ...
    Dialog dialog;
    File p_image;
    String mCurrentPhotoPath;
    private static final int CAMERA_REQUEST = 333;
    private static final int PICK_IMAGE_REQUEST = 454;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 222;
    ImageView iv_image;
    int clicked = 0;

    public void dialogGatePassGenerate(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_gatepass);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        EditText edt_description = dialog.findViewById(R.id.edt_description);
        ImageView iv_camera = dialog.findViewById(R.id.iv_camera);
        ImageView iv_gallery = dialog.findViewById(R.id.iv_gallery);
        iv_image = dialog.findViewById(R.id.iv_image);
        TextView close = dialog.findViewById(R.id.close);
        LinearLayout ll_submit = dialog.findViewById(R.id.ll_submit);

        dialog.show();

        close.setOnClickListener(v -> {
            dialog.dismiss();

        });

        iv_camera.setOnClickListener(v -> {

            clicked = CAMERA_REQUEST;

            checkPermission();

        });

        iv_gallery.setOnClickListener(v -> {

            clicked = PICK_IMAGE_REQUEST;

            checkPermission();

        });


        ll_submit.setOnClickListener(v -> {

            if (edt_description.getText().toString().trim().length() == 0){
                TastyToast.makeText(Activity_details.this,
                        "Enter description", TastyToast.LENGTH_LONG, TastyToast.INFO);

                return;
            }

            generatePasscode(edt_description.getText().toString());

        });

    }

    private void captureImage(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            dispatchTakePictureIntent();
        } else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    private void dispatchTakePictureIntent() {

        try {

            final String dir = android.os.Environment
                    .getExternalStorageDirectory() + "/Shield";

            File file = new File(dir);
            if (!file.exists())
                file.mkdir();


            String files = dir + "/pic_gatepass" +".jpg";
            File newfile = new File(files);

           /* if (newfile.exists()){
                newfile.delete();
            }*/

            p_image = newfile;

            Uri photoURI = FileProvider.getUriForFile(Activity_details.this,
                    "com.sketch.securityowner.provider", newfile);
            mCurrentPhotoPath = photoURI.toString();


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean checkPermission() {

        List<String> permissionsList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(Activity_details.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(Activity_details.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(Activity_details.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.CAMERA);
        }

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) Activity_details.this, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;

        } else {

            if (clicked == PICK_IMAGE_REQUEST){

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), PICK_IMAGE_REQUEST);

            }else {

                captureImage();
            }

        }


        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                        (permissions.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    checkPermission();
                } else {

                    checkPermission();
                }

        }
    }

    private void writeBitmap(Bitmap bitmap){

        final String dir = android.os.Environment
                .getExternalStorageDirectory() + "/Shield";

        File file = new File(dir);
        if (!file.exists())
            file.mkdir();


        String files = dir + "/pic_gatepass" +".jpg";
        File newfile = new File(files);
        p_image = newfile;
        try {

            iv_image.setImageBitmap(bitmap);

            newfile.delete();
            OutputStream outFile = null;
            try {

                p_image = newfile;

                outFile = new FileOutputStream(newfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outFile);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){

                Glide.with(Activity_details.this)
                        .clear(iv_image);

                Uri uri = Uri.parse(mCurrentPhotoPath);

                try {
                    getContentResolver().notifyChange(uri, null);
                    ContentResolver cr = getContentResolver();

                    Bitmap photo = android.provider.MediaStore.Images.Media.getBitmap(cr, uri);
                    photo = Config.RotateBitmap(photo, 90);
                    writeBitmap(photo);

                    iv_image.setImageBitmap(photo);

                }catch (Exception e){
                    e.printStackTrace();
                }
            } else{

                try {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    Log.d(AppConfig.TAG , "photo- " + photo);

                    Glide.with(Activity_details.this)
                            .clear(iv_image);

                    writeBitmap(photo);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_CANCELED) {

            p_image = null;

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                iv_image.setImageBitmap(bitmap);
                writeBitmap(bitmap);

               // p_image = new File(getRealPathFromURI(uri));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void generatePasscode(String desc){

        loaderDialog.show();

        String url = AppConfig.getpass_generate;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("complex_id", globalClass.getComplex_id());
        params.put("activity_id", activityChild.getActivity_id());
        params.put("description", desc);

        try{

            if (p_image != null){
                params.put("image", p_image);
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

       // Log.d(AppConfig.TAG , "new_visitor_add- " + url);
        Log.d(AppConfig.TAG , "new_visitor_add- " + params.toString());

        int DEFAULT_TIMEOUT = 15 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);

        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(AppConfig.TAG, "new_visitor_add- " + response.toString());

                if (response != null) {
                    try {

                        int status = response.optInt("status");
                        String message = response.optString("message");


                        if (status == 1) {

                            TastyToast.makeText(Activity_details.this,
                                    message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            finish();

                        }else {

                            TastyToast.makeText(Activity_details.this,
                                    message, TastyToast.LENGTH_LONG, TastyToast.WARNING);

                        }


                        loaderDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String res, Throwable t) {
                Log.d(AppConfig.TAG, "new_visitor_add- " + res);
                loaderDialog.dismiss();

                TastyToast.makeText(Activity_details.this,
                        "Server error. Try again.",
                        TastyToast.LENGTH_LONG, TastyToast.WARNING);

            }


        });

    }


    public void deleteActivity(){

        loaderDialog.show();

        String url = AppConfig.delete_activity;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("complex_id", globalClass.getComplex_id());
        params.put("activity_id", activityChild.getActivity_id());
        params.put("table", activityChild.getType());

       /* client.setSSLSocketFactory(
                new SSLSocketFactory(Config.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));*/

        Log.d(AppConfig.TAG , "delete_activity- " + url);
        Log.d(AppConfig.TAG , "delete_activity- " + params.toString());

        int DEFAULT_TIMEOUT = 15 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);

        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(AppConfig.TAG, "delete_activity- " + response.toString());

                if (response != null) {
                    try {

                        int status = response.optInt("status");
                        String message = response.optString("message");


                        if (status == 1) {

                            TastyToast.makeText(Activity_details.this,
                                    message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            finish();

                        }else {

                            TastyToast.makeText(Activity_details.this,
                                    message, TastyToast.LENGTH_LONG, TastyToast.WARNING);

                        }


                        loaderDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String res, Throwable t) {
                Log.d(AppConfig.TAG, "delete_activity- " + res);
                loaderDialog.dismiss();

                TastyToast.makeText(Activity_details.this,
                        "Server error. Try again.",
                        TastyToast.LENGTH_LONG, TastyToast.WARNING);

            }


        });

    }



    /// overnight stay ...
    String time_type = "";
    private void dialogOvernightStay(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_stay_overnight, null);
        alertDialog.setView(convertView);

        final AlertDialog show = alertDialog.show();

        EditText edt_how_much = convertView.findViewById(R.id.edt_how_much);
        Button btn_submit_stay = convertView.findViewById(R.id.btn_submit_stay);
        Spinner spinner_days = convertView.findViewById(R.id.spinner_days);

        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Day");
        spinnerArray.add("Week");
        spinnerArray.add("Month");
        spinnerArray.add("Year");

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray);
        spinner_days.setAdapter(spinnerArrayAdapter);



        spinner_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                time_type = spinnerArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_submit_stay.setOnClickListener(v -> {

            if (edt_how_much.getText().toString().trim().length() == 0){
                TastyToast.makeText(Activity_details.this,
                        "Enter value.",
                        TastyToast.LENGTH_LONG, TastyToast.INFO);

                return;
            }

            if (edt_how_much.getText().toString().equals("0")){
                TastyToast.makeText(Activity_details.this,
                        "0 value is not accepted.",
                        TastyToast.LENGTH_LONG, TastyToast.INFO);

                return;
            }


            show.dismiss();

            postStayValue(edt_how_much.getText().toString(), time_type);


        });

    }

    public void postStayValue(String time, String time_type){

        loaderDialog.show();

        String url = AppConfig.guest_stay_time;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("complex_id", globalClass.getComplex_id());
        params.put("activity_id", activityChild.getActivity_id());
        params.put("time", time);
        params.put("time_type", time_type);


        //Log.d(AppConfig.TAG , "new_visitor_add- " + url);
        Log.d(AppConfig.TAG , "guest_stay_time- " + params.toString());

        int DEFAULT_TIMEOUT = 15 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);

        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(AppConfig.TAG, "guest_stay_time- " + response.toString());

                if (response != null) {
                    try {

                        int status = response.optInt("status");
                        String message = response.optString("message");


                        if (status == 1) {

                            TastyToast.makeText(Activity_details.this,
                                    message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            finish();

                        }else {

                            TastyToast.makeText(Activity_details.this,
                                    message, TastyToast.LENGTH_LONG, TastyToast.WARNING);

                        }


                        loaderDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String res, Throwable t) {
                Log.d(AppConfig.TAG, "guest_stay_time- " + res);
                loaderDialog.dismiss();

                TastyToast.makeText(Activity_details.this,
                        "Server error. Try again.",
                        TastyToast.LENGTH_LONG, TastyToast.WARNING);

            }


        });

    }

}
