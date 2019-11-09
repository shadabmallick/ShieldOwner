package com.sketch.securityowner.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdsmdg.tastytoast.TastyToast;
import com.sketch.securityowner.Adapter.CarAdapter;
import com.sketch.securityowner.Adapter.FamilyAdapter;
import com.sketch.securityowner.Adapter.StaffAdapter;
import com.sketch.securityowner.Adapter.categoryAdapter;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.Config;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class SettingActivity extends AppCompatActivity implements categoryAdapter.onItemClickListner {
    String TAG="Setting";
    RecyclerView delivery_recycle,company_name_recycle,recyclerView,recyclerViewStaff,recyclerViewCar;
    ArrayList<String> Number;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerView.LayoutManager RecyclerViewLayoutManager1;
    RecyclerView.LayoutManager RecyclerViewLayoutManager2;
    RecyclerView.LayoutManager RecyclerViewLayoutManager3;
    RecyclerView.LayoutManager RecyclerViewLayoutManager4;
    FamilyAdapter RecyclerViewHorizontalAdapter;
    ArrayAdapter<String> dataAdapter1;
    Spinner spinner_help;
    categoryAdapter CategoryAdapter;
    StaffAdapter staffAdapter;
    ArrayList<String> array1;
    CarAdapter carAdapter;
    LinearLayoutManager HorizontalLayout ;
    LinearLayoutManager HorizontalLayout1 ;
    LinearLayoutManager HorizontalLayout2 ;
    LinearLayoutManager HorizontalLayout3 ;
    LinearLayoutManager HorizontalLayout4 ;
    File p_image;
     Dialog dialog;
    RadioButton radio1,radio2;
    private int mYear, mMonth, mDay, mHour, mMinute,mSecond;
    Calendar myCalendar = Calendar.getInstance();
    RadioGroup radioSex;
    Toolbar toolbar;
    TextView tv_time,date_picker,tv_animal,tv_medical,tv_thief,tv_threat,tv_lift,tv_id,tv_details_company,close,tv_details,app_setting,user_name,user_mobile,user_email;
    ImageView profile_image_edit,image_member;
    ProgressDialog pd;
    EditText edit_car_no,edit_parking_no,edit_name,edit_phone,edit_mail,edit_family_name,edit_family_phone;
    GlobalClass globalClass;
    Shared_Preference preference;
    CardView animal,fire,threat,lift,medical,theif;
    ArrayList<HashMap<String,String>> productDetaiils;
    ArrayList<HashMap<String,String>> Category;
    ArrayList<HashMap<String,String>> DeliveryList;
    ArrayList<HashMap<String,String>> HelpList;
    ArrayList<HashMap<String,String>> productDetaiils_sub;
    ArrayList<HashMap<String,String>> staffList;
    ImageView profile_image,img_cab,img_delivery,img_guest,img_help;
    AVLoadingIndicatorView avLoadingIndicatorView;
    ScrollView scroll_details,scroll_settings;
    RelativeLayout rel_profile,rel_middle_icon;
    ImageView edit;
    String category,help_id,date_web;
    TextView add_car,add_staff,add_member;
    EditText edit_name_cab,edit_phone_cab, edit_vehicle_no,tv_others;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private final int PICK_IMAGE_CAMERA_FAMILY = 3, PICK_IMAGE_GALLERY_FAMILY = 4;
    private final int PICK_IMAGE_CAMERA_CAR = 5, PICK_IMAGE_GALLERY_CAR = 6;
    LinearLayout ll_submit,ll_alram,ll_hide,ll_bell,button_E3,button_E1,ll_logout,ll_notification,ll_mycomplex,car1,button_activity,ll_about_us,ll_contact_us;
    private  boolean button1IsVisible = true;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
       // browseJob();
        initToolBar();



        browseJob();
        //

       // view1.setVisibility(View.VISIBLE);
        tv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll_details.setVisibility(View.VISIBLE);
                scroll_settings.setVisibility(View.GONE);
                rel_profile.setVisibility(View.VISIBLE);

                app_setting.setTypeface(null, Typeface.NORMAL); //only text style(only bold)

                tv_details.setTypeface(null, Typeface.BOLD); //only text style(only bold)

            }
        });
        app_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scroll_details.setVisibility(View.GONE);
                scroll_settings.setVisibility(View.VISIBLE);
                rel_profile.setVisibility(View.GONE);
                app_setting.setTypeface(null, Typeface.BOLD); //only text style(only bold)
                tv_details.setTypeface(null, Typeface.NORMAL); //only text style(only bold)

            }
        });

    }

    public void initToolBar() {
        productDetaiils=new ArrayList<>();
        staffList=new ArrayList<>();
        productDetaiils_sub=new ArrayList<>();
        DeliveryList=new ArrayList<>();
        HelpList=new ArrayList<>();

        Category=new ArrayList<>();
        globalClass = (GlobalClass) getApplicationContext();
        preference = new Shared_Preference(SettingActivity.this);
        preference.loadPrefrence();
        pd = new ProgressDialog(SettingActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        scroll_details =  findViewById(R.id.scroll_details);
        edit =  findViewById(R.id.edit);
        add_car =  findViewById(R.id.add_car);
        add_member =  findViewById(R.id.add_member);
        add_staff =  findViewById(R.id.add_staff);
        scroll_settings =  findViewById(R.id.scroll_settings);
        tv_details =findViewById(R.id.tv_details);
        rel_profile =findViewById(R.id.rel_profile);
        app_setting =  findViewById(R.id.app_details);
        profile_image =  findViewById(R.id.profile_image);
        user_email =  findViewById(R.id.user_email);
        user_name =  findViewById(R.id.user_name);
        ll_notification =  findViewById(R.id.ll_notification);
        user_mobile =  findViewById(R.id.user_mobile);
        ll_mycomplex =  findViewById(R.id.ll_mycomplex);
        avLoadingIndicatorView =  findViewById(R.id.avi);
        car1 =  findViewById(R.id.car1);
        rel_middle_icon =  findViewById(R.id.rel_middle_icon);
        button_activity=  findViewById(R.id.button_E);
        ll_about_us=  findViewById(R.id.ll_about_us);
        ll_contact_us=  findViewById(R.id.ll_contact_us);
        ll_logout=  findViewById(R.id.ll_logout);
        button_E1=  findViewById(R.id.button_E1);
        button_E3=  findViewById(R.id.button_E3);
        ll_bell=  findViewById(R.id.ll_bell);
        img_cab=  findViewById(R.id.img_cab);
        img_guest=  findViewById(R.id.img_guest);
        img_delivery=  findViewById(R.id.img_delivery);
        img_help=  findViewById(R.id.img_help);


        toolbar.setTitle("");
        recyclerView = (RecyclerView)findViewById(R.id.rec_family);
        recyclerViewStaff = (RecyclerView)findViewById(R.id.rec_staff);
        recyclerViewCar = (RecyclerView)findViewById(R.id.rec_car);

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerViewLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        RecyclerViewLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        RecyclerViewLayoutManager3 = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        recyclerViewStaff.setLayoutManager(RecyclerViewLayoutManager1);
        recyclerViewCar.setLayoutManager(RecyclerViewLayoutManager2);




        HorizontalLayout = new LinearLayoutManager(SettingActivity.this, LinearLayoutManager.HORIZONTAL, false);
        HorizontalLayout1 = new LinearLayoutManager(SettingActivity.this, LinearLayoutManager.HORIZONTAL, false);
        HorizontalLayout2 = new LinearLayoutManager(SettingActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerViewStaff.setLayoutManager(HorizontalLayout1);
        recyclerViewCar.setLayoutManager(HorizontalLayout2);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SettingActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileDialog();
            }
        });
        add_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaffDialog();
            }
        });
        img_cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCab();
                car1.setVisibility(View.GONE);

              //  dialog.dismiss();
            }
        });
        ll_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm();
                car1.setVisibility(View.GONE);

              //  dialog.dismiss();
            }
        });
        img_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AddDelivery();
                car1.setVisibility(View.GONE);


            }
        });
        img_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AddGuest();
                car1.setVisibility(View.GONE);


            }
        });
        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHelp();
                car1.setVisibility(View.GONE);


            }
        });
        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberDialog();
            }
        });
        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarDialog();
            }
        });
        ll_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),NotificationManager.class);
                notification.addFlags(FLAG_ACTIVITY_CLEAR_TOP|
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        ll_mycomplex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),MyComplex.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        button_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),Activity_activity.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        ll_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),AboutUs.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        ll_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),ContactUs.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        button_E1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),MainActivity.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        button_E3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),CommunityActivity.class);
                notification.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notification);
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logoutDailog();
            }
        });
        rel_middle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true)
                {

                    car1.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false)
                {
                   // car1.animate().alpha(1.0f);
                    car1.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
            }
        });

    }
   public void profileDialog(){
        dialog = new Dialog(this);
       dialog.setContentView(R.layout.edit_profile_dialog);
       profile_image_edit  =dialog.findViewById(R.id.profile_image_edit);
        ImageView img_edit=dialog.findViewById(R.id.img_edit);
       edit_name  =dialog.findViewById(R.id.edit_name);
       edit_phone=dialog.findViewById(R.id.edit_phone);
      edit_mail=dialog.findViewById(R.id.edit_mail);
       Picasso.with(SettingActivity.this)
               .load(globalClass.getProfil_pic()) // web image url
               .fit().centerInside()
               .rotate(90)                    //if you want to rotate by 90 degrees
               .error(R.mipmap.profile_image)
               .placeholder(R.mipmap.profile_image)
               .into(profile_image_edit);

         edit_name.setText(globalClass.getName());
         edit_phone.setText(globalClass.getPhone_number());
         edit_mail.setText(globalClass.getEmail());
       // set the custom dialog components - text, image and button
       img_edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectImage();
           }
       });
       LinearLayout ll_save=dialog.findViewById(R.id.ll_save);

       // if button is clicked, close the custom dialog
       ll_save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String name=edit_name.getText().toString();
               String phone=edit_phone.getText().toString();
               String email=edit_mail.getText().toString();


               updateProfile(name,phone,email);

                 // dialog.dismiss();
           }
       });

       dialog.show();
       if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           if (ContextCompat.checkSelfPermission(SettingActivity.this,
                   Manifest.permission.CAMERA)
                   == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SettingActivity.this,
                   Manifest.permission.WRITE_EXTERNAL_STORAGE)
                   == PackageManager.PERMISSION_GRANTED) {
           }
           else{
               if(checkForPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                       Manifest.permission.CAMERA}, 124)){

               }

           }
       }


   }
    public boolean checkForPermission(final String[] permissions, final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    Log.d("permisssion","not granted");


                    if (shouldShowRequestPermissionRationale(permissions[i])) {

                        Log.d("if","if");
                        permissionsNeeded.add(perm);

                    } else {
                        // add the request.
                        Log.d("else","else");
                        permissionsNeeded.add(perm);
                    }

                }
            }
        }

        if (permissionsNeeded.size() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // go ahead and request permissions
                requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), permRequestCode);
            }
            return false;
        } else {
            // no permission need to be asked so all good...we have them all.
            return true;
        }

    }
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(SettingActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(SettingActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void selectImageFamily() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA_FAMILY);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY_FAMILY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(SettingActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(SettingActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void selectImageCar() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA_CAR);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY_CAR);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(SettingActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(SettingActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            p_image = new File(getRealPathFromURI(uri));


            Log.d(TAG, "image = "+p_image);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                profile_image_edit.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {


            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }


            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

/*
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);*/

                Log.d(TAG, "bitmap: "+bitmap);

                profile_image_edit.setImageBitmap(bitmap);

                String path = Environment.getExternalStorageDirectory()+File.separator;
                // + File.separator
                //   + "Phoenix" + File.separator + "default";
               // f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {

                    p_image = file;

                    Log.d(TAG, "OutputStream: "+p_image);


                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            // iv_product_image.setImageBitmap(photo);
        }
       else if (requestCode == PICK_IMAGE_GALLERY_FAMILY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            p_image = new File(getRealPathFromURI(uri));


            Log.d(TAG, "image = "+p_image);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                image_member.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         else if (requestCode == PICK_IMAGE_CAMERA_CAR && resultCode == RESULT_OK) {


            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }


            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

/*
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);*/

                Log.d(TAG, "bitmap: "+bitmap);

                image_member.setImageBitmap(bitmap);

                String path = Environment.getExternalStorageDirectory()+File.separator;
                // + File.separator
                //   + "Phoenix" + File.separator + "default";
                // f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {

                    p_image = file;

                    Log.d(TAG, "OutputStream: "+p_image);


                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            // iv_product_image.setImageBitmap(photo);
        }

        else if (requestCode == PICK_IMAGE_GALLERY_CAR && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            p_image = new File(getRealPathFromURI(uri));


            Log.d(TAG, "image = "+p_image);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                image_member.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_CAMERA_FAMILY && resultCode == RESULT_OK) {


            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }


            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

/*
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);*/

                Log.d(TAG, "bitmap: "+bitmap);

                image_member.setImageBitmap(bitmap);

                String path = Environment.getExternalStorageDirectory()+File.separator;
                // + File.separator
                //   + "Phoenix" + File.separator + "default";
                // f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {

                    p_image = file;

                    Log.d(TAG, "OutputStream: "+p_image);


                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            // iv_product_image.setImageBitmap(photo);
        }





        if(globalClass.isNetworkAvailable()){
            // user_profile_pic_update_url();
        }else{
        TastyToast.makeText(getApplicationContext(),"Network Connection",TastyToast.LENGTH_LONG,TastyToast.WARNING).show();
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void StaffDialog(){
       final Dialog dialog = new Dialog(this);
       dialog.setContentView(R.layout.staff_nfo);


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
    public void AddCab(){
       final Dialog dialog = new Dialog(this);
       dialog.setContentView(R.layout.dailog_cab);
       dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       // set the custom dialog components - text, image and button
        close=dialog.findViewById(R.id.close);
        date_picker=dialog.findViewById(R.id.date_picker);
        radio1=dialog.findViewById(R.id.radioMale);
        radio2=dialog.findViewById(R.id.radioFemale);
        radioSex=dialog.findViewById(R.id.radioSex);
        edit_vehicle_no=dialog.findViewById(R.id.edit_vehicle_no);
        edit_name_cab=dialog.findViewById(R.id.edit_name);
        edit_phone_cab=dialog.findViewById(R.id.edit_phone);
        tv_time=dialog.findViewById(R.id.tv_time);
        tv_details_company=dialog.findViewById(R.id.tv_details_company);
        ll_submit=dialog.findViewById(R.id.ll_submit);
        tv_others=dialog.findViewById(R.id.tv_others);
        company_name_recycle=dialog.findViewById(R.id.company_name_recycle);
        HorizontalLayout3 = new LinearLayoutManager(SettingActivity.this, LinearLayoutManager.HORIZONTAL, false);

        company_name_recycle.setLayoutManager(HorizontalLayout3);
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(SettingActivity.this, R.style.datepicker,datePickerListener, mYear, mMonth, mDay).show();


            }


        });



        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = dialog. findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(TAG, "AddCab: "+radio_value);
            Toast.makeText(getApplicationContext(),radio_value, Toast.LENGTH_SHORT).show();

        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });


        ll_hide=dialog.findViewById(R.id.ll_hide);
        CategoryList();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
      ll_submit=dialog.findViewById(R.id.ll_submit);

       // if button is clicked, close the custom dialog
        ll_submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int selectedId = radioSex.getCheckedRadioButtonId();

               // find the radiobutton by returned id
               radio1 = dialog.findViewById(selectedId);

               String radio_value=radio1.getText().toString();
               AddVisitors("cab",radio_value);
               dialog.dismiss();



           }
       });
        tv_details_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true)
                {

                    ll_hide.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false)
                {
                    // car1.animate().alpha(1.0f);
                    ll_hide.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
            }
        });


        dialog.show();

   }
    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    myCalendar.set(Calendar.YEAR, selectedYear);
                    myCalendar.set(Calendar.MONTH, selectedMonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    String myFormat = "MMM dd, yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                 //   date_notify_exam = sdf1.format(myCalendar.getTime());
                    String date_to_show = sdf.format(myCalendar.getTime());
                    date_web = sdf1.format(myCalendar.getTime());
                   // Log.d(TAG, "date_notify_exam: "+date_notify_exam);
                    date_picker.setText(date_to_show);

                }
            };


    private void timePicker2(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);



        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(SettingActivity.this,R.style.datepicker,
                new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour,
                                  int selectedMinute) {

                String time1 = selectedHour + ":" + selectedMinute;
                SimpleDateFormat  format1 = new SimpleDateFormat("HH:mm",
                        Locale.ENGLISH);
                SimpleDateFormat  format2 = new SimpleDateFormat("hh:mm:ss a",
                        Locale.ENGLISH);

                try {

                    Date date = format1.parse(time1);

                    tv_time.setText(format2.format(date));

                }catch (Exception e){
                    e.printStackTrace();
                }

              //  tv_time.setText( ""+selectedHour + ":" + selectedMinute);

            }
        }, mHour, mMinute,true);

        mTimePicker.show();
    }


    public void AddDelivery(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delivery);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        delivery_recycle=dialog.findViewById(R.id.delivery_recycle);
        HorizontalLayout4 = new LinearLayoutManager(SettingActivity.this, LinearLayoutManager.HORIZONTAL, false);
        date_picker=dialog.findViewById(R.id.date_picker);
        radio1=dialog.findViewById(R.id.radioMale);
        radio2=dialog.findViewById(R.id.radioFemale);
        radioSex=dialog.findViewById(R.id.radioSex);
        edit_name_cab=dialog.findViewById(R.id.edit_name);
        edit_phone_cab=dialog.findViewById(R.id.edit_phone);
        tv_time=dialog.findViewById(R.id.tv_time);
        delivery_recycle.setLayoutManager(HorizontalLayout4);
        close=dialog.findViewById(R.id.close);
        tv_details_company=dialog.findViewById(R.id.tv_details_company);
        ll_hide=dialog.findViewById(R.id.ll_hide);
        DeliveryList();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(SettingActivity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


            }


        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });

        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = dialog. findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(TAG, "AddCab: "+radio_value);
            Toast.makeText(getApplicationContext(),radio_value, Toast.LENGTH_SHORT).show();

        });


        LinearLayout ll_submit=dialog.findViewById(R.id.ll_submit);

        // if button is clicked, close the custom dialog
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio1 = dialog.findViewById(selectedId);

                String radio_value=radio1.getText().toString();
                AddDelivery("delivery",radio_value);
                dialog.dismiss();
            }
        });
        tv_details_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true)
                {

                    ll_hide.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false)
                {
                    // car1.animate().alpha(1.0f);
                    ll_hide.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
            }
        });


        dialog.show();

    }
    public void AddGuest(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_guest);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        close=dialog.findViewById(R.id.close);
       // tv_details_company=dialog.findViewById(R.id.tv_details_company);
        ll_hide=dialog.findViewById(R.id.ll_hide);
        date_picker=dialog.findViewById(R.id.date_picker);
        radio1=dialog.findViewById(R.id.radioMale);
        radio2=dialog.findViewById(R.id.radioFemale);
        radioSex=dialog.findViewById(R.id.radioSex);
        edit_name_cab=dialog.findViewById(R.id.edit_name);
        edit_phone_cab=dialog.findViewById(R.id.edit_phone);
        tv_time=dialog.findViewById(R.id.tv_time);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(SettingActivity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


            }


        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });

        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = dialog. findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(TAG, "AddCab: "+radio_value);
            Toast.makeText(getApplicationContext(),radio_value, Toast.LENGTH_SHORT).show();

        });

        LinearLayout ll_submit=dialog.findViewById(R.id.ll_submit);

        // if button is clicked, close the custom dialog
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio1 = dialog.findViewById(selectedId);

                String radio_value=radio1.getText().toString();
                AddGuest("guest",radio_value);
                dialog.dismiss();
            }
        });


        dialog.show();

    }
    public void AddHelp(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_help);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        BrowseCity();
        close=dialog.findViewById(R.id.close);
        date_picker=dialog.findViewById(R.id.date_picker);
        radio1=dialog.findViewById(R.id.radioMale);
        radio2=dialog.findViewById(R.id.radioFemale);
        radioSex=dialog.findViewById(R.id.radioSex);
        edit_name_cab=dialog.findViewById(R.id.edit_name);
        edit_phone_cab=dialog.findViewById(R.id.edit_phone);
        tv_time=dialog.findViewById(R.id.tv_time);
        tv_details_company=dialog.findViewById(R.id.tv_details_company);
        spinner_help=dialog.findViewById(R.id.spinner_help);
        ll_hide=dialog.findViewById(R.id.ll_hide);

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(SettingActivity.this,R.style.datepicker, datePickerListener, mYear, mMonth, mDay).show();


            }


        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2();
            }
        });

        radioSex.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = dialog. findViewById(checkedId);
            // Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
            String radio_value= (String) radioButton.getText();
            Log.d(TAG, "AddCab: "+radio_value);
            Toast.makeText(getApplicationContext(),radio_value, Toast.LENGTH_SHORT).show();

        });
        spinner_help.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // Locate the textviews in activity_main.xml
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position !=0){
                    help_id = HelpList.get(position-1).get("name");
                    Log.d(TAG, "onItemSelected: "+help_id);


                   // BrowseComplex(city_id);

                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout ll_submit=dialog.findViewById(R.id.ll_submit);

        // if button is clicked, close the custom dialog
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio1 = dialog.findViewById(selectedId);

                String radio_value=radio1.getText().toString();

                AddVisitorHelp("visiting_help",radio_value);
                dialog.dismiss();
            }
        });
        tv_details_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true)
                {

                    ll_hide.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false)
                {
                    // car1.animate().alpha(1.0f);
                    ll_hide.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
            }
        });


        dialog.show();

    }

    private void AddVisitorHelp(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,


                AppConfig.add_panic, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();
               // dialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        //array.add("Select Location");
                        //  JsonArray jarray = jobj.getAsJsonArray("data");
                        //  Log.d("jarray", "" + jarray.toString());

                    }

                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_web);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
            //    params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);

                params.put("visiting_help_cat",help_id);
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void AddDelivery(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();
              //  dialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        //array.add("Select Location");
                        //  JsonArray jarray = jobj.getAsJsonArray("data");
                        //  Log.d("jarray", "" + jarray.toString());

                    }

                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_web);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
              // params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);
                params.put("vendor_name",category);
                params.put("visiting_help_cat","");
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void AddVisitors(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();
              //  dialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        //array.add("Select Location");
                        //  JsonArray jarray = jobj.getAsJsonArray("data");
                        //  Log.d("jarray", "" + jarray.toString());

                    }

                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_web);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
               params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);
                params.put("vendor_name",category);
                params.put("visiting_help_cat","");
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void AddGuest(final String type,final String radio_value) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();
              //  dialog.dismiss();

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        //array.add("Select Location");
                        //  JsonArray jarray = jobj.getAsJsonArray("data");
                        //  Log.d("jarray", "" + jarray.toString());

                    }

                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", type);
                params.put("time", tv_time.getText().toString());
                params.put("date", date_web);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name",edit_name_cab.getText().toString() );
                params.put("visitor_mobile",edit_phone_cab.getText().toString() );
              //  params.put("vehicle_no",edit_vehicle_no.getText().toString() );
                params.put("frequency",radio_value);
                params.put("visiting_help_cat","");
                params.put("profileImage","");
                params.put("user_id",globalClass.getId());

                Log.d(TAG, "getParams: "+params);
                return params;
            }



        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

    private void FireAlarm(final String category) {
        String tag_string_req = "req_login";
        HelpList.clear();
       // startAnim();
       pd.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

              pd.dismiss();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("user_id", globalClass.getId());
                params.put("category", category);
                params.put("complex_id", globalClass.getComplex_id());
                params.put("flat_id", globalClass.getFlat_no());

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }

    public void Alarm(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_alarm);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        close=dialog.findViewById(R.id.close);
        animal=dialog.findViewById(R.id.animal);
        theif=dialog.findViewById(R.id.burglary);
        lift=dialog.findViewById(R.id.lift);
        medical=dialog.findViewById(R.id.medical);
        threat=dialog.findViewById(R.id.threat);
        tv_details_company=dialog.findViewById(R.id.tv_details_company);
        tv_id=dialog.findViewById(R.id.tv_fire);
        tv_lift=dialog.findViewById(R.id.tv_lift);
        tv_threat=dialog.findViewById(R.id.tv_threat);
        tv_thief=dialog.findViewById(R.id.tv_thief);
        tv_medical=dialog.findViewById(R.id.tv_medical);
        tv_animal=dialog.findViewById(R.id.tv_animal);
        threat=dialog.findViewById(R.id.threat);
        ll_alram=dialog.findViewById(R.id.ll_alram);
        ll_hide=dialog.findViewById(R.id.ll_hide);
        fire=dialog.findViewById(R.id.fire);

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_id.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        lift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_lift.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        threat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_threat.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        theif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_thief.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_medical.getText().toString();
                FireAlarm(category);
                dialog.dismiss();

            }
        });
        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tv_animal.getText().toString();

                FireAlarm(category);
                dialog.dismiss();

            }
        });
/*
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
*/
    //    LinearLayout ll_submit=dialog.findViewById(R.id.ll_submit);

        // if button is clicked, close the custom dialog
/*
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
/*
        tv_details_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true)
                {

                    ll_hide.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false)
                {
                    // car1.animate().alpha(1.0f);
                    ll_hide.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
            }
        });
*/


        dialog.show();

    }



    public void MemberDialog(){
        dialog = new Dialog(this);
       dialog.setContentView(R.layout.member_info);
       ImageView img_edit=dialog.findViewById(R.id.img_edit_member);
        edit_family_name=dialog.findViewById(R.id.edit_name);
       edit_family_phone=dialog.findViewById(R.id.edit_phone);
       image_member=dialog.findViewById(R.id.image_member);

       img_edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectImageFamily();
           }
       });

       // set the custom dialog components - text, image and button

       LinearLayout ll_save=dialog.findViewById(R.id.ll_save);

       // if button is clicked, close the custom dialog
       ll_save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String family_name=edit_family_name.getText().toString();
               String family_phone=edit_family_phone.getText().toString();
               AddFamily(family_name,family_phone);
           }
       });

       dialog.show();

   }
   public void CarDialog(){
        dialog = new Dialog(this);
       dialog.setContentView(R.layout.car_info);
       ImageView img_edit=dialog.findViewById(R.id.edit_car);
        edit_car_no=dialog.findViewById(R.id.edit_car_no);
        edit_parking_no=dialog.findViewById(R.id.edit_parking_no);

       img_edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               selectImageCar();
           }
       });

       // set the custom dialog components - text, image and button

       LinearLayout ll_save=dialog.findViewById(R.id.ll_save);

       // if button is clicked, close the custom dialog
       ll_save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String car_no=edit_car_no.getText().toString();
               String parking_no=edit_parking_no.getText().toString();
               AddCar(car_no,parking_no);
           }
       });

       dialog.show();

   }

    public void logoutDailog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dailog);


        // set the custom dialog components - text, image and button

        LinearLayout ll_no=dialog.findViewById(R.id.ll_no);
        LinearLayout ll_yes=dialog.findViewById(R.id.ll_yes);

        // if button is clicked, close the custom dialog
        ll_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Logout();

            }
        });
        ll_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();

    }

public void Logout(){
    pd.show();

    StringRequest strReq = new StringRequest(Request.Method.POST,
            AppConfig.logout, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.d(TAG, "JOB RESPONSE: " + response.toString());

            pd.dismiss();


            Gson gson = new Gson();

            try {

                JsonObject jobj = gson.fromJson(response, JsonObject.class);

                String status = jobj.get("status").getAsString().replaceAll("\"", "");
                String message = jobj.get("message").getAsString().replaceAll("\"", "");

                Log.d(TAG, "Message: "+message);

                if(status.equals("1") ) {

                    preference.clearPrefrence();
                    Intent intent=new Intent(SettingActivity.this,LaunchActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else {
                    TastyToast.makeText(getApplicationContext(),
                            message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }

            } catch (Exception e) {
                e.printStackTrace();
                TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            }


        }
    }, new Response.ErrorListener() {

        @Override

        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
            TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            pd.dismiss();
        }
    }) {

        @Override
        protected Map<String, String> getParams() {
            // Posting parameters to login url
            Map<String, String> params = new HashMap<>();

            params.put("user_id", globalClass.getId());
            Log.d(TAG, "getParams: "+params);
            return params;
        }

    };

    // Adding request to request queue
    VolleySingleton.getInstance(SettingActivity.this)
            .addToRequestQueue(strReq
                    .setRetryPolicy(
                            new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

}
    public void updateProfile(final String name,final String phone,final String email){

         pd.show();

        String url = AppConfig.profile_update;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("user_id", globalClass.getId());
        params.put("name",name);
        params.put("emailid", email);
        params.put("mobile", phone);

        try{

            params.put("profileImage", p_image);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        cl.setSSLSocketFactory(
                new SSLSocketFactory(Config.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));



        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "user_profile_pic_update- " + response.toString());
                    try {
                        pd.dismiss();
                        dialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            // Log.d(TAG, "name: "+name)

                            JSONObject data = response.getJSONObject("data");

                            String user_id = data.optString("user_id");
                            String name = data.optString("name");
                            String emailid = data.optString("emailid");
                            String mobile = data.optString("mobile");
                            String profile_pic = data.optString("profile_pic");


                            globalClass.setId(user_id);
                            globalClass.setEmail(emailid);
                            globalClass.setName(name);
                            globalClass.setPhone_number(mobile);
                            globalClass.setProfil_pic(profile_pic);

                            preference.savePrefrence();
                            if (globalClass.getProfil_pic().equals("")) {
                                Picasso.with(getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").into(profile_image_edit);
                            } else {
                                Picasso.with(getApplicationContext()).load(globalClass.getProfil_pic()).into(profile_image_edit);
                            }
                            edit_name.setText(globalClass.getName());

                        } else {
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


}


    public void AddFamily(final String name,final String phone){

          pd.show();

        String url = AppConfig.add_family_member;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("user_id",globalClass.getId());
        params.put("family_member_name", name);
        params.put("flat_no", globalClass.getFlat_no());
        params.put("relationship", "");
        params.put("complex_id", globalClass.getComplex_id());
        params.put("family_member_mobile", phone);

        try{

            params.put("profileImage", p_image);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        cl.setSSLSocketFactory(
                new SSLSocketFactory(Config.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));



        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    pd.dismiss();
                if (response != null) {
                    Log.d(TAG, "user_profile_pic_update- " + response.toString());
                    try {

                        //JSONObject result = response.getJSONObject("result");

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                             dialog.dismiss();
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                              browseJob();



                        }else{

                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();


                        }

                        pd.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                // pd.dismiss();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });


    }

    public void AddCar(final String number,final String parking){

        pd.show();

        String url = AppConfig.add_car;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("user_id",globalClass.getId());
        params.put("car_name", "");
        params.put("car_no", number);
        params.put("parking_no", parking);
        params.put("complex_id", globalClass.getComplex_id());




        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pd.dismiss();
                if (response != null) {
                    Log.d(TAG, "user_profile_pic_update- " + response.toString());
                    try {

                        //JSONObject result = response.getJSONObject("result");

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            dialog.dismiss();
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                            browseJob();



                        }else{

                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();


                        }

                        pd.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                // pd.dismiss();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });


    }

    private void browseJob() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

       pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.profile_details, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);

                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d(TAG, "Message: "+message);

                    if(status.equals("1") ) {

                        JsonObject user = jobj.getAsJsonObject("user");
                        String user_id = user.get("user_id").toString().replaceAll("\"", "");
                        String name = user.get("name").toString().replaceAll("\"", "");
                        String emailid = user.get("emailid").toString().replaceAll("\"", "");
                        String mobile = user.get("mobile").toString().replaceAll("\"", "");
                        String profile_pic = user.get("profile_pic").toString().replaceAll("\"", "");
                        globalClass.setId(user_id);
                        globalClass.setEmail(emailid);
                        globalClass.setName(name);
                        globalClass.setPhone_number(mobile);
                        globalClass.setProfil_pic(profile_pic);
                        globalClass.setLogin_status(true);
                        globalClass.setLogin_from("signup");
                        preference.savePrefrence();
                        user_name.setText(globalClass.getName());
                        user_email.setText(globalClass.getEmail());
                        user_mobile.setText(globalClass.getPhone_number());


                        if (!profile_pic.isEmpty()){

                            Picasso.with(SettingActivity.this)
                                    .load(profile_pic) // web image url
                                    .fit().centerInside()
                                    .rotate(90)                    //if you want to rotate by 90 degrees
                                    .error(R.mipmap.profile_image)
                                    .placeholder(R.mipmap.profile_image)
                                    .into(profile_image);
                        }


                        JsonArray product = jobj.getAsJsonArray("family");
                        for (int i = 0; i < product.size(); i++) {
                            JsonObject images1 = product.get(i).getAsJsonObject();
                            String family_member_id = images1.get("family_member_id").toString().replaceAll("\"", "");
                            String family_member_name = images1.get("family_member_name").toString().replaceAll("\"", "");
                            String family_member_type = images1.get("family_member_type").toString().replaceAll("\"", "");
                            String family_member_mobile = images1.get("family_member_mobile").toString().replaceAll("\"", "");
                            String profile_pic_family = images1.get("profile_pic").toString().replaceAll("\"", "");


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("family_member_id", family_member_id);
                            hashMap.put("family_member_name", family_member_name);
                            hashMap.put("family_member_type", family_member_type);
                            hashMap.put("family_member_mobile", family_member_mobile);
                            hashMap.put("profile_pic_family", profile_pic_family);

                            productDetaiils.add(hashMap);
                            Log.d(TAG, "Hashmap " + hashMap);

                        }

                        RecyclerViewHorizontalAdapter = new FamilyAdapter(SettingActivity.this, productDetaiils);
                        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
                        JsonArray product_sub = jobj.getAsJsonArray("car");
                        for (int j = 0; j < product_sub.size(); j++) {
                            JsonObject images1_sub = product_sub.get(j).getAsJsonObject();
                            String id = images1_sub.get("id").toString().replaceAll("\"", "");
                            String _car_user_id = images1_sub.get("user_id").toString().replaceAll("\"", "");
                            String car_name = images1_sub.get("car_name").toString().replaceAll("\"", "");
                            String car_no = images1_sub.get("car_no").toString().replaceAll("\"", "");
                            String car_type = images1_sub.get("car_type").toString().replaceAll("\"", "");
                            String driver_name = images1_sub.get("driver_name").toString().replaceAll("\"", "");
                            String driver_mobile = images1_sub.get("driver_mobile").toString().replaceAll("\"", "");
                            String parking_no = images1_sub.get("parking_no").toString().replaceAll("\"", "");
                            String note = images1_sub.get("note").toString().replaceAll("\"", "");
                            String flat_no = images1_sub.get("flat_no").toString().replaceAll("\"", "");
                            String delete_flag = images1_sub.get("delete_flag").toString().replaceAll("\"", "");
                            String is_active = images1_sub.get("is_active").toString().replaceAll("\"", "");
                            String entry_date = images1_sub.get("entry_date").toString().replaceAll("\"", "");
                            String modified_date = images1_sub.get("modified_date").toString().replaceAll("\"", "");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", id);
                            hashMap.put("_car_user_id", _car_user_id);
                            hashMap.put("car_name", car_name);
                            hashMap.put("car_no", car_no);
                            hashMap.put("car_type", car_type);
                            hashMap.put("driver_name", driver_name);
                            hashMap.put("driver_mobile", driver_mobile);
                            hashMap.put("parking_no", parking_no);
                            hashMap.put("note", note);
                            hashMap.put("flat_no", flat_no);
                            hashMap.put("delete_flag", delete_flag);
                            hashMap.put("is_active", is_active);
                            hashMap.put("entry_date", entry_date);
                            hashMap.put("modified_date", modified_date);
                            productDetaiils_sub.add(hashMap);
                            Log.d(TAG, "Hashmap1 " + hashMap);

                        }
                        carAdapter = new CarAdapter(SettingActivity.this, productDetaiils_sub);
                        recyclerViewCar.setAdapter(carAdapter);


                        JsonArray staff = jobj.getAsJsonArray("staff");
                        for (int j = 0; j < staff.size(); j++) {
                            JsonObject images1_sub = staff.get(j).getAsJsonObject();
                            String staff_id = images1_sub.get("staff_id").toString().replaceAll("\"", "");
                            String staff_name = images1_sub.get("staff_name").toString().replaceAll("\"", "");
                            String staff_type = images1_sub.get("staff_type").toString().replaceAll("\"", "");
                            String staff_mobile = images1_sub.get("staff_mobile").toString().replaceAll("\"", "");
                            String staffprofile_pic = images1_sub.get("profile_pic").toString().replaceAll("\"", "");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("staff_id", staff_id);
                            hashMap.put("staff_name", staff_name);
                            hashMap.put("staff_type", staff_type);
                            hashMap.put("staff_mobile", staff_mobile);
                            hashMap.put("staffprofile_pic", staffprofile_pic);

                            staffList.add(hashMap);
                            Log.d(TAG, "Hashmap1 " + hashMap);

                        }
                        staffAdapter = new StaffAdapter(SettingActivity.this, staffList);
                        recyclerViewStaff.setAdapter(staffAdapter);
                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("user_id", globalClass.getId());
                params.put("flat_no", globalClass.getFlat_no());
                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void CategoryList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        Category.clear();

       pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.company_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);

                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d(TAG, "Message: "+message);

                    if(status.equals("1") ) {



                        JsonArray product = jobj.getAsJsonArray("data");
                        for (int i = 0; i < product.size(); i++) {
                            JsonObject images1 = product.get(i).getAsJsonObject();
                            String id = images1.get("id").toString().replaceAll("\"", "");
                            String company = images1.get("company").toString().replaceAll("\"", "");
                            String type = images1.get("type").toString().replaceAll("\"", "");
                            String icon = images1.get("icon").toString().replaceAll("\"", "");


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", id);
                            hashMap.put("company", company);
                            hashMap.put("type", type);
                            hashMap.put("icon", icon);

                            Category.add(hashMap);
                            Log.d(TAG, "Hashmap " + hashMap);

                        }

                        CategoryAdapter = new categoryAdapter(SettingActivity.this,
                                        Category,SettingActivity.this);
                        company_name_recycle.setAdapter(CategoryAdapter);




                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", "cab");

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void DeliveryList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        DeliveryList.clear();
       pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.company_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);

                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d(TAG, "Message: "+message);

                    if(status.equals("1") ) {



                        JsonArray product = jobj.getAsJsonArray("data");
                        for (int i = 0; i < product.size(); i++) {
                            JsonObject images1 = product.get(i).getAsJsonObject();
                            String id = images1.get("id").toString().replaceAll("\"", "");
                            String company = images1.get("company").toString().replaceAll("\"", "");
                            String type = images1.get("type").toString().replaceAll("\"", "");
                            String icon = images1.get("icon").toString().replaceAll("\"", "");


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", id);
                            hashMap.put("company", company);
                            hashMap.put("type", type);
                            hashMap.put("icon", icon);

                            DeliveryList.add(hashMap);
                            Log.d(TAG, "Hashmap " + hashMap);

                        }

                        CategoryAdapter = new categoryAdapter(SettingActivity.this,
                                DeliveryList,SettingActivity.this);
                        delivery_recycle.setAdapter(CategoryAdapter);


                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", "delivery");

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void Add(final String type) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";


       pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.add_visitor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                pd.dismiss();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);

                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");

                    Log.d(TAG, "Message: "+message);

                    if(status.equals("1") ) {








                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("type", "delivery");
                params.put("time", tv_time.getText().toString());
                params.put("date", date_web);
                params.put("type", type);
                params.put("flat_no", globalClass.getFlat_no());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("visitor_name", edit_name_cab.getText().toString());
                params.put("visitor_mobile", edit_phone_cab.getText().toString());

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    private void BrowseCity() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
        startAnim();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.help_category_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                stopAnim();


                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    String status = jobj.get("status").getAsString().replaceAll("\"", "");
                    String message = jobj.get("message").getAsString().replaceAll("\"", "");


                    if(status.equals("1")) {

                        Log.d("jobj", "" + jobj);
                        ArrayList<String> state_array = new ArrayList<String>();
                        state_array.add("Select State");

                        //array.add("Select Location");
                        JsonArray jarray = jobj.getAsJsonArray("data");
                        Log.d("jarray", "" + jarray.toString());
                        array1 = new ArrayList<>();
                        array1.add("Help");
                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            //get the object

                            //JsonObject jobj1 = jarray.get(i).getAsJsonObject();
                            String id = jobj1.get("id").toString().replaceAll("\"", "");
                            String name = jobj1.get("name").toString().replaceAll("\"", "");
                            String image = jobj1.get("image").toString().replaceAll("\"", "");

                            HashMap<String, String> map_ser = new HashMap<>();


                            map_ser.put("id", id);
                            map_ser.put("name", name);
                            map_ser.put("image", image);


                            HelpList.add(map_ser);

                            array1.add(name);


                        }

                        dataAdapter1 = new ArrayAdapter(SettingActivity.this, R.layout.item_spinner, R.id.tvCust, array1);
                        spinner_help.setAdapter(dataAdapter1);
                        spinner_help.setPrompt("Help");
                    }
                    else {
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }



                } catch (Exception e) {
                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                pd.dismiss();
            }
        }) {



        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }
    void startAnim(){
        avLoadingIndicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }

    @Override
    public void onItemClick(String s) {
        category=s;
        if (category.equals("Others")){
            tv_others.setVisibility(View.VISIBLE);
        }
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();


    }


/*
    private void displayFirebaseRegId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        //   globalClass.setFcm_token(token);

                        // Log and toast
                        Log.d(TAG, "token = "+token);

                        user_email.setText(token);

                        //  threadFor();
                    }
                });

    }
*/

}