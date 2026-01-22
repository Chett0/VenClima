package com.venclima.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FireBaseMessagingConfiguration {

    private static final Logger logger =
            LoggerFactory.getLogger(FireBaseMessagingConfiguration.class);

    /**
     * Initializes the Firebase application after the Spring context is ready.
     * <p>
     * Reads credentials from `firebase.json` located in the classpath and initializes
     * {@link FirebaseApp} for use in push notifications.
     * <p>
     * Any exceptions during initialization are logged as errors.
     *
     * @throws IOException if reading the credentials file fails
     */
    @PostConstruct
    public void firebaseInit() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource("firebase.json");

            FirebaseOptions options = FirebaseOptions
                    .builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();
            FirebaseApp.initializeApp(options);
            logger.info("FirebaseApp initialized");
//            isFirebaseActive = true;
        } catch (Exception e) {
            logger.error("Firebase error sending notification", e);
        }
    }

}
