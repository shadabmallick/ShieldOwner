package com.shield.resident.ui;

import android.app.Dialog;
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
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.Config;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;

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
    TextView ivr_number;

    LoaderDialog loaderDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interphone_screen);
        img_back = findViewById(R.id.img_back);
        ll_edit = findViewById(R.id.ll_edit);
        edit_ivr = findViewById(R.id.edit_ivr);
        ivr_number = findViewById(R.id.ivr_number);
        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

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
        final Dialog dialog = new Dialog(this, R.style.datepicker);
        dialog.setContentView(R.layout.add_phone);
        dialog.setCancelable(true);
        add_number=dialog.findViewById(R.id.add_number);
        ll_submit=dialog.findViewById(R.id.ll_submit);
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = add_number.getText().toString();

                if (number.trim().length() == 0){
                    TastyToast.makeText(getApplicationContext(),
                            "Enter a phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (number.trim().length() < 10){
                    TastyToast.makeText(getApplicationContext(),
                            "Enter a valid phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                EditNumber(number);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void IVR(){

        loaderDialog.show();

        String url = AppConfig.ivr_number;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("user_id",globalClass.getId());
        params.put("flat_id",globalClass.getFlat_no());

        cl.setSSLSocketFactory(new SSLSocketFactory(Config.getSslContext(),
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
                        loaderDialog.dismiss();
                        // dialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            String ivr_number_res = response.getString("ivr_number");

                            ivr_number.setText(ivr_number_res);

                            TastyToast.makeText(getApplicationContext(),
                                    message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

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
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

    public void EditNumber(final String number){

        loaderDialog.show();

        String url = AppConfig.update_ivr_number;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("user_id",globalClass.getId());
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
                    Log.d(TAG, "update_ivr_number- " + response.toString());
                    try {
                        loaderDialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {
                            IVR();

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
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG, "update_ivr_number- " +responseString);
            }
        });


    }

}
