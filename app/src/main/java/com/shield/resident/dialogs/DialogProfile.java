package com.shield.resident.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Adapter.ProfileFlatAdapter;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.shield.resident.ui.InvoiceList;
import com.shield.resident.ui.SettingActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DialogProfile extends Dialog implements ProfileFlatAdapter.ViewClickListener {

    private Context context;
    private GlobalClass globalClass;
    private Shared_Preference preference;

    private ImageView profile_image;
    private TextView user_name,block,address;
    private LinearLayout ll_setting,ll_invoice;
    private RecyclerView recycler_user_flats;


    private LoaderDialog loaderDialog;
    ArrayList<HashMap<String, String>> listOwnerFlat;

    public boolean is_clicked = false;

    public DialogProfile(@NonNull Context context,
                         ArrayList<HashMap<String, String>> listOwnerFlat) {
        super(context, R.style.datepicker);
        this.context = context;
        this.listOwnerFlat = listOwnerFlat;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dailog_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        WindowManager.LayoutParams wmlp = getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        wmlp.x = 100;   //x position
        wmlp.y = 300;   //y position



        globalClass = (GlobalClass) context.getApplicationContext();
        preference = new Shared_Preference(context);
        preference.loadPrefrence();

        loaderDialog = new LoaderDialog(context, android.R.style.Theme_Translucent,
                false, "");


        profile_image = findViewById(R.id.profile_image);
        ll_setting = findViewById(R.id.ll_setting);
        ll_invoice = findViewById(R.id.ll_invoice);
        user_name = findViewById(R.id.user_name);
        block = findViewById(R.id.block);
        address = findViewById(R.id.address);
        recycler_user_flats = findViewById(R.id.recycler_user_flats);


        if (!globalClass.getProfil_pic().isEmpty()){
            Picasso.with(context)
                    .load(globalClass.getProfil_pic())
                    .fit().centerInside()
                    .error(R.mipmap.profile_image)
                    .placeholder(R.mipmap.profile_image)
                    .into(profile_image);
        }

        user_name.setText(globalClass.getName());
        address.setText(globalClass.getComplex_name());
        block.setText(globalClass.getFlat_name()+" / "
                +globalClass.getBlock()+" "+"block");

        // if button is clicked, close the custom dialog
        ll_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                Intent setting=new Intent(context, SettingActivity.class);
                context.startActivity(setting);
            }
        });

        ll_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                Intent setting=new Intent(context, InvoiceList.class);
                context.startActivity(setting);

            }
        });




        if (globalClass.getUser_type().equals("1")
                || globalClass.getUser_type().equals("4")){
            if (globalClass.getPayment_system().equals("1")){
                ll_invoice.setVisibility(View.VISIBLE);
            }else {
                ll_invoice.setVisibility(View.GONE);
            }
        }else {
            if (globalClass.getOwner().equals("no") &&
                    globalClass.getPayment_system().equals("1")){
                ll_invoice.setVisibility(View.VISIBLE);
            }else {
                ll_invoice.setVisibility(View.GONE);
            }
        }


        recycler_user_flats.setLayoutManager(new LinearLayoutManager(context));
        setData();


    }




    private void setData(){

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        for (int i = 0; i < listOwnerFlat.size(); i++){

            HashMap<String, String> hashMap = listOwnerFlat.get(i);

            if (!hashMap.get("flat_id").equals(globalClass.getFlat_id())){

                list.add(hashMap);
            }

        }


        ProfileFlatAdapter profileFlatAdapter =
                new ProfileFlatAdapter(context, list);
        recycler_user_flats.setAdapter(profileFlatAdapter);
        profileFlatAdapter.setViewClickListener(this);

    }

    @Override
    public void onClicked(HashMap<String, String> hashMap) {

        is_clicked = true;

        globalClass.setFlat_id(hashMap.get("flat_id"));
        globalClass.setFlat_name(hashMap.get("flat_no"));
        globalClass.setUser_type(hashMap.get("user_type"));
        globalClass.setComplex_id(hashMap.get("complex_id"));
        globalClass.setComplex_name(hashMap.get("complex_name"));
        globalClass.setBlock(hashMap.get("block"));
        globalClass.setIs_tenant(hashMap.get("tenant"));
        globalClass.setPayment_system(hashMap.get("payment_system"));
        globalClass.setComplex_address(hashMap.get("address"));

        globalClass.setPayu_mkey(hashMap.get("payu_key"));
        globalClass.setPayu_mid(hashMap.get("payu_mid"));
        globalClass.setPayu_salt(hashMap.get("payu_salt"));
        globalClass.setParking_no(hashMap.get("parking_no"));
        globalClass.setParking_id(hashMap.get("parking_id"));


        preference.savePrefrence();
        preference.loadPrefrence();

        dismiss();

    }
}
