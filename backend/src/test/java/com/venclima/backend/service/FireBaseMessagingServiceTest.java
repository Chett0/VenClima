package com.venclima.backend.service;

import com.venclima.model.NotificationRequest;
import com.venclima.model.Token;
import com.venclima.model.User;
import com.venclima.repository.TokenRepository;
import com.venclima.repository.UserRepository;
import com.venclima.service.FireBaseMessagingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FireBaseMessagingServiceTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @InjectMocks
    private FireBaseMessagingService fireBaseMessagingService;

    @Test
    void sendPushNotificationService() {

        String userEmail = "a";
        String firebaseNotFoundError = "NOT FOUND";
        String firebaseExceptionError = "Firebase error sending";

        Optional<User> user = userRepository.findByEmail(userEmail);
        assert user.isPresent() : "User not found";

        List<Token> tokens = tokenRepository.findAllByUser(user.get());
        assert !tokens.isEmpty() : "No tokens found";

        for(Token token : tokens) {
            String res = fireBaseMessagingService.sendPushNotificationService(new NotificationRequest(
                    "VenClima",
                    "Test Notification",
                    token.getToken()
                    )
            );

            assert !res.equals(firebaseNotFoundError) : "Token not found";
            assert !res.equals(firebaseExceptionError) : "Firebase error sending";
        }

    }
}
