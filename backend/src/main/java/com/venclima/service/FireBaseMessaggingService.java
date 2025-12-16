package com.venclima.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.venclima.model.NotificationRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FireBaseMessaggingService {

    public String sendPushNotificationService(NotificationRequest request) {
        Map<String, String> firebaseMessageBody = new HashMap<>();
        firebaseMessageBody.put("title", request.getTitle());
        firebaseMessageBody.put("body", request.getBody());
        try {
            Message message = Message
                    .builder()
                    .setToken(request.getToken())
                    .putAllData(firebaseMessageBody)
                    .build();

            return FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            return "Firebase error sending";
        }
    }


}
