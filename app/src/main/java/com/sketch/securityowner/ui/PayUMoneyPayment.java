package com.sketch.securityowner.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PayUMoneyPayment extends AppCompatActivity {


    PayUmoneySdkInitializer.PaymentParam.Builder builder;
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;
    private ProgressDialog pDialog;


    String amount, name, email, phone, prodname, txnid;
    private String merchantKey = "ueXP5x4B";
    private String merchant_id = "6712375";


    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView mWebView = new WebView(this);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(100 * 1000 * 1000);
        mWebView.setWebChromeClient(new WebChromeClient());

        pDialog  = new ProgressDialog(PayUMoneyPayment.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Please wait...");



        builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            amount = bundle.getString("amount");
            name = bundle.getString("name");
            email = bundle.getString("email");
            phone = bundle.getString("phone");

            txnid = String.valueOf(System.currentTimeMillis());

            prodname = "product_name";

            startpay();
        }

    }

    public void startpay(){

        builder.setAmount(amount)                          // Payment amount
                .setTxnId(txnid)                     // Transaction ID
                .setPhone(phone)                   // User Phone number
                .setProductName(prodname)          // Product Name or description
                .setFirstName(name)            // User First name
                .setEmail(email)              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantKey)                        // Merchant key
                .setMerchantId(merchant_id);


        try {
            paymentParam = builder.build();
            // generateHashFromServer(paymentParam );
            getHashkey();

        } catch (Exception e) {
            Log.e(AppConfig.TAG, " error str: "+e.toString());

           /* Toasty.info(PayUMoneyPayment.this,
                    "Phone number is invalid.", Toast.LENGTH_LONG,
                    true).show();*/
        }

    }



    private void getHashkey() {

        pDialog.show();

        String url = "";

        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("txnid", txnid);
        params.put("amount", amount);
        params.put("productinfo", prodname);
        params.put("firstname", name);
        params.put("email", email);

        Log.d(AppConfig.TAG , "URL "+url);
        Log.d(AppConfig.TAG , "params "+params.toString());

        int DEFAULT_TIMEOUT = 20 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d("TAG", "PayumoneyHash- " + response.toString());
                    try {

                        JSONObject result = response.getJSONObject("result");

                        int status = result.getInt("status");

                        if (status == 1) {

                            String data = result.optString("data");

                            paymentParam.setMerchantHash(data);

                            pDialog.dismiss();

                            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, PayUMoneyPayment.this,
                                    R.style.AppTheme_default, true);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("TAG", "PayumoneyHash- " + errorResponse.toString());

                pDialog.dismiss();
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Log.d(ApiClient.TAG, "onActivityResult: " +data);

       // Log.e("data_new",""+data);
       // Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT
                && resultCode == RESULT_OK && data != null) {

            TransactionResponse transactionResponse =
                    data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if(transactionResponse.getTransactionStatus()
                        .equals( TransactionResponse.TransactionStatus.SUCCESSFUL )){
                    //Success Transaction

                } else{
                    //Failure Transaction

                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                Log.d(AppConfig.TAG, "payuResponse: "+payuResponse);


                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                 Log.d(AppConfig.TAG, "merchantResponse: "+merchantResponse);

                try {
                    JSONObject object = new JSONObject(payuResponse);

                    JSONObject result = object.getJSONObject("result");

                    String status = result.optString("status");
                    String txnid = result.optString("txnid");
                    String cardnum = result.optString("cardnum");

                    if (status.equals("success")){


                       /* Toasty.success(PayUMoneyPayment.this,
                                "Payment successful", Toast.LENGTH_SHORT,
                                true).show();*/

                        Intent intent = new Intent();
                        intent.putExtra("txn_id", txnid);
                        intent.putExtra("amount", amount);
                        setResult(Activity.RESULT_OK, intent);

                        finish();

                    }else {

                       /* Toasty.info(PayUMoneyPayment.this,
                                "Payment failed. Try again.", Toast.LENGTH_LONG,
                                true).show();*/

                        finish();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }else {

           /* Toasty.info(PayUMoneyPayment.this,
                    "Payment failed. Try again.", Toast.LENGTH_SHORT,
                    true).show();*/

            finish();
        }
    }

}