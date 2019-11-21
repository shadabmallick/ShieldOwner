package com.sketch.securityowner.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.sketch.securityowner.Adapter.AdapterHelpList;
import com.sketch.securityowner.Adapter.AdapterOnvoiceList;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.sketch.securityowner.dialogs.LoaderDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class InvoiceList extends AppCompatActivity implements AdapterOnvoiceList.onItemClickListner {

    RecyclerView recycler_view;
    GlobalClass globalClass;
    AdapterOnvoiceList adapterOnvoiceList;
    ArrayList<HashMap<String,String>> invoice_list;
    TextView tv_address,billed,receipt,tv_due;
    Shared_Preference preference;
    Toolbar toolbar;
    LoaderDialog loaderDialog;

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
                    String invoice_total = jobj.get("invoice_total").getAsString().replaceAll("\"", "");
                    String total_paid = jobj.get("total_paid").getAsString().replaceAll("\"", "");
                    String due = jobj.get("due").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

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


                            invoice_list.add(map_ser);
                            Log.d(TAG, "cityList: "+invoice_list);




                        }

                        billed.setText(invoice_total);
                        receipt.setText(total_paid);
                        tv_due.setText(due);
                        tv_address.setText(globalClass.getComplex_name()+" "+globalClass.getFlat_name()+" "+globalClass.getBlock()+" "+"block");

                        adapterOnvoiceList = new AdapterOnvoiceList(InvoiceList.this,
                                invoice_list,InvoiceList.this);
                        recycler_view.setAdapter(adapterOnvoiceList);
                    }
                    else {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }


                    loaderDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(InvoiceList.this, "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("flat_id", "4");

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
        startActivity(intent);

    }



}
