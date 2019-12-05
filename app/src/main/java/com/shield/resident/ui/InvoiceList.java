package com.shield.resident.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.shield.resident.Adapter.AdapterOnvoiceList;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class InvoiceList extends AppCompatActivity implements AdapterOnvoiceList.onItemClickListner {

    RecyclerView recycler_view;
    GlobalClass globalClass;
    AdapterOnvoiceList adapterOnvoiceList;
    ArrayList<HashMap<String,String>> invoice_list;
    TextView tv_address,billed,receipt,tv_due;
    Shared_Preference preference;
    Toolbar toolbar;
    LoaderDialog loaderDialog;
    LinearLayout linear_nodata;

    private static final int REQUEST_FOR_PAYMENT = 2323;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_list);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invoice");

        tv_address=findViewById(R.id.tv_address);
        recycler_view=findViewById(R.id.recycler_view);
        billed=findViewById(R.id.billed);
        receipt=findViewById(R.id.receipt);
        tv_due=findViewById(R.id.due);
        linear_nodata=findViewById(R.id.linear_nodata);
        linear_nodata.setVisibility(View.GONE);

        invoice_list=new ArrayList<>();

        globalClass = (GlobalClass) getApplicationContext();
        preference = new Shared_Preference(InvoiceList.this);
        preference.loadPrefrence();

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        InvoiceList();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void InvoiceList() {
        // Tag used to cancel the request
        invoice_list.clear();
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.inovice_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                Gson gson = new Gson();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);

                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    if(status.equals("1")) {

                        String invoice_total = jobj.get("invoice_total").getAsString().replaceAll("\"", "");
                        String total_paid = jobj.get("total_paid").getAsString().replaceAll("\"", "");
                        String due = jobj.get("due").getAsString().replaceAll("\"", "");

                        JsonArray jarray = jobj.getAsJsonArray("data");

                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            //get the object

                            String id = jobj1.get("id").toString().replaceAll("\"", "");
                            String invoice_id = jobj1.get("invoice_id").toString().replaceAll("\"", "");
                            String invoice_no = jobj1.get("invoice_no").toString().replaceAll("\"", "");
                            String complex_id = jobj1.get("complex_id").toString().replaceAll("\"", "");
                            String flat_id = jobj1.get("flat_id").toString().replaceAll("\"", "");
                            String billing_amount = jobj1.get("billing_amount").toString().replaceAll("\"", "");
                            String billing_month = jobj1.get("billing_month").toString().replaceAll("\"", "");
                            String billing_year = jobj1.get("billing_year").toString().replaceAll("\"", "");
                            String invoice_link = jobj1.get("invoice_link").toString().replaceAll("\"", "");
                            String date = jobj1.get("date").toString().replaceAll("\"", "");
                            String is_active = jobj1.get("is_active").toString().replaceAll("\"", "");

                            String invoicestatus = jobj1.get("status").toString().replaceAll("\"", "");
                            String delete_flag = jobj1.get("delete_flag").toString().replaceAll("\"", "");
                            String invoice_name = jobj1.get("invoice_name").toString().replaceAll("\"", "");
                            String note = jobj1.get("note").toString().replaceAll("\"", "");
                            String status1 = jobj1.get("status").toString().replaceAll("\"", "");
                            String date_after = formateDateFromstring("yyyy-MM-dd", "dd, MMM yyyy", date);

                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("id", id);
                            map_ser.put("invoice_id", invoice_id);
                            map_ser.put("invoice_no", invoice_no);
                            map_ser.put("complex_id", complex_id);
                            map_ser.put("flat_id", flat_id);
                            map_ser.put("billing_amount", billing_amount);
                            map_ser.put("billing_month", billing_month);
                            map_ser.put("billing_year", billing_year);
                            map_ser.put("invoice_link", invoice_link);
                            map_ser.put("date", date_after);
                            map_ser.put("is_active", is_active);
                            map_ser.put("invoicestatus", invoicestatus);
                            map_ser.put("delete_flag", delete_flag);
                            map_ser.put("invoice_name", invoice_name);
                            map_ser.put("note", note);
                            map_ser.put("status", status1);

                            invoice_list.add(map_ser);

                        }

                        billed.setText(invoice_total);
                        receipt.setText(total_paid);
                        tv_due.setText(due);
                        tv_address.setText(globalClass.getComplex_name()
                                +"/"+globalClass.getFlat_name()
                                +"/"+globalClass.getBlock()+" "+"block");

                        adapterOnvoiceList = new AdapterOnvoiceList(InvoiceList.this,
                                invoice_list,InvoiceList.this);
                        recycler_view.setAdapter(adapterOnvoiceList);

                    } else {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                    if (invoice_list.size() == 0){
                        linear_nodata.setVisibility(View.VISIBLE);
                    }

                    loaderDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    if (invoice_list.size() == 0){
                        linear_nodata.setVisibility(View.VISIBLE);
                    }

                    loaderDialog.dismiss();
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("flat_id", globalClass.getFlat_no());

                Log.d(TAG, "getParams: "+params);

                return params;
            }


        };

        // Adding request to request queue
        VolleySingleton.getInstance(InvoiceList.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.d(TAG, "formateDateFromstring: ");
        }

        return outputDate;

    }

    @Override
    public void onItemClick(String url) {

        Intent show_invoice=new Intent(getApplicationContext(), ShowInvoice.class);
        show_invoice.putExtra("url",url);
        Log.d(TAG, "onItemClick: "+url);
        startActivity(show_invoice);

    }


    @Override
    public void onClickForPay(HashMap<String, String> hashMap) {

        Intent intent = new Intent(InvoiceList.this, PayUMoneyPayment.class);
        intent.putExtra("hashmap", hashMap);
        startActivityForResult(intent, REQUEST_FOR_PAYMENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_FOR_PAYMENT  && resultCode == RESULT_OK) {

            String txnid = data.getStringExtra("txnid");
            String amount = data.getStringExtra("amount");
            String cardnum = data.getStringExtra("cardnum");
            String paymentId = data.getStringExtra("paymentId");
            String mode = data.getStringExtra("mode");
            String bank_ref_num = data.getStringExtra("bank_ref_num");
            String status = data.getStringExtra("status");
            String invoice_no = data.getStringExtra("invoice_no");
            String invoice_id = data.getStringExtra("invoice_id");
            String id = data.getStringExtra("id");

            finalPaymentReceive2(txnid, amount, cardnum, paymentId,
                    mode, bank_ref_num, status, invoice_no,
                    invoice_id, id);


        }
    }


    private void finalPaymentReceive2(String txnid, String amount, String cardnum,
                                      String paymentId, String mode, String bank_ref_num,
                                      String status, String invoice_no,
                                      String invoice_id, String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        loaderDialog.show();

        final Map<String, String> params = new HashMap<>();

        params.put("txnid", txnid);
        params.put("cardnum", cardnum);
        params.put("bank_ref_num", bank_ref_num);
        params.put("amount", amount);
        params.put("paymentId", paymentId);
        params.put("mode", mode);
        params.put("status", status);
        params.put("user_id", globalClass.getId());
        params.put("invoice_id", invoice_id);
        params.put("firstname", globalClass.getName());
        params.put("email", globalClass.getEmail());
        params.put("phone", globalClass.getPhone_number());
        params.put("flat_id", globalClass.getFlat_no());
        params.put("complex_id", globalClass.getComplex_id());
        params.put("id", id);


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.payment_receive, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "payment_receive RESPONSE: " +response);

                loaderDialog.dismiss();
                try {

                    JSONObject object = new JSONObject(response);

                    int status = object.optInt("status");
                    String message = object.optString("message");

                    if (status == 1){

                        InvoiceList();

                        TastyToast.makeText(InvoiceList.this, message,
                                TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }else {

                        TastyToast.makeText(InvoiceList.this, message,
                                TastyToast.LENGTH_LONG, TastyToast.ERROR);

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

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        VolleySingleton.getInstance(InvoiceList.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }


}
