package com.sketch.securityowner.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sketch.securityowner.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class Activity_details extends AppCompatActivity {

    String TAG="Activity Details";

    RelativeLayout rl_top;
    ImageView img_back;
    String images;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        rl_top=findViewById(R.id.rl_top);
        img_back=findViewById(R.id.img_back);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

             images = bundle.getString("images");
            Log.d(TAG, "onCreate: "+images);
            String number = bundle.getString("number");
            String status = bundle.getString("status");
            String date_time = bundle.getString("date_time");
            String type = bundle.getString("type");

        }
        if (!images.isEmpty()){
            Picasso.with(Activity_details.this)
                    .load(images) // web image url
                    .fit().centerInside()
                    .rotate(90)                    //if you want to rotate by 90 degrees
                    .error(R.mipmap.profile_image)
                    .placeholder(R.mipmap.profile_image)
                    .into((Target) rl_top);
        }

        img_back.setOnClickListener(v -> finish());

    }
}
