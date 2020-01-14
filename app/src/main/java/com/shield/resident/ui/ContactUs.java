package com.shield.resident.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ContactUs extends AppCompatActivity {
    ImageView img_back;
    LoaderDialog loaderDialog;
    TextView tv_complex_name, tv_complex_address, tv_complex_email, tv_complex_phone, tv_send;
    EditText edt_message;


    GlobalClass globalClass;
    Shared_Preference prefManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        img_back=findViewById(R.id.img_back);
        tv_complex_name=findViewById(R.id.tv_complex_name);
        tv_complex_address=findViewById(R.id.tv_complex_address);
        tv_complex_email=findViewById(R.id.tv_complex_email);
        tv_complex_phone=findViewById(R.id.tv_complex_phone);
        edt_message=findViewById(R.id.edt_message);
        tv_send=findViewById(R.id.tv_send);

        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        img_back.setOnClickListener(v -> finish());

        tv_send.setOnClickListener(v -> {

            if (edt_message.getText().toString().trim().length() > 0){
                addContactMessage(edt_message.getText().toString());
            }else {
                TastyToast.makeText(getApplicationContext(),
                        "Type a message.",
                        TastyToast.LENGTH_LONG, TastyToast.INFO);
            }
        });

        getComplexDetails();
    }

    public void getComplexDetails(){

        loaderDialog.show();

        String url = AppConfig.complex_details;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("complex_id",globalClass.getComplex_id());

        //Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 15 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "complex_details- " + response.toString());
                    try {
                        loaderDialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");
                        if (status == 1) {
                            JSONObject data = response.getJSONObject("data");

                            tv_complex_name.setText(data.optString("name"));
                            tv_complex_address.setText(data.optString("address"));
                            tv_complex_email.setText(data.optString("emailid"));
                            tv_complex_phone.setText(data.optString("mobile"));

                        } else {
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loaderDialog.dismiss();
            }
        });


    }


    public void addContactMessage(String message){

        loaderDialog.show();

        String url = AppConfig.add_contact;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("complex_id",globalClass.getComplex_id());
        params.put("flat_id",globalClass.getFlat_id());
        params.put("user_id",globalClass.getId());
        params.put("content",message);

        //Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 15 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "add_contact- " + response.toString());
                    try {
                        loaderDialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");
                        if (status == 1) {
                            TastyToast.makeText(getApplicationContext(),
                                    message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            finish();

                        } else {
                            TastyToast.makeText(getApplicationContext(),
                                    message, TastyToast.LENGTH_LONG, TastyToast.ERROR);
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
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loaderDialog.dismiss();
            }
        });


    }
}
