package com.example.venclima.notifications;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private static String token;

    public static String getToken() {
        return token;
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseNotificationService.token = token;
    }
}
