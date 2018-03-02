package com.gercep.alarem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by lelouch on 2/22/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private int id = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("DEBUG FIREBASE MESSAGE", remoteMessage.getData().toString());
        Log.d("a", "a");

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("DEBUG FIREBASE MESSAGE", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("DEBUG FIREBASE MESSAGE", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }



        Intent notificationIntent = new Intent(this, LoginActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarem")
                .setContentText(remoteMessage.getData().get("message"))
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)     getSystemService(NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());
        id++;
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
