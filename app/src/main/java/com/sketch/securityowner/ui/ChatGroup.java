package com.sketch.securityowner.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdsmdg.tastytoast.TastyToast;

import com.sketch.securityowner.Adapter.ChatGroupAdapter;
import com.sketch.securityowner.Constant.AppConfig;
import com.sketch.securityowner.GlobalClass.Config;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.GlobalClass.Shared_Preference;
import com.sketch.securityowner.GlobalClass.VolleySingleton;
import com.sketch.securityowner.R;
import com.sketch.securityowner.model.ChatData;
import com.sketch.securityowner.model.ChatListData;
import com.sketch.securityowner.util.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;


import static com.sketch.securityowner.GlobalClass.VolleySingleton.backOff;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.nuOfRetry;
import static com.sketch.securityowner.GlobalClass.VolleySingleton.timeOut;

public class ChatGroup extends AppCompatActivity implements
        ChatGroupAdapter.onItemClickListner {

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    @BindView(R.id.imgAttachment) ImageView imgAttachment;
    @BindView(R.id.imgSend) ImageView imgSend;
    @BindView(R.id.edt_message) EditText edt_message;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    ChatListData chatListData;
    ProgressDialog progressDialog;
    ArrayList<ChatData> chatListDataArrayList;

    GlobalClass globalClass;
    Shared_Preference prefManager;
    ChatGroupAdapter chatGroupAdapter;

    Bitmap resized_bitmap;
    File p_image1 = null;
    String help_id;

    private int PICK_IMAGE_REQUEST = 444;
    private static final int CAMERA_REQUEST = 1888;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 333;


    String TAG = "group chat list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);
        ButterKnife.bind(this);
        intViews();

        this.registerReceiver(mMessageReceiver, new IntentFilter("chat_screen"));

    }


    private void intViews() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");

        globalClass = (GlobalClass) getApplicationContext();
        prefManager = new Shared_Preference(this);


        LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(layoutManager);

        chatListDataArrayList = new ArrayList<>();
        chatGroupAdapter = new ChatGroupAdapter(ChatGroup.this,
                chatListDataArrayList, this);
        recycler_view.setAdapter(chatGroupAdapter);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            help_id = bundle.getString("id");

            getSupportActionBar().setTitle("Help Id"+" "+help_id);

        }


        imgAttachment.setOnClickListener(v -> {

            if (!ConnectivityReceiver.isConnected()){
                TastyToast.makeText(ChatGroup.this,
                        "Please connect to internet",
                        TastyToast.LENGTH_LONG, TastyToast.ERROR);

                return;
            }

            checkPermission();

        });

        imgSend.setOnClickListener(v -> {

            if (!ConnectivityReceiver.isConnected()){
                TastyToast.makeText(ChatGroup.this,
                        "Please connect to internet",
                        TastyToast.LENGTH_LONG, TastyToast.ERROR);
                return;
            }

            if (edt_message.getText().toString().trim().isEmpty()){
                TastyToast.makeText(ChatGroup.this,
                        "Enter message",
                        TastyToast.LENGTH_LONG, TastyToast.WARNING);

                return;
            }

            sendTextMessage();

        });


        getChatList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }


    private void getChatList() {

        progressDialog.show();

        chatListDataArrayList = new ArrayList<>();

        final String url = AppConfig.help_id_wise_chat;

        HashMap<String, String> param = new HashMap<>();
        param.put("help_id", help_id);

        Log.d(AppConfig.TAG ,"wise_chat- " + url);
        Log.d(AppConfig.TAG ,"wise_chat - " + param);


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(AppConfig.TAG, "wise_chat Response: " + response);

                try {

                    JSONObject main_object = new JSONObject(response);

                    int status = main_object.optInt("status");
                    String message = main_object.optString("message");

                    if (status == 1) {

                        JSONArray data = main_object.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);

                            ChatData chatData = new ChatData();
                            chatData.setChat_id(object.optString("chat_id"));
                            chatData.setType(object.optString("type")); // user type
                            chatData.setHelp_id(object.optString("help_id"));
                          //  chatData.setSender_name(object.optString("sender_name"));
                            chatData.setContent(object.optString("content"));
                            chatData.setImage(object.optString("image"));
                            chatData.setDate(object.optString("date"));
                            chatData.setTime(object.optString("time"));
                            chatData.setStatus(object.optString("status"));

                            chatData.setImage_from("web");


                            if (globalClass.getId()
                                    .equals(object.optString("help_id"))){
                                chatData.setIs_me(true);
                            }else {
                                chatData.setIs_me(false);
                            }


                            chatListDataArrayList.add(chatData);
                        }

                        chatGroupAdapter = new ChatGroupAdapter(ChatGroup.this,
                                chatListDataArrayList, ChatGroup.this);
                        recycler_view.setAdapter(chatGroupAdapter);
                        chatGroupAdapter.notifyDataSetChanged();
                    }


                    progressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(AppConfig.TAG, "DATA NOT FOUND: " + error.getMessage());
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                return param;
            }

        };

        VolleySingleton.getInstance(ChatGroup.this)
                .addToRequestQueue(strReq
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }


    private void setGroupChatData(ChatData chatData){

        chatListDataArrayList.add(chatData);

        chatGroupAdapter = new ChatGroupAdapter(ChatGroup.this,
                chatListDataArrayList, ChatGroup.this);
        recycler_view.setAdapter(chatGroupAdapter);
        chatGroupAdapter.notifyDataSetChanged();


    }


    @Override
    public void onItemClick(ChatData chatData) {

    }

    private static DateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private static DateFormat dateOnlyformat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public void sendTextMessage() {

        Date today = Calendar.getInstance().getTime();
        String message = edt_message.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {

            ChatData chatData = new ChatData();

            chatData.setChat_id("");
            chatData.setType("user"); // user type
            chatData.setSender_id(globalClass.getId());
            chatData.setSender_name(globalClass.getName());
            chatData.setContent(message);
            chatData.setImage("");

            String date_time = dateFormat.format(today);
            String[] array = date_time.split(" ");

            chatData.setDate(array[0]);
            chatData.setTime(array[1]);

            chatData.setStatus("");
            chatData.setImage_from("");
            chatData.setIs_me(true);

            p_image1 = null;



            edt_message.setText("");
            chatListDataArrayList.add(chatData);
            chatGroupAdapter.notifyDataSetChanged();

            recycler_view.smoothScrollToPosition(chatListDataArrayList.size() - 1);
           // Log.d(TAG ,"array- " + chatListDataArrayList.size());

            postChat(chatData);
        }
    }



    private boolean checkPermission() {

        List<String> permissionsList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(ChatGroup.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(ChatGroup.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(ChatGroup.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.CAMERA);
        }

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) ChatGroup.this,
                    permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

            return false;
        } else {

            selectImage();

        }


        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                        (permissions.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    //list is still empty
                    selectImage();
                } else {
                    checkPermission();
                    // Permission Denied
                }
                break;
        }
    }

    public void selectImage() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChatGroup.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_picture_select, null);
        dialogBuilder.setView(dialogView);

        ImageView iv_gallery = dialogView.findViewById(R.id.iv_gallery);
        ImageView iv_camera = dialogView.findViewById(R.id.iv_camera);

        final AlertDialog alertDialog = dialogBuilder.create();

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        PICK_IMAGE_REQUEST);

                alertDialog.dismiss();

            }
        });


        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

                alertDialog.dismiss();

            }
        });


        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        p_image1 = null;

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Log.d(TAG , "PICK_IMAGE_REQUEST - "+uri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                sendImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            sendImage(photo);
            Log.d(TAG , "CAMERA_REQUEST - "+data.getExtras().get("data"));

        }

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        try {
            Cursor cursor = getApplicationContext().getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx); // Exception raised HERE
                cursor.close(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void sendImage(Bitmap bitmap) {

        try {
            //InputStream inputStream = getContentResolver().openInputStream(uri);
           // Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            //if (inputStream != null) inputStream.close();
            resized_bitmap = getResizedBitmap(bitmap,480);
        }catch (Exception e){
            e.printStackTrace();
        }


        long time = 0;
        time =  System.currentTimeMillis();
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Supernathral/Images/sent";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        final File image_file = new File(dir, "sup" + time+ ".jpg");

        p_image1 = image_file;

        OutputStream os;
        try {
            os = new FileOutputStream(image_file);
            resized_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
            os.flush();
            os.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(image_file)));
        } catch (Exception e) {
            e.printStackTrace();
        }


        Date today = Calendar.getInstance().getTime();

        ChatData chatData = new ChatData();

        chatData.setChat_id("");
        chatData.setType("user"); // user type
        chatData.setSender_id(globalClass.getId());
        chatData.setSender_name(globalClass.getName());
        chatData.setContent("");
        chatData.setImage(image_file.getAbsolutePath());

        String date_time = dateFormat.format(today);
        String[] array = date_time.split(" ");

        chatData.setDate(array[0]);
        chatData.setTime(array[1]);

        chatData.setStatus("");
        chatData.setImage_from("local");
        chatData.setIs_me(true);


        edt_message.setText("");
        chatListDataArrayList.add(chatData);
        chatGroupAdapter.notifyDataSetChanged();

        recycler_view.smoothScrollToPosition(chatListDataArrayList.size() - 1);

        //Log.d(TAG ,"array- " + chatListDataArrayList.size());

        postChat(chatData);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (width < maxSize && height < maxSize){

            return image;

        }else {

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }

            return Bitmap.createScaledBitmap(image, width, height, true);
        }

    }

    private void postChat(final ChatData chatData) {

        String url = AppConfig.user_chat;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("user_id", globalClass.getId());
        params.put("complex_id", globalClass.getComplex_id());
        params.put("help_id", help_id);
        params.put("type", chatData.getType());

        params.put("message", chatData.getContent());

        if (p_image1 != null){
            try {
                params.put("image", p_image1);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }



        Log.d(TAG ,"URL- " + url);
        Log.d(TAG ,"PARAM - " + params.toString());

        client.setSSLSocketFactory(
                new SSLSocketFactory(Config.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));


        client.setMaxRetriesAndTimeout(5 , 15000);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "onFailure- " + res);


                AlertDialog alert =
                        new AlertDialog.Builder(ChatGroup.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }

        });

    }




    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        this.unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String chat_data = intent.getStringExtra("data");
            //do other stuff here
            Log.d(TAG, "chat_data = "+chat_data);


            try {
                JSONObject object = new JSONObject(chat_data);

                ChatData chatData = new ChatData();
                chatData.setChat_id(object.optString("chat_id"));
                chatData.setType(object.optString("type")); // user type
                chatData.setHelp_id(object.optString("help_id"));
               chatData.setSender_name(object.optString("sender_name"));
                chatData.setContent(object.optString("content"));
                chatData.setImage(object.optString("image"));
                chatData.setDate(object.optString("date"));
                chatData.setTime(object.optString("time"));
                chatData.setStatus(object.optString("status"));
                chatData.setImage_from("web");

                if (globalClass.getId()
                        .equals(object.optString("sender_id"))){
                    chatData.setIs_me(true);
                }else {
                    chatData.setIs_me(false);
                }


                setGroupChatData(chatData);

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    };



}