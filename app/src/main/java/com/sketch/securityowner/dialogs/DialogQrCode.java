package com.sketch.securityowner.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.sketch.securityowner.ui.SettingActivity;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DialogQrCode extends Dialog {

    private Context context;
    private GlobalClass globalClass;
    private ProgressDialog progressDialog;
    RadioGroup radioSex;
    private int mYear, mMonth, mDay, mHour, mMinute,mSecond;
    private String qr_code, image,help_id;
    ArrayList<HashMap<String,String>> HelpList;
    private TextView close,tv_code,tv_time, date_picker;
    private RadioButton radio1, radio2;
    private EditText edit_name_cab, edit_phone_cab;
    ArrayList<String> array1;
    LinearLayout ll_hide;
    ImageView img_qr_code,img_share;
    private  boolean button1IsVisible = true;
    private Calendar myCalendar = Calendar.getInstance();
    Spinner spinner_help;

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

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setCancelable(false);
      //  globalClass = (GlobalClass) context.getApplicationContext();
       /* progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");*/

        close = findViewById(R.id.close);
        ll_hide = findViewById(R.id.ll_hide);

        close=findViewById(R.id.close);
        img_qr_code=findViewById(R.id.img_qr_code);
        date_picker=findViewById(R.id.date_picker);
        radio1=findViewById(R.id.radioMale);
        radio2=findViewById(R.id.radioFemale);
        radioSex=findViewById(R.id.radioSex);
        edit_name_cab=findViewById(R.id.edit_name);
        edit_phone_cab=findViewById(R.id.edit_phone);
        tv_time=findViewById(R.id.tv_time);
        tv_code=findViewById(R.id.tv_code);
        spinner_help=findViewById(R.id.spinner_help);
        ll_hide=findViewById(R.id.ll_hide);
        img_share=findViewById(R.id.img_share);
        tv_code.setText(qr_code);

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
            dismiss();
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) img_qr_code.getDrawable());
            Bitmap bitmap = bitmapDrawable .getBitmap();
            String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"", null);
            Uri bitmapUri = Uri.parse(bitmapPath);
            Intent shareIntent=new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, qr_code);
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
