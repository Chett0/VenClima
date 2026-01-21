package com.venclima.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.venclima.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FireBaseMessagingService {

    private static final Logger logger =
            LoggerFactory.getLogger(FireBaseMessagingService.class);

    /**
     * Sends a push notification to a device using Firebase Cloud Messaging.
     * <p>
     * The notification includes a title and body provided in the {@link NotificationRequest}.
     *
     * @param request the notification request containing:
     *                <ul>
     *                    <li>token: the device FCM token to send the notification to</li>
     *                    <li>title: the notification title</li>
     *                    <li>body: the notification body</li>
     *                </ul>
     * @return a {@link String} indicating the result of the send operation:
     *         <ul>
     *             <li>Firebase message ID if successful</li>
     *             <li>"NOT FOUND" if the token does not exist</li>
     *             <li>"Firebase error sending" for other Firebase errors</li>
     *         </ul>
     */
    public String sendPushNotificationService(NotificationRequest request) {
        try {
            Message message = Message
                    .builder()
                    .setToken(request.getToken())
                    .putData("title", request.getTitle())
                    .putData("body", request.getBody())
                    .build();

            String result = FirebaseMessaging.getInstance().send(message);
            logger.info(result);
            return result;
        } catch (FirebaseMessagingException e) {
            logger.error("Firebase error sending: {}", e.toString());
            if(e.getErrorCode().toString().equals("NOT_FOUND"))
                return "NOT FOUND";
            return "Firebase error sending";
        }
    }


}
