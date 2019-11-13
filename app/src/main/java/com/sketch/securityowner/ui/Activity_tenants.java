package com.sketch.securityowner.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sketch.securityowner.R;

public class Activity_tenants extends AppCompatActivity {
RelativeLayout rl_add_tenants;
    Dialog dialog;
    ImageView img_back;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teenant);
        rl_add_tenants=findViewById(R.id.rl_add_tenants);
        img_back=findViewById(R.id.img_back);


        rl_add_tenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberDialog();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }


    public void MemberDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.tenants_info);
        ImageView img_edit=dialog.findViewById(R.id.img_edit_member);


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // set the custom dialog components - text, image and button

        LinearLayout ll_save=dialog.findViewById(R.id.ll_save);

        // if button is clicked, close the custom dialog
        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();

    }

}
