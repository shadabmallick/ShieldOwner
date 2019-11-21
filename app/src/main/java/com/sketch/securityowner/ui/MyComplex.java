package com.sketch.securityowner.ui;

import android.content.Intent;
import android.os.Bundle;



import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sketch.securityowner.R;

public class MyComplex extends AppCompatActivity {
    ImageView img_back;
    LinearLayout ll_tenants,ll_add_multiple_complex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_complex);
        img_back = findViewById(R.id.img_back);
        ll_tenants = findViewById(R.id.ll_tenants);
        ll_add_multiple_complex = findViewById(R.id.ll_add_multiple_complex);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_tenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tenant = new Intent(getApplicationContext(), Activity_tenants.class);
                startActivity(tenant);
            }
        });
        ll_add_multiple_complex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tenant = new Intent(getApplicationContext(), AddMultipleFlat.class);
                startActivity(tenant);
            }
        });


    }
}