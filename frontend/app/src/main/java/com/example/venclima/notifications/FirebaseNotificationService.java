package com.example.venclima.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.venclima.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private static String token;

    public static String getToken() {
        return token;
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.i("FirebaseNotificationService", "Token: "+ token);
        FirebaseNotificationService.token = token;
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FirebaseNotificationService", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload (which yours does)
        if (remoteMessage.getData().size() > 0) {
            Log.d("FirebaseNotificationService", "Message data payload: " + remoteMessage.getData());

            // Extract data passed from your backend call
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            // Since it's data-only, we must manually create the notification
            sendNotification(title, body);
        }
    }

    private void sendNotification(String title, String body) {
        // 1. Create a Notification Channel (required for Android 8.0+ / API 26+)
        String channelId = "default_channel_id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Default notification channel for app alerts.");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 2. Create the Notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_alert) // Use your app's icon
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // 3. Display the notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Use a unique ID for each notification
        notificationManager.notify(0, notificationBuilder.build());
    }


}
