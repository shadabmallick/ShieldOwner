package com.shield.resident.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.shield.resident.Adapter.CarAdapter;
import com.shield.resident.Adapter.FamilyAdapter;
import com.shield.resident.Adapter.StaffAdapter;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;
import com.shield.resident.dialogs.DialogAlarmAdd;
import com.shield.resident.dialogs.DialogCabAdd;
import com.shield.resident.dialogs.DialogDeliveryAdd;
import com.shield.resident.dialogs.DialogGuestActivityAdd;
import com.shield.resident.dialogs.DialogHelpActivityAdd;
import com.shield.resident.dialogs.LoaderDialog;
import com.shield.resident.util.Commons;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class SettingActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        FamilyAdapter.ViewClickListener,
        CarAdapter.ViewClickListener{


    String TAG="Setting";

    RecyclerView recyclerView_members,recyclerViewStaff,recyclerViewCar;

    FamilyAdapter RecyclerViewHorizontalAdapter;
    ArrayAdapter<String> dataAdapter1;
    Spinner spinner_help;
    StaffAdapter staffAdapter;
    ArrayList<String> array1;
    CarAdapter carAdapter;

    File p_image;
    String currentDate,currentTime;
    Dialog dialog;
    Toolbar toolbar;
    TextView user_name, user_mobile, user_email, passcode;
    ImageView profile_image_edit,image_member;

    EditText edit_car_no,edit_parking_no,edit_name,edit_phone,edit_mail,
            edit_family_name,edit_family_phone;
    GlobalClass globalClass;
    Shared_Preference preference;
    ArrayList<HashMap<String,String>> listFamilyMembers;
    ArrayList<HashMap<String,String>> Category;
    ArrayList<HashMap<String,String>> DeliveryList;
    ArrayList<HashMap<String,String>> HelpList;
    ArrayList<HashMap<String,String>> listCars;
    ArrayList<HashMap<String,String>> staffList;
    ImageView profile_image,img_cab,img_delivery,img_guest, img_help,profile_image_staff;
    RelativeLayout rel_profile,rel_middle_icon;
    ImageView edit, iv_no_data1, iv_no_data2, iv_no_data3;
    String help_id = "";
    TextView add_car,add_staff,add_member;
    EditText edit_staff_phone,edit_staff_name,tv_others;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private final int PICK_IMAGE_CAMERA_FAMILY = 3, PICK_IMAGE_GALLERY_FAMILY = 4;
    private final int PICK_IMAGE_CAMERA_CAR = 5, PICK_IMAGE_GALLERY_CAR = 6;
    private final int PICK_IMAGE_CAMERA_STAFF = 7, PICK_IMAGE_GALLERY_STAFF = 6;
    LinearLayout ll_bell,button_E3,button_E1,ll_app_help,ll_visitor_option,button_activity;
    SwipeRefreshLayout swipe_refresh;
    LinearLayout linear_main, ll_my_complex, linear_share_address;
    RelativeLayout rl_app_noti_settings, rl_support_feedback, rl_tell_friend, rl_logout;
    TextView tv_appVersion, terms_condition, tv_privacy;
    RelativeLayout rl_add_member, relfamily, rl_add_staff, relstaff, rl_add_car, relCar;
    String phone_secure;

    LoaderDialog loaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        initViews();
        currentTime = new SimpleDateFormat("HH:mm:ss",
                Locale.getDefault()).format(new Date());
        currentDate = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SettingActivity.this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SettingActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            }
            else{
                if(checkForPermission(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 124)){

                }

            }
        }


    }

    public void initViews() {

        listFamilyMembers =new ArrayList<>();
        staffList=new ArrayList<>();
        listCars =new ArrayList<>();
        DeliveryList=new ArrayList<>();
        HelpList=new ArrayList<>();

        Category=new ArrayList<>();
        globalClass = (GlobalClass) getApplicationContext();
        preference = new Shared_Preference(SettingActivity.this);
        preference.loadPrefrence();
        preference.saveFirstLogin(false);


        toolbar = findViewById(R.id.toolbar);
        edit =  findViewById(R.id.edit);
        add_car =  findViewById(R.id.add_car);
        add_member =  findViewById(R.id.add_member);
        add_staff =  findViewById(R.id.add_staff);
        rel_profile =findViewById(R.id.rel_profile);
        profile_image =  findViewById(R.id.profile_image);
        user_email =  findViewById(R.id.user_email);
        user_name =  findViewById(R.id.user_name);
        passcode =  findViewById(R.id.passcode);

        user_mobile =  findViewById(R.id.user_mobile);
        ll_visitor_option =  findViewById(R.id.ll_visitor_option);
        rel_middle_icon =  findViewById(R.id.rel_middle_icon);
        button_activity=  findViewById(R.id.button_E);

        button_E1=  findViewById(R.id.button_E1);
        button_E3=  findViewById(R.id.button_E3);
        ll_app_help=  findViewById(R.id.button_E4);
        ll_bell=  findViewById(R.id.ll_bell);
        img_cab=  findViewById(R.id.img_cab);
        img_guest=  findViewById(R.id.img_guest);
        img_delivery=  findViewById(R.id.img_delivery);
        img_help=  findViewById(R.id.img_help);

        iv_no_data1=  findViewById(R.id.iv_no_data1);
        iv_no_data2=  findViewById(R.id.iv_no_data2);
        iv_no_data3=  findViewById(R.id.iv_no_data3);
        iv_no_data1.setVisibility(View.GONE);
        iv_no_data2.setVisibility(View.GONE);
        iv_no_data3.setVisibility(View.GONE);


        rl_add_member = findViewById(R.id.rl_add_member);
        relfamily = findViewById(R.id.relfamily);
        rl_add_staff = findViewById(R.id.rl_add_staff);
        relstaff = findViewById(R.id.relstaff);
        rl_add_car = findViewById(R.id.rl_add_car);
        relCar = findViewById(R.id.relCar);


        toolbar.setTitle("");
        recyclerView_members = findViewById(R.id.rec_family);
        recyclerViewStaff = findViewById(R.id.rec_staff);
        recyclerViewCar = findViewById(R.id.rec_car);

        swipe_refresh = findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(this);

        linear_share_address = findViewById(R.id.linear_share_address);
        linear_main = findViewById(R.id.linear_main);
        linear_main.setVisibility(View.INVISIBLE);

        rl_app_noti_settings = findViewById(R.id.rl_app_noti_settings);
        rl_support_feedback = findViewById(R.id.rl_support_feedback);
        rl_tell_friend = findViewById(R.id.rl_tell_friend);
        rl_logout = findViewById(R.id.rl_logout);
        ll_my_complex = findViewById(R.id.ll_my_complex);

        tv_appVersion = findViewById(R.id.tv_appVersion);
        terms_condition = findViewById(R.id.terms_condition);
        tv_privacy = findViewById(R.id.tv_privacy);


        recyclerView_members.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStaff.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCar.setLayoutManager(new LinearLayoutManager(this));


        setSupportActionBar(toolbar);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileDialog();
            }
        });

        img_cab.setOnClickListener(v -> {

            ll_visitor_option.setVisibility(View.GONE);

            DialogCabAdd dialogCabAdd = new DialogCabAdd(SettingActivity.this);
            dialogCabAdd.show();

        });

        img_delivery.setOnClickListener(v -> {

            ll_visitor_option.setVisibility(View.GONE);

            DialogDeliveryAdd dialogDeliveryAdd = new DialogDeliveryAdd(SettingActivity.this);
            dialogDeliveryAdd.show();

        });

        img_guest.setOnClickListener(v -> {
            ll_visitor_option.setVisibility(View.GONE);

            Intent intent = new Intent(SettingActivity.this,
                    DialogGuestActivityAdd.class);
            startActivity(intent);

        });

        img_help.setOnClickListener(v -> {
            ll_visitor_option.setVisibility(View.GONE);

            Intent intent = new Intent(SettingActivity.this,
                    DialogHelpActivityAdd.class);
            startActivity(intent);


        });

        ll_bell.setOnClickListener(v -> {

            ll_visitor_option.setVisibility(View.GONE);

            DialogAlarmAdd dialogAlarmAdd = new DialogAlarmAdd(SettingActivity.this);
            dialogAlarmAdd.show();
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

        add_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaffDialog();
            }
        });

        rl_app_noti_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),NotificationManager.class);

                startActivity(notification);
            }
        });

        ll_my_complex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MyComplex.class);
                startActivity(intent);
            }
        });

        button_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(getApplicationContext(),Activity_activity.class);

                startActivity(notification);
            }
        });

        rl_support_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ContactUs.class);
                startActivity(intent);
            }
        });

        button_E1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SecurityScreen.class);
                startActivity(intent);
            }
        });

        button_E3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CommunityActivity.class);
                startActivity(intent);
            }
        });

        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });

        ll_app_help.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, AppTour.class);
            startActivity(intent);
        });

        rel_middle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (globalClass.getIs_tenant().equals("no")){
                    ll_visitor_option.setVisibility(ll_visitor_option.getVisibility()
                            == View.VISIBLE ? View.GONE : View.VISIBLE);
                }else {
                    TastyToast.makeText(getApplicationContext(),
                            "You shifted this features to your tenant",
                            TastyToast.LENGTH_LONG, TastyToast.INFO);
                }
            }
        });

        rl_tell_friend.setOnClickListener(v -> {

            String url = "https://play.google.com/store/apps/details?id="
                    + SettingActivity.this.getPackageName();

            String message = "The all-in-one solution to manage your guests, " +
                    "deliveries and much more. Implement the app in your society.\n\n"+url;

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Share Via ");
            startActivity(shareIntent);

        });
        linear_share_address.setOnClickListener(v -> {

            String message = "My Complex address :\n"+globalClass.getComplex_address()+"\n";

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Share Via ");
            startActivity(shareIntent);

        });


        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pInfo.versionName;
            int versionCode = pInfo.versionCode;

            Log.d("TAG", "versionName = "+versionName);
            Log.d("TAG", "versionCode = "+versionCode);

            tv_appVersion.setText("Version: "+versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        terms_condition.setOnClickListener(v -> {

            String url = "https://www.shieldapp.in/terms-conditions/";

            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
                        + " Please install a web browser",  Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        tv_privacy.setOnClickListener(v -> {

            String url = "https://www.shieldapp.in/privacy-policy/";

            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
                        + " Please install a web browser",  Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        rl_add_member.setOnClickListener(v -> {
            MemberDialog();
        });
        rl_add_staff.setOnClickListener(v -> {
            StaffDialog();
        });
        rl_add_car.setOnClickListener(v -> {
            CarDialog();
        });



        profile_details_api_call();
    }

    @Override
    public void onRefresh() {

        profile_details_api_call();

    }

    Switch switch_phone_secure;
    public void profileDialog(){

        dialog = new Dialog(this, R.style.datepicker);
        dialog.setContentView(R.layout.edit_profile_dialog);

        profile_image_edit  =dialog.findViewById(R.id.profile_image_edit);
        ImageView img_edit=dialog.findViewById(R.id.img_edit);
        edit_name  =dialog.findViewById(R.id.edit_name);
        edit_phone=dialog.findViewById(R.id.edit_phone);
        edit_mail=dialog.findViewById(R.id.edit_mail);
        switch_phone_secure = dialog.findViewById(R.id.switch_phone_secure);

        if (phone_secure.equals("1")){
            switch_phone_secure.setChecked(true);
        }else {
            switch_phone_secure.setChecked(false);
        }

        if (!globalClass.getProfil_pic().isEmpty()){
            Picasso.with(SettingActivity.this)
                    .load(globalClass.getProfil_pic()) // web image url
                    .fit().centerInside()
                    .error(R.mipmap.profile_image)
                    .placeholder(R.mipmap.profile_image)
                    .into(profile_image_edit);

        }

        edit_name.setText(globalClass.getName());
        edit_phone.setText(globalClass.getPhone_number());
        edit_mail.setText(globalClass.getEmail());


        if (!edit_mail.getText().toString().isEmpty()){
            edit_mail.setEnabled(false);
        }

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


                if (name.trim().length() == 0){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter your name",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (phone.trim().length() == 0){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter your phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (phone.trim().length() < 10){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter 10-digit phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (email.trim().length() == 0){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter your email",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (!isValidEmail(email)){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter valid email",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }




                String switch_value = "0";
                if (switch_phone_secure.isChecked()){
                    switch_value = "1";
                }else {
                    switch_value = "0";
                }

                updateProfile(name,phone,email, switch_value);

            }
        });

        dialog.show();

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public boolean checkForPermission(final String[] permissions,
                                      final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this,
                        permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        permissionsNeeded.add(perm);

                    } else {
                        permissionsNeeded.add(perm);
                    }

                }
            }
        }

        if (permissionsNeeded.size() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), permRequestCode);
            }
            return false;
        } else {
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
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                        }
                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else{


            }
        } catch (Exception e) {
            Toast.makeText(SettingActivity.this,
                    "Camera Permission error", Toast.LENGTH_SHORT).show();
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

    private void selectImageStaff() {
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
                            startActivityForResult(intent, PICK_IMAGE_CAMERA_STAFF);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY_STAFF);
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

        /// profile ...
        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                writeBitmap(bitmap, profile_image_edit);
            } catch (IOException e){
                e.printStackTrace();
            }

        }
        else if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {

            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                writeBitmap(bitmap, profile_image_edit);
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        /// family member ...
        else if (requestCode == PICK_IMAGE_GALLERY_FAMILY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                writeBitmap(bitmap, image_member);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_CAMERA_FAMILY && resultCode == RESULT_OK) {

            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                writeBitmap(bitmap, image_member);
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        /// car image ...
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
        else if (requestCode == PICK_IMAGE_GALLERY_CAR && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri uri = data.getData();
            p_image = new File(getRealPathFromURI(uri));
            Log.d(TAG, "image = "+p_image);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image_member.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /// Staff image ...
        else if (requestCode == PICK_IMAGE_GALLERY_STAFF && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                writeBitmap(bitmap, profile_image_staff);
            } catch (IOException e){
                e.printStackTrace();
            }

        }
        else if (requestCode == PICK_IMAGE_CAMERA_STAFF && resultCode == RESULT_OK) {

            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                writeBitmap(bitmap, profile_image_staff);
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        if(globalClass.isNetworkAvailable()){
        }else{
            TastyToast.makeText(SettingActivity.this,
                    "Network Connection",
                    TastyToast.LENGTH_LONG,TastyToast.WARNING).show();
        }

    }


    private void writeBitmap(Bitmap bitmap, ImageView imageView){

        bitmap = Commons.getResizedBitmap(bitmap, 480, 520);

        final String dir = Commons.getFolderDirectory();

        File file = new File(dir);
        if (!file.exists())
            file.mkdir();


        String files = dir + "/profile_pic" +".jpg";
        File newfile = new File(files);

        try {

            imageView.setImageBitmap(bitmap);

            newfile.delete();
            OutputStream outFile = null;
            try {

                p_image = newfile;

                outFile = new FileOutputStream(newfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outFile);
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

    }



    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null,
                null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void StaffDialog(){

        dialog = new Dialog(this, R.style.datepicker);
        dialog.setContentView(R.layout.staff_nfo);
        dialog.setCanceledOnTouchOutside(false);

        profile_image_staff=dialog.findViewById(R.id.profile_image);
        edit_staff_name=dialog.findViewById(R.id.edit_staff_name);
        edit_staff_phone=dialog.findViewById(R.id.edit_staff_phone);
        help_category_list();
        spinner_help=dialog.findViewById(R.id.spinner_help);
        // set the custom dialog components - text, image and button

        LinearLayout ll_save=dialog.findViewById(R.id.ll_save);
        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edit_staff_name.getText().toString();
                String phone=edit_staff_phone.getText().toString();

                if (name.trim().length() == 0){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter staff name",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (phone.trim().length() == 0){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter staff phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (phone.trim().length() < 10){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter 10-digit phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (help_id.isEmpty()){
                    TastyToast.makeText(SettingActivity.this,
                            "Select help category",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }


                AddStaff("staff", name, phone);

            }
        });


        spinner_help.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int position, long arg3) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position !=0){
                    help_id = HelpList.get(position-1).get("name");
                    Log.d(TAG, "onItemSelected: "+help_id);
                }else {
                    help_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        dialog.show();

    }

    public void AddStaff(final String type, final String name,
                         final String phone){

        loaderDialog.show();

        String url = AppConfig.add_visitor;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("user_id", globalClass.getId());
        params.put("visitor_name", name);
        params.put("type", type);
        params.put("visitor_mobile", phone);
        params.put("flat_no", globalClass.getFlat_id());
        params.put("complex_id", globalClass.getComplex_id());
        params.put("visiting_help_cat", help_id);
        params.put("vendor_name", "");
        params.put("vehicle_no", "");
        params.put("frequency", "no");
        params.put("date", currentDate);
        params.put("time", currentTime);


       // Log.d(TAG , "URL "+url);
      //  Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "user_profile_pic_update- " + response.toString());
                    try {
                        loaderDialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            dialog.dismiss();

                            profile_details_api_call();
                            // Log.d(TAG, "name: "+name)
                            TastyToast.makeText(SettingActivity.this, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        } else {
                            TastyToast.makeText(SettingActivity.this, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        TastyToast.makeText(SettingActivity.this, "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

    public void MemberDialog(){
        dialog = new Dialog(this, R.style.datepicker);
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


        LinearLayout ll_save=dialog.findViewById(R.id.ll_save);

        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String family_name=edit_family_name.getText().toString();
                String family_phone=edit_family_phone.getText().toString();

                if (family_name.trim().length() == 0){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter family member name",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (family_phone.trim().length() == 0){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter family member's phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (family_phone.trim().length() < 10){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter 10-digit phone number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }



                AddFamily(family_name,family_phone);
            }
        });

        dialog.show();

    }

    public void CarDialog(){

        dialog = new Dialog(this, R.style.datepicker);
        dialog.setContentView(R.layout.car_info);
        ImageView img_edit = dialog.findViewById(R.id.edit_car);
        edit_car_no=dialog.findViewById(R.id.edit_car_no);
        edit_parking_no=dialog.findViewById(R.id.edit_parking_no);

        if (globalClass.getParking_no() != null
                || !globalClass.getParking_no().equals(null)
                || !globalClass.getParking_no().equals("null")){
            edit_parking_no.setText(globalClass.getParking_no());
        }
        edit_parking_no.setEnabled(false);


        LinearLayout ll_save=dialog.findViewById(R.id.ll_save);

        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String car_no=edit_car_no.getText().toString();
                String parking_no=edit_parking_no.getText().toString();

                if (car_no.trim().length() == 0){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter your car registration number",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }

                if (parking_no.trim().length() == 0){
                    TastyToast.makeText(SettingActivity.this,
                            "Enter your car parking no",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    return;
                }


                AddCar(car_no,parking_no);
            }
        });

        dialog.show();

    }

    public void logoutDialog(){
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);
        dialog.setContentView(R.layout.logout_dailog);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        LinearLayout ll_no=dialog.findViewById(R.id.ll_no);
        LinearLayout ll_yes=dialog.findViewById(R.id.ll_yes);
        dialog.show();

        ll_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Logout();

            }
        });
        ll_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

    }

    public void Logout(){
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.logout, response -> {
            Log.d(TAG, "JOB RESPONSE: " + response.toString());

            loaderDialog.dismiss();


            Gson gson = new Gson();

            try {

                JsonObject jobj = gson.fromJson(response, JsonObject.class);

                String status = jobj.get("status").getAsString().replaceAll("\"", "");
                String message = jobj.get("message").getAsString().replaceAll("\"", "");

                Log.d(TAG, "Message: "+message);

                if(status.equals("1") ) {

                    preference.clearPreference();
                    Intent intent=new Intent(SettingActivity.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                else {
                    TastyToast.makeText(SettingActivity.this,
                            message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }

            } catch (Exception e) {
                e.printStackTrace();
                TastyToast.makeText(SettingActivity.this, "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            }


        }, error -> {
            Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
            loaderDialog.dismiss();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
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

    public void updateProfile(final String name,final String phone,
                              final String email, String switch_value){

        loaderDialog.show();

        String url = AppConfig.profile_update;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("user_id", globalClass.getId());
        params.put("name",name);
        params.put("emailid", email);
        params.put("mobile", phone);
        params.put("phone_show", switch_value);

        try{

            if (p_image != null){
                params.put("profileImage", p_image);
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


       // Log.d(TAG , "URL "+url);
       // Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "user_profile_pic_update- " + response.toString());
                    try {

                        loaderDialog.dismiss();

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
                            String phone_show = data.optString("phone_show");


                            globalClass.setId(user_id);
                            globalClass.setEmail(emailid);
                            globalClass.setName(name);
                            globalClass.setPhone_number(mobile);
                            globalClass.setProfil_pic(profile_pic);

                            phone_secure = phone_show;
                            if (phone_show.equals("1")){
                                user_mobile.setText(globalClass.getPhone_number() + "(Open)");
                            }else {
                                user_mobile.setText(globalClass.getPhone_number()  + "(Secure)");
                            }



                            preference.savePrefrence();
                            if (!globalClass.getProfil_pic().isEmpty()) {
                                Picasso.with(getApplicationContext())
                                        .load(globalClass.getProfil_pic())
                                        .into(profile_image_edit);
                            }
                            edit_name.setText(globalClass.getName());

                            profile_details_api_call();

                        } else {
                            TastyToast.makeText(SettingActivity.this, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        TastyToast.makeText(SettingActivity.this, "Error Connection", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG, "responseString- " + responseString);
            }
        });


    }

    public void AddFamily(final String name,final String phone){

        loaderDialog.show();

        String url = AppConfig.add_family_member;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("user_id",globalClass.getId());
        params.put("family_member_name", name);
        params.put("flat_no", globalClass.getFlat_id());
        params.put("relationship", "");
        params.put("complex_id", globalClass.getComplex_id());
        params.put("family_member_mobile", phone);
        params.put("user_type", globalClass.getUser_type());


        try{

            params.put("profileImage", p_image);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

       // Log.d(TAG , "URL "+url);
       // Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loaderDialog.dismiss();
                if (response != null) {
                    Log.d(TAG, "add_family_member- " + response.toString());
                    try {

                        loaderDialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            dialog.dismiss();

                            TastyToast.makeText(SettingActivity.this,
                                    message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                            profile_details_api_call();

                        } else{

                            TastyToast.makeText(SettingActivity.this,
                                    message, TastyToast.LENGTH_LONG,
                                    TastyToast.WARNING).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });


    }

    public void AddCar(final String number,final String parking){

        loaderDialog.show();

        String url = AppConfig.add_car;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("user_id",globalClass.getId());
        params.put("flat_id",globalClass.getFlat_id());
        params.put("car_name", "");
        params.put("car_no", number);
        params.put("parking_no", parking);
        params.put("parking_id", globalClass.getParking_id());
        params.put("complex_id", globalClass.getComplex_id());

        Log.d(TAG, "AddCar: "+params);

       // Log.d(TAG , "URL "+url);
       // Log.d(TAG , "params "+params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loaderDialog.dismiss();
                if (response != null) {
                    Log.d(TAG, "user_profile_pic_update- " + response.toString());
                    try {

                        loaderDialog.dismiss();

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            dialog.dismiss();

                            TastyToast.makeText(getApplicationContext(), message,
                                    TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                            profile_details_api_call();

                        }

                        else if(status == 0){
                            dialog.dismiss();
                          //  loaderDialog.dismiss();
                            Log.d(TAG, "onSuccess: "+message);
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                           /* TastyToast.makeText(getApplicationContext(), message,
                                    TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();*/
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });

    }

    private void profile_details_api_call() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        listCars = new ArrayList<>();
        listFamilyMembers = new ArrayList<>();
        staffList = new ArrayList<>();
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.profile_details, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                loaderDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");

                    Log.d(TAG, "Message: "+message);

                    if(status.equals("1") ) {

                        JSONObject user = jsonObject.getJSONObject("user");

                        String user_id = user.optString("user_id");
                        String name = user.optString("name");
                        String emailid = user.optString("emailid");
                        String mobile = user.optString("mobile");
                        String profile_pic = user.optString("profile_pic");
                        String phone_show = user.optString("phone_show");
                        String user_passcode = user.optString("user_passcode");



                        if (emailid == null
                                || emailid.equals("null")
                                || emailid.equals(null)){
                            emailid = "";
                        }



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
                        passcode.setText("#Passcode: "+user_passcode);


                        phone_secure = phone_show;
                        if (phone_show.equals("1")){
                            user_mobile.setText(globalClass.getPhone_number() + "(Open)");
                        }else {
                            user_mobile.setText(globalClass.getPhone_number()  + "(Secure)");
                        }



                        if (!profile_pic.isEmpty()){

                            Picasso.with(SettingActivity.this)
                                    .load(profile_pic) // web image url
                                    .fit().centerInside()
                                    .error(R.mipmap.profile_image)
                                    .placeholder(R.mipmap.profile_image)
                                    .into(profile_image);
                        }


                        JSONArray family = jsonObject.getJSONArray("family");
                        for (int i = 0; i < family.length(); i++) {
                            JSONObject object1 = family.getJSONObject(i);
                            String family_member_id = object1.optString("family_member_id");
                            String family_member_name = object1.optString("family_member_name");
                            String family_member_type = object1.optString("family_member_type");
                            String family_member_mobile = object1.optString("family_member_mobile");
                            String profile_pic_family = object1.optString("profile_pic");


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("family_member_id", family_member_id);
                            hashMap.put("family_member_name", family_member_name);
                            hashMap.put("family_member_type", family_member_type);
                            hashMap.put("family_member_mobile", family_member_mobile);
                            hashMap.put("profile_pic_family", profile_pic_family);

                            listFamilyMembers.add(hashMap);

                        }

                        RecyclerViewHorizontalAdapter = new FamilyAdapter(SettingActivity.this,
                                        listFamilyMembers);
                        recyclerView_members.setAdapter(RecyclerViewHorizontalAdapter);
                        RecyclerViewHorizontalAdapter.setViewClickListener(SettingActivity.this);


                        if (listFamilyMembers.size() == 0){
                            add_member.setVisibility(View.INVISIBLE);
                            rl_add_member.setVisibility(View.VISIBLE);
                            relfamily.setVisibility(View.GONE);
                        }else {
                            rl_add_member.setVisibility(View.GONE);
                            relfamily.setVisibility(View.VISIBLE);
                            add_member.setVisibility(View.VISIBLE);
                        }



                        JSONArray car = jsonObject.getJSONArray("car");
                        for (int j = 0; j < car.length(); j++) {
                            JSONObject car_object = car.getJSONObject(j);

                            String id = car_object.optString("id");
                            String _car_user_id = car_object.optString("user_id");
                            String car_name = car_object.optString("car_name");
                            String car_no = car_object.optString("car_no");
                            String car_type = car_object.optString("car_type");
                            String driver_name = car_object.optString("driver_name");
                            String driver_mobile = car_object.optString("driver_mobile");
                            String parking_no = car_object.optString("parking_no");
                            String note = car_object.optString("note");
                            String flat_no = car_object.optString("flat_no");
                            String delete_flag = car_object.optString("delete_flag");
                            String is_active = car_object.optString("is_active");
                            String entry_date = car_object.optString("entry_date");
                            String modified_date = car_object.optString("modified_date");

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

                            listCars.add(hashMap);

                        }
                        carAdapter = new CarAdapter(SettingActivity.this, listCars);
                        recyclerViewCar.setAdapter(carAdapter);
                        carAdapter.setViewClickListener(SettingActivity.this);


                        if (listCars.size() == 0){
                            add_car.setVisibility(View.INVISIBLE);
                            rl_add_car.setVisibility(View.VISIBLE);
                            relCar.setVisibility(View.GONE);
                        }else {
                            rl_add_car.setVisibility(View.GONE);
                            relCar.setVisibility(View.VISIBLE);
                            add_car.setVisibility(View.VISIBLE);
                        }



                        JSONArray staff = jsonObject.getJSONArray("staff");
                        for (int j = 0; j < staff.length(); j++) {

                            JSONObject staff_object = staff.getJSONObject(j);
                            String staff_id = staff_object.optString("staff_id");
                            String staff_name = staff_object.optString("staff_name");
                            String staff_type = staff_object.optString("staff_type");
                            String staff_mobile = staff_object.optString("staff_mobile");
                            String staffprofile_pic = staff_object.optString("profile_pic");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("staff_id", staff_id);
                            hashMap.put("staff_name", staff_name);
                            hashMap.put("staff_type", staff_type);
                            hashMap.put("staff_mobile", staff_mobile);
                            hashMap.put("staffprofile_pic", staffprofile_pic);

                            staffList.add(hashMap);
                            //Log.d(TAG, "Hashmap1 " + hashMap);


                        }

                        staffAdapter = new StaffAdapter(SettingActivity.this, staffList);
                        recyclerViewStaff.setAdapter(staffAdapter);


                        if (staffList.size() == 0){
                            add_staff.setVisibility(View.INVISIBLE);
                            rl_add_staff.setVisibility(View.VISIBLE);
                            relstaff.setVisibility(View.GONE);
                        }else {
                            rl_add_staff.setVisibility(View.GONE);
                            relstaff.setVisibility(View.VISIBLE);
                            add_staff.setVisibility(View.VISIBLE);
                        }


                    } else {
                        TastyToast.makeText(getApplicationContext(), message,
                                TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "Error Connection",
                            TastyToast.LENGTH_LONG, TastyToast.WARNING);

                }

                linear_main.setVisibility(View.VISIBLE);

                swipe_refresh.setRefreshing(false);

            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                loaderDialog.dismiss();
                swipe_refresh.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<>();

                params.put("user_id", globalClass.getId());
                params.put("flat_no", globalClass.getFlat_id());
                params.put("complex_id", globalClass.getComplex_id());
                params.put("user_type", globalClass.getUser_type());

                /*if (globalClass.getUser_type().equals("owner")){
                    params.put("user_type", "1");
                }else if (globalClass.getUser_type().equals("member")){
                    params.put("user_type", "4");
                }else if (globalClass.getUser_type().equals("tenant")){
                    params.put("user_type", "6");
                }*/




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

    private void help_category_list() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        HelpList.clear();
        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.help_category_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "JOB RESPONSE: " + response.toString());

                loaderDialog.dismiss();


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
                        array1.add("Visiting Help");

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

                        dataAdapter1 = new ArrayAdapter(SettingActivity.this,
                                R.layout.help_spinner, R.id.tvCust, array1);
                        spinner_help.setAdapter(dataAdapter1);
                        spinner_help.setPrompt("Visiting Help");
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
                loaderDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<>();

                params.put("complex_id", globalClass.getComplex_id());

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


    @Override
    public void onMemberDelete(HashMap<String, String> hashMap) {

        deleteMemberDialog(hashMap.get("family_member_id"));
    }

    public void deleteMemberDialog(String member_id){

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.datepicker);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Are you sure you want to delete this member?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteFamilyMember(member_id);
            }
        });
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void deleteFamilyMember(String member_id) {

        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.delete_family_member,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "delete_family_member: " + response.toString());

                loaderDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");

                    if(status.equals("1")) {

                        profile_details_api_call();


                    } else {
                        TastyToast.makeText(getApplicationContext(),
                                message,
                                TastyToast.LENGTH_LONG, TastyToast.WARNING);

                    }

                } catch (Exception e) {
                   e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                loaderDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<>();

                params.put("user_id", member_id);
                params.put("flat_id", globalClass.getFlat_id());

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq.setRetryPolicy(
                        new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }


    @Override
    public void onCarDelete(HashMap<String, String> hashMap) {

        deleteCarDialog(hashMap.get("id"));
    }

    public void deleteCarDialog(String car_id){

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.datepicker);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Are you sure you want to delete this car?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteCar(car_id);
            }
        });
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void deleteCar(String car_id) {

        loaderDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.delete_car,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "delete_car: " + response.toString());

                        loaderDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.optString("status");
                            String message = jsonObject.optString("message");

                            if(status.equals("1")) {

                                profile_details_api_call();


                            } else {
                                TastyToast.makeText(getApplicationContext(),
                                        message,
                                        TastyToast.LENGTH_LONG, TastyToast.WARNING);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "DATA NOT FOUND: " + error.getMessage());
                loaderDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<>();

                params.put("user_id", globalClass.getId());
                params.put("car_id", car_id);

                Log.d(TAG, "getParams: "+params);
                return params;
            }

        };

        // Adding request to request queue
        VolleySingleton.getInstance(SettingActivity.this)
                .addToRequestQueue(strReq.setRetryPolicy(
                        new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));


    }

}