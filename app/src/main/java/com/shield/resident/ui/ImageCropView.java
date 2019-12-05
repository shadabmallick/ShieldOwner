package com.shield.resident.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shield.resident.R;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageCropView extends AppCompatActivity {

    CropImageView cropImageView;
    Button btn_cancel, btn_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_crop_view);

        cropImageView = findViewById(R.id.cropImageView);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_ok = findViewById(R.id.btn_ok);





    }


}
