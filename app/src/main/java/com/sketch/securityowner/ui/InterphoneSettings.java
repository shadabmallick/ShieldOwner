package com.sketch.securityowner.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.Config;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.R;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class InterphoneSettings extends AppCompatActivity {
    ImageView img_back,edit_ivr;
    LinearLayout ll_edit,ll_submit;
    EditText add_number;
    GlobalClass globalClass;
    Shared_Preference prefManager;
    ProgressDialog progressDialog;
    TextView ivr_number;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_commerce);
        img_back = findViewById(R.id.img_back);
        ll_edit = findViewById(R.id.ll_edit);
        edit_ivr = findViewById(R.id.edit_ivr);
        ivr_number = findViewById(R.id.ivr_number);
        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        IVR();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addPhone();
            }
        });
    }
    public void addPhone(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_phone);
        dialog.setCancelable(true);
        add_number=dialog.findViewById(R.id.add_number);
        ll_submit=dialog.findViewById(R.id.ll_submit);
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=add_number.getText().toString();
                EditNumber(number);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void IVR(){

        progressDialog.show();

        String url = AppConfig.ivr_number;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("flat_id",globalClass.getFlat_no());


        cl.setSSLSocketFactory(
                new SSLSocketFactory(Config.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));



        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "user_profile_pic_update- " + response.toString());
                    try {
                        progressDialog.dismiss();
                        // dialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");
                        String ivr_number_res = response.getString("ivr_number");

                        if (status == 1) {
                            ivr_number.setText(ivr_number_res);

                            // Log.d(TAG, "name: "+name)

                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            //  edit_name.setText(globalClass.getName());

                        } else {
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }
    public void EditNumber(final String number){

        progressDialog.show();

        String url = AppConfig.update_ivr_number;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("ivr_number",number);
        params.put("flat_id",globalClass.getFlat_no());


        cl.setSSLSocketFactory(
                new SSLSocketFactory(Config.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));



        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "user_profile_pic_update- " + response.toString());
                    try {
                        progressDialog.dismiss();
                        // dialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");
                      //  String ivr_number_res = response.getString("ivr_number");

                        if (status == 1) {
                            IVR();

                            // Log.d(TAG, "name: "+name)

                        //    TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            //  edit_name.setText(globalClass.getName());

                        } else {
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

}
