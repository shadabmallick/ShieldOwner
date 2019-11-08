package com.sketch.securityowner.ui;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.sketch.securityowner.GlobalClass.GlobalClass;
import com.sketch.securityowner.R;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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


    ProgressDialog progressDialog;
    GlobalClass globalClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_ui);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);
        actionViews();

    }


    private void actionViews(){

        PowerManager pm = (PowerManager) getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(
                (PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP), ":TAG1");
        wakeLock.acquire(30*1000);


        globalClass = (GlobalClass) getApplicationContext();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            HashMap<String, String> hashMap = (HashMap<String, String>) bundle.getSerializable("hashMap");

            tv_name.setText(hashMap.get("message"));

            tv_vendor_name.setText(hashMap.get("type").toUpperCase());

            if (hashMap.get("message").equals("new delivery call")){
                ll_leave_at_gate.setVisibility(View.VISIBLE);
            }else {
                ll_leave_at_gate.setVisibility(View.GONE);
            }


            /*String profile_image = bundle.getString("data");

            Glide.with(this)
                    .load(profile_image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(image_pic);*/

        }




        NotificationManager manager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();



        long[] jArr = {0, 300, 200, 300, 500, 300};
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                vibrator.vibrate(VibrationEffect.createWaveform(jArr,
                        new int[]{0, 145, 0, 145, 0, 145}, -1));
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            vibrator.vibrate(jArr, 0);
        }


        AudioManager audioManager = (AudioManager)getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);



        Uri defaultSoundUri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/raw/ring1");

        MediaPlayer mediaPlayer = new MediaPlayer();
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






        rel_rejected.setOnClickListener(v -> {
            mediaPlayer.stop();
            vibrator.cancel();


        });

        rel_approved.setOnClickListener(v -> {
            mediaPlayer.stop();
            vibrator.cancel();


        });

        rel_leaveGate.setOnClickListener(v -> {
            mediaPlayer.stop();
            vibrator.cancel();


        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        /// disable back button...
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
