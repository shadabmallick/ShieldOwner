package com.shield.resident.ui;

import android.os.Bundle;
import android.widget.ImageView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shield.resident.R;

public class ContactUs extends AppCompatActivity {
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        img_back=findViewById(R.id.img_back);

        img_back.setOnClickListener(v -> finish());
    }
}