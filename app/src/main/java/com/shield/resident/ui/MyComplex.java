package com.shield.resident.ui;

import android.content.Intent;
import android.os.Bundle;



import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyComplex extends AppCompatActivity {
    ImageView img_back;
    LinearLayout ll_tenants,ll_add_multiple_complex, linear_share;
    TextView user_name, block, address;
    CircleImageView profile_image;

    GlobalClass globalClass;
    Shared_Preference preference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_complex);

        globalClass = (GlobalClass) getApplicationContext();
        preference = new Shared_Preference(this);
        preference.loadPrefrence();

        user_name = findViewById(R.id.user_name);
        block = findViewById(R.id.block);
        address = findViewById(R.id.address);
        profile_image = findViewById(R.id.profile_image);


        user_name.setText(globalClass.getName());
        address.setText(globalClass.getComplex_name());
        block.setText(globalClass.getFlat_name()+" / "
                + globalClass.getBlock()+" "+"block");


        if (!globalClass.getProfil_pic().isEmpty()){
            Picasso.with(this)
                    .load(globalClass.getProfil_pic())
                    .fit().centerInside()
                    .error(R.mipmap.profile_image)
                    .placeholder(R.mipmap.profile_image)
                    .into(profile_image);
        }



        img_back = findViewById(R.id.img_back);
        ll_tenants = findViewById(R.id.ll_tenants);
        linear_share = findViewById(R.id.linear_share);
        ll_add_multiple_complex = findViewById(R.id.ll_add_multiple_complex);



        if (globalClass.getUser_type().equals("owner")
                || globalClass.getUser_type().equals("member")){
            ll_tenants.setVisibility(View.VISIBLE);
            ll_add_multiple_complex.setVisibility(View.VISIBLE);
        }else {
            ll_tenants.setVisibility(View.GONE);
            ll_add_multiple_complex.setVisibility(View.GONE);
        }




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


        linear_share.setOnClickListener(v -> {

            String url = "https://play.google.com/store/apps/details?id="
                    + MyComplex.this.getPackageName();

            String message = globalClass.getName() + " "   ;


            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Share Via ");
            startActivity(shareIntent);

        });


    }
}