package com.shield.resident.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;


import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class RegisterUserDetails extends AppCompatActivity {
    String TAG="RegisterUserDetails";
    RelativeLayout rel_back,rel_next,rel_login;
    EditText edt_name,edt_phone_no,edt_email;
    GlobalClass globalClass;
    RadioButton radioOwner, radioTenant;
    RadioGroup radioGroup;
    LoaderDialog loaderDialog;

    String email,phone,name,deviceId, user_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_details);

        globalClass=(GlobalClass)getApplicationContext();

        rel_back=findViewById(R.id.rel_back);
        rel_next=findViewById(R.id.rel_next);
        rel_login=findViewById(R.id.rel_login);
        edt_name=findViewById(R.id.edt_name);
        edt_phone_no=findViewById(R.id.edt_phone_no);
        edt_email=findViewById(R.id.edt_email);
        radioOwner=findViewById(R.id.radioOwner);
        radioTenant=findViewById(R.id.radioTenant);
        radioGroup=findViewById(R.id.radioGroup);


        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");


        /*Bundle bundle = getIntent().getExtras();
        city_id = bundle.getString("city_id");
        block = bundle.getString("block");
        complex_id = bundle.getString("complex_id");
        complex_name = bundle.getString("complex_name");
        flat_no = bundle.getString("flat_no");
        flat_id = bundle.getString("flat_id");*/


        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        rel_back.setOnClickListener(v -> {

            finish();
        });


        rel_login.setOnClickListener(v -> {

            Intent intent = new Intent(RegisterUserDetails.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        });


        user_type = "owner";
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.radioOwner){
                user_type = "owner";
            }else if (checkedId == R.id.radioTenant){
                user_type = "tenant";
            }

        });



        rel_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                name = edt_name.getText().toString().trim();
                phone = edt_phone_no.getText().toString().trim();
                email = edt_email.getText().toString().trim();

                if (!globalClass.isNetworkAvailable()) {
                    TastyToast.makeText(getApplicationContext(),
                            "Check internet Connection",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    return;
                }

                if (name.isEmpty()) {
                    TastyToast.makeText(getApplicationContext(),
                            "Please enter name",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    return;
                }

                if (phone.isEmpty()) {
                    TastyToast.makeText(getApplicationContext(),
                            "Please enter phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    return;
                }

                if (phone.length() < 10) {
                    TastyToast.makeText(getApplicationContext(),
                            "Please enter 10-digit mobile number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    return;
                }

                if (email.isEmpty()) {
                    TastyToast.makeText(getApplicationContext(),
                            "Please enter email",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    return;
                }

                if (!isValidEmail(email)){
                    TastyToast.makeText(getApplicationContext(),
                            "Please enter valid email",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    return;
                }


                Intent intent = new Intent(RegisterUserDetails.this,
                        RegisterFlatDetails.class);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("user_type", user_type);
                startActivity(intent);


            }
        });
    }



    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}
