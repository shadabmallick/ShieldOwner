package com.shield.resident.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.squareup.picasso.Picasso;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DialogQrCode extends Dialog {

    private Context context;
    private String qr_code, image;
    private TextView close,tv_code;
    private ImageView img_qr_code,img_share;
    private GlobalClass globalClass;
    private Shared_Preference preference;


    public DialogQrCode(@NonNull Context context,String qr_code,String image) {
        super(context);
        this.context=context;
        this.qr_code=qr_code;
        this.image=image;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qr_code);
        globalClass = (GlobalClass) context.getApplicationContext();
        preference = new Shared_Preference(context);
        preference.loadPrefrence();


        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setCancelable(false);

        close = findViewById(R.id.close);
        close=findViewById(R.id.close);
        img_qr_code=findViewById(R.id.img_qr_code);
        tv_code=findViewById(R.id.tv_code);
        img_share=findViewById(R.id.img_share);
        tv_code.setText(qr_code);


        String s1 = globalClass.getName() + " has added you as guest to visit "
                + "Resident-" + globalClass.getComplex_name()
                + ", Block-" + globalClass.getBlock()
                + " Flat-"+ globalClass.getFlat_name()+".";
        String s2 = "Your pass code - "+qr_code + ". Use this code at the gate," +
                " every time you enter in the society.";

        String send_msg = s1 + "\n\n" + s2;

        Picasso.with(context)
                .load(image) // web image url
                .into(img_qr_code);

        Log.d(TAG, "image: "+image);
        Uri uri =  Uri.parse(image);
        
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dismiss();
            }
        });

        img_share.setOnClickListener(v -> {
            //dismiss();
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) img_qr_code.getDrawable());
            Bitmap bitmap = bitmapDrawable .getBitmap();
            String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"", null);
            Uri bitmapUri = Uri.parse(bitmapPath);
            Intent shareIntent=new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, send_msg);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            context.startActivity(Intent.createChooser(shareIntent,"Share Via "));
        });


    }





    private void shareImage(Uri imagePath) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
        context.startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }

}
