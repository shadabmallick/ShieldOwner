package com.shield.resident.ui;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.sdsmdg.tastytoast.TastyToast;
import com.shield.resident.Constant.AppConfig;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.GlobalClass.VolleySingleton;
import com.shield.resident.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.shield.resident.GlobalClass.VolleySingleton.backOff;
import static com.shield.resident.GlobalClass.VolleySingleton.nuOfRetry;
import static com.shield.resident.GlobalClass.VolleySingleton.timeOut;

public class CallUi extends AppCompatActivity {


    @BindView(R.id.image_pic)
    CircleImageView image_pic;
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.tv_vendor_name) TextView tv_vendor_name;
    @BindView(R.id.rel_rejected) RelativeLayout rel_rejected;
    @BindView(R.id.rel_approved) RelativeLayout rel_approved;
    @BindView(R.id.rel_leaveGate) RelativeLayout rel_leaveGate;
    @BindView(R.id.ll_leave_at_gate)
    LinearLayout ll_leave_at_gate;


    private ProgressDialog progressDialog;
    private GlobalClass globalClass;
    private Shared_Preference shared_preference;
    private HashMap<String, String> hashMap;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    boolean test_noti = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       /* Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);*/

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        setContentView(R.layout.call_ui);


        ButterKnife.bind(this);
        actionViews();


    }



    private void actionViews() {

        PowerManager pm = (PowerManager) getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(
                (PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.FULL_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP), ":TAG1");
        wakeLock.acquire(40 * 1000);


        globalClass = (GlobalClass) getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Submitting your response...");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            test_noti = false;

            hashMap = (HashMap<String, String>) bundle.getSerializable("hashMap");

            tv_name.setText(hashMap.get("message"));
            tv_vendor_name.setText(hashMap.get("type").toUpperCase());



            ll_leave_at_gate.setVisibility(View.GONE);

            if (hashMap.get("type").equals("new delivery call")
                    || hashMap.get("type").equals("delivery call")) {
                ll_leave_at_gate.setVisibility(View.VISIBLE);
            } else {
                ll_leave_at_gate.setVisibility(View.GONE);
            }


            String profile_image = hashMap.get("url");

            Glide.with(this)
                    .load(profile_image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(image_pic);


            if (hashMap.get("message").equals("new delivery call")) {

            }


            NotificationManager manager = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancelAll();


            long[] jArr = {0, 300, 200, 300, 500, 300};
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    vibrator.vibrate(VibrationEffect.createWaveform(jArr,
                            new int[]{0, 145, 0, 145, 0, 145}, -1));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                vibrator.vibrate(jArr, 0);
            }


            AudioManager audioManager = (AudioManager) getApplicationContext()
                    .getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


            Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getPackageName() + "/raw/ring1");



            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


            try {
                mediaPlayer.setDataSource(getApplicationContext(), defaultSoundUri);
                mediaPlayer.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    mp.start();

                }
            });



            rel_rejected.setOnClickListener(v -> {
                mediaPlayer.stop();
                vibrator.cancel();

                if (hashMap.get("type").contains("new")) {
                    status_updateNewCall("n");
                }else {
                    status_updateCall("n");
                }


            });

            rel_approved.setOnClickListener(v -> {
                mediaPlayer.stop();
                vibrator.cancel();

                if (hashMap.get("type").contains("new")) {
                    status_updateNewCall("y");
                }else {
                    status_updateCall("y");
                }


            });

            rel_leaveGate.setOnClickListener(v -> {
                mediaPlayer.stop();
                vibrator.cancel();

                if (hashMap.get("type").contains("new")) {
                    status_updateNewCall("l");
                }else {
                    status_updateCall("l");
                }


            });

        }else {
            ll_leave_at_gate.setVisibility(View.GONE);
            test_noti = true;



            long[] jArr = {0, 300, 200, 300, 500, 300};
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    vibrator.vibrate(VibrationEffect.createWaveform(jArr,
                            new int[]{0, 145, 0, 145, 0, 145}, -1));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                vibrator.vibrate(jArr, 0);
            }


            AudioManager audioManager = (AudioManager) getApplicationContext()
                    .getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


            Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getPackageName() + "/raw/ring1");



            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


            try {
                mediaPlayer.setDataSource(getApplicationContext(), defaultSoundUri);
                mediaPlayer.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    mp.start();

                }
            });



            rel_rejected.setOnClickListener(v -> {
                mediaPlayer.stop();
                vibrator.cancel();

                finish();
                super.finishAndRemoveTask();
            });

            rel_approved.setOnClickListener(v -> {
                mediaPlayer.stop();
                vibrator.cancel();

                finish();
                super.finishAndRemoveTask();

            });
        }


        threadForActivityFinish();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        /// disable back button...
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }





    public void status_updateNewCall(String status){

        progressDialog.show();

        String url = AppConfig.new_visitor_status_update;

        final Map<String, String> params = new HashMap<>();

        params.put("user_id", globalClass.getId());
        params.put("table", hashMap.get("table"));
        params.put("activity_id", hashMap.get("activity_id"));

        if (hashMap.get("visitor_id") != null){
            params.put("visitor_id", hashMap.get("visitor_id"));
        }else {
            params.put("visitor_id", "");
        }

        params.put("security_id", hashMap.get("security_id"));
        params.put("complex_id", hashMap.get("complex_id"));
        params.put("flat_name", hashMap.get("flat_name"));
        params.put("block", hashMap.get("block"));
        params.put("status", status);


        //Log.d(AppConfig.TAG , "status_update- " + url);
        Log.d(AppConfig.TAG , "status_update- " + params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(AppConfig.TAG , "status_update- " +response);

                        if (response != null){
                            try {

                                JSONObject main_object = new JSONObject(response);

                                progressDialog.dismiss();

                                int status = main_object.optInt("status");
                                String message = main_object.optString("message");
                                if (status == 1){

                                    TastyToast.makeText(getApplicationContext(),
                                            message,
                                            TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                                    finish();

                                }else {

                                    TastyToast.makeText(getApplicationContext(),
                                            message,
                                            TastyToast.LENGTH_LONG, TastyToast.WARNING);

                                    finish();
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                finish();

                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //on error storing the name to sqlite with status unsynced

                Log.e(AppConfig.TAG, "error - "+error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };

        VolleySingleton.getInstance(CallUi.this)
                .addToRequestQueue(stringRequest
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }


    public void status_updateCall(String status){

        progressDialog.show();

        String url = AppConfig.visitor_status_update;

        final Map<String, String> params = new HashMap<>();

        params.put("user_id", globalClass.getId());
        params.put("table", hashMap.get("table"));
        params.put("activity_id", hashMap.get("activity_id"));

        if (hashMap.get("visitor_id") != null){
            params.put("visitor_id", hashMap.get("visitor_id"));
        }else {
            params.put("visitor_id", "");
        }

        params.put("security_id", hashMap.get("security_id"));
        params.put("complex_id", hashMap.get("complex_id"));
        params.put("flat_name", hashMap.get("flat_name"));
        params.put("block", hashMap.get("block"));
        params.put("status", status);


       // Log.d(AppConfig.TAG , "status_update- " + url);
        Log.d(AppConfig.TAG , "status_update- " + params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(AppConfig.TAG , "status_update- " +response);

                if (response != null){
                    try {

                        JSONObject main_object = new JSONObject(response);

                        int status = main_object.optInt("status");
                        String message = main_object.optString("message");
                        if (status == 1){

                            TastyToast.makeText(getApplicationContext(),
                                    message,
                                    TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            finish();

                        }else {

                            TastyToast.makeText(getApplicationContext(),
                                    message,
                                    TastyToast.LENGTH_LONG, TastyToast.WARNING);

                            finish();
                        }

                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        finish();
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //on error storing the name to sqlite with status unsynced

                Log.e(AppConfig.TAG, "error - "+error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };

        VolleySingleton.getInstance(CallUi.this)
                .addToRequestQueue(stringRequest
                        .setRetryPolicy(
                                new DefaultRetryPolicy(timeOut, nuOfRetry, backOff)));

    }




    private Handler handler;
    private Runnable myRunnable;
    private void threadForActivityFinish(){

        handler =  new Handler();
        myRunnable = new Runnable() {
            public void run() {

                try {

                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        handler.postDelayed(myRunnable, (40 * 1000));

    }

    @Override
    protected void onDestroy() {

        if (vibrator != null){
            vibrator.cancel();
        }

        if (handler != null){
            handler.removeCallbacks(myRunnable);
        }

        if (mediaPlayer != null){
            mediaPlayer.stop();
        }

        super.onDestroy();
    }


    @Override
    public void finish() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        } else {
            super.finish();
        }
    }
}
