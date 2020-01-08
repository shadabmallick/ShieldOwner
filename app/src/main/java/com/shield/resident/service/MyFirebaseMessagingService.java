package com.shield.resident.service;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.shield.resident.ui.CallUi;
import com.shield.resident.ui.Login;
import com.shield.resident.ui.Splash;
import com.shield.resident.util.NotificationUtils;

import java.util.HashMap;

import static com.shield.resident.GlobalClass.GlobalClass.CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "Notifi";
    Shared_Preference shared_preference;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null)
            return;

        shared_preference = new Shared_Preference(getApplicationContext());

        if (!shared_preference.isLogin()){
            return;
        }


        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            String message = remoteMessage.getData().get("body"); // message
            String title = remoteMessage.getData().get("title"); // Notification


            String type = remoteMessage.getData().get("type"); // call/new call

            showNotification(title, message);

            if (type != null && type.equals("call")){

                String activity_id = remoteMessage.getData().get("activity_id");
                String table = remoteMessage.getData().get("table");
                String security_id = remoteMessage.getData().get("security_id");
                String block = remoteMessage.getData().get("block");
                String flat_name = remoteMessage.getData().get("flat_name");
                String flat_id = remoteMessage.getData().get("flat_id");
                String complex_name = remoteMessage.getData().get("complex_name");
                String complex_id = remoteMessage.getData().get("complex_id");
                String visitor_id = remoteMessage.getData().get("visitor_id");
                String url = remoteMessage.getData().get("url");


                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("activity_id", activity_id);
                hashMap.put("table", table);
                hashMap.put("security_id", security_id);
                hashMap.put("block", block);
                hashMap.put("flat_name", flat_name);
                hashMap.put("flat_id", flat_id);
                hashMap.put("complex_name", complex_name);
                hashMap.put("complex_id", complex_id);
                hashMap.put("type", type);
                hashMap.put("message", message);
                hashMap.put("visitor_id", visitor_id);
                hashMap.put("url", url);

                callTo(hashMap);

            } else if (type != null && type.equals("new call")){

                String activity_id = remoteMessage.getData().get("activity_id");
                String table = remoteMessage.getData().get("table");
                String security_id = remoteMessage.getData().get("security_id");
                String block = remoteMessage.getData().get("block");
                String flat_name = remoteMessage.getData().get("flat_name");
                String flat_id = remoteMessage.getData().get("flat_id");
                String complex_name = remoteMessage.getData().get("complex_name");
                String complex_id = remoteMessage.getData().get("complex_id");
                String visitor_id = remoteMessage.getData().get("visitor_id");
                String url = remoteMessage.getData().get("url");


                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("activity_id", activity_id);
                hashMap.put("table", table);
                hashMap.put("security_id", security_id);
                hashMap.put("block", block);
                hashMap.put("flat_name", flat_name);
                hashMap.put("flat_id", flat_id);
                hashMap.put("complex_name", complex_name);
                hashMap.put("complex_id", complex_id);
                hashMap.put("visitor_id", visitor_id);
                hashMap.put("type", type);
                hashMap.put("message", message);
                hashMap.put("url", url);

                callTo(hashMap);

            } else if (type != null && type.equals("new delivery call")){

                String activity_id = remoteMessage.getData().get("activity_id");
                String table = remoteMessage.getData().get("table");
                String security_id = remoteMessage.getData().get("security_id");
                String block = remoteMessage.getData().get("block");
                String flat_name = remoteMessage.getData().get("flat_name");
                String flat_id = remoteMessage.getData().get("flat_id");
                String complex_name = remoteMessage.getData().get("complex_name");
                String complex_id = remoteMessage.getData().get("complex_id");
                String visitor_id = remoteMessage.getData().get("visitor_id");
                String url = remoteMessage.getData().get("url");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("activity_id", activity_id);
                hashMap.put("table", table);
                hashMap.put("security_id", security_id);
                hashMap.put("block", block);
                hashMap.put("flat_name", flat_name);
                hashMap.put("flat_id", flat_id);
                hashMap.put("complex_name", complex_name);
                hashMap.put("complex_id", complex_id);
                hashMap.put("visitor_id", visitor_id);
                hashMap.put("type", type);
                hashMap.put("message", message);
                hashMap.put("url", url);

                callTo(hashMap);

            }else if (type != null && type.equals("delivery call")){

                String activity_id = remoteMessage.getData().get("activity_id");
                String table = remoteMessage.getData().get("table");
                String security_id = remoteMessage.getData().get("security_id");
                String block = remoteMessage.getData().get("block");
                String flat_name = remoteMessage.getData().get("flat_name");
                String flat_id = remoteMessage.getData().get("flat_id");
                String complex_name = remoteMessage.getData().get("complex_name");
                String complex_id = remoteMessage.getData().get("complex_id");
                String visitor_id = remoteMessage.getData().get("visitor_id");
                String url = remoteMessage.getData().get("url");


                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("activity_id", activity_id);
                hashMap.put("table", table);
                hashMap.put("security_id", security_id);
                hashMap.put("block", block);
                hashMap.put("flat_name", flat_name);
                hashMap.put("flat_id", flat_id);
                hashMap.put("complex_name", complex_name);
                hashMap.put("complex_id", complex_id);
                hashMap.put("visitor_id", visitor_id);
                hashMap.put("type", type);
                hashMap.put("message", message);
                hashMap.put("url", url);

                callTo(hashMap);

            }else if (type.equals("tenant_blocked")){

                shared_preference.setPref_logInStatus(false);
                gotoLoginScreen();

            }else if (type.equals("member_blocked")){

                shared_preference.setPref_logInStatus(false);
                gotoLoginScreen();
            }

            sendResponseToActivityScreen(getApplicationContext(), type);

        }

    }


    private void handleNotification(String title, String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            showNotification(title, message);
        }else{
            showNotification(title, message);
        }
    }



    public void showNotification(String title, String message) {

        Intent intent = new Intent(this, Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        long[] vibrate = {0, 300, 200, 300, 500, 300};

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_collapsed);
        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expanded);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Intent clickIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);

        collapsedView.setTextViewText(R.id.text_view_collapsed_1, message);

        expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.bg_rounded_white);
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.noti_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(defaultSoundUri)
                .setVibrate(vibrate)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Shield Residents",
                    NotificationManager.IMPORTANCE_HIGH);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel.setSound(defaultSoundUri, attributes);
            channel.setDescription(message);
            channel.setVibrationPattern(vibrate);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1, notification);
    }


    private void callTo(HashMap<String, String> hashMap){

        Intent intent = new Intent(getApplicationContext(), CallUi.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("hashMap", hashMap);
        startActivity(intent);

    }

    static void sendResponseToActivityScreen(Context context, String type) {
        Intent intent = new Intent("activity_screen");
        intent.putExtra("type", type);
        context.sendBroadcast(intent);
    }

    private void gotoLoginScreen(){

        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
