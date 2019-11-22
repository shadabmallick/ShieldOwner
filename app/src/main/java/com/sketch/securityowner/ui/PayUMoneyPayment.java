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
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.R;
import com.sketch.securityowner.dialogs.LoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class PayUMoneyPayment extends AppCompatActivity {


    PayUmoneySdkInitializer.PaymentParam.Builder builder;
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;
    private LoaderDialog loaderDialog;


    String amount, name, email, phone, prodname, txnid;
    private String merchantKey = "H0Afr8df";
    private String merchant_id = "5466792";

    private GlobalClass globalClass;

    private HashMap<String, String> hashMap;

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


        builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        globalClass = (GlobalClass) getApplicationContext();



        txnid = String.valueOf(System.currentTimeMillis());
        name = globalClass.getName();
        email = globalClass.getEmail().trim();
        //phone = globalClass.getPhone_number();
        phone = "8240253745";


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            hashMap = (HashMap<String, String>) bundle.getSerializable("hashmap");

            amount = hashMap.get("billing_amount");
        //    prodname = hashMap.get("invoice_no");
            prodname = "shield_invoice";

            startpay();
        }

        Log.d("TAG", "txnid "+txnid);
        Log.d("TAG", "name "+name);
        Log.d("TAG", "email "+email);
        Log.d("TAG", "phone "+phone);
        Log.d("TAG", "amount "+amount);
        Log.d("TAG", "prodname "+prodname);

    }

    public void startpay(){

        builder.setAmount(amount)              // Payment amount
                .setTxnId(txnid)               // Transaction ID
                .setPhone(phone)               // User Phone number
                .setProductName(prodname)      // Product Name or description
                .setFirstName(name)            // User First name
                .setEmail(email)               // User Email ID
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
                .setIsDebug(true)             // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantKey)          // Merchant key
                .setMerchantId(merchant_id);


        try {
            paymentParam = builder.build();
            // generateHashFromServer(paymentParam );
            getHashkey();

        } catch (Exception e) {
            Log.e(AppConfig.TAG, " error str: "+e.toString());


            TastyToast.makeText(getApplicationContext(),
                    "Phone number is invalid.",
                    TastyToast.LENGTH_LONG, TastyToast.WARNING);

        }

    }



    private void getHashkey() {

        loaderDialog.show();

        String url = AppConfig.payumoney_hash_for;

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

                        int status = response.getInt("status");

                        if (status == 1) {

                            String hash = response.optString("hash");

                            paymentParam.setMerchantHash(hash);

                            loaderDialog.dismiss();

                            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam,
                                    PayUMoneyPayment.this,
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

                loaderDialog.dismiss();
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
                    String paymentId = result.optString("paymentId");
                    String mode = result.optString("mode");
                    String bank_ref_num = result.optString("bank_ref_num");
                    String productinfo = result.optString("productinfo");


                    if (status.equals("success")){


                        TastyToast.makeText(getApplicationContext(),
                                "Payment successful",
                                TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


                        Intent intent = new Intent();
                        intent.putExtra("txnid", txnid);
                        intent.putExtra("amount", amount);
                        intent.putExtra("cardnum", cardnum);
                        intent.putExtra("paymentId", paymentId);
                        intent.putExtra("mode", mode);
                        intent.putExtra("bank_ref_num", bank_ref_num);
                        intent.putExtra("status", "y");
                        intent.putExtra("invoice_no", productinfo);
                        intent.putExtra("invoice_id", hashMap.get("invoice_id"));

                        setResult(Activity.RESULT_OK, intent);

                        finish();

                    }else {

                        TastyToast.makeText(getApplicationContext(),
                                "Payment failed. Try again.",
                                TastyToast.LENGTH_LONG, TastyToast.WARNING);

                        Intent intent = new Intent();
                        intent.putExtra("txn_id", txnid);
                        intent.putExtra("amount", amount);
                        intent.putExtra("cardnum", cardnum);
                        intent.putExtra("paymentId", paymentId);
                        intent.putExtra("mode", mode);
                        intent.putExtra("bank_ref_num", bank_ref_num);
                        intent.putExtra("invoice_no", productinfo);
                        intent.putExtra("status", "n");
                        intent.putExtra("invoice_id", hashMap.get("invoice_id"));

                        setResult(Activity.RESULT_OK, intent);

                        finish();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }else {

            TastyToast.makeText(getApplicationContext(),
                    "Payment failed. Try again.",
                    TastyToast.LENGTH_LONG, TastyToast.WARNING);

            finish();
        }
    }

}