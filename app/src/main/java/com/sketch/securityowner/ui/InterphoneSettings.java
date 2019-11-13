package com.sketch.securityowner.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sketch.securityowner.R;

public class InterphoneSettings extends AppCompatActivity {
    ImageView img_back;
    LinearLayout ll_edit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_commerce);
        img_back = findViewById(R.id.img_back);
        ll_edit = findViewById(R.id.ll_edit);
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
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_phone);
        dialog.setCancelable(false);

        dialog.show();
    }
}
