package com.venclima.backend.service;

import com.venclima.dto.NotificationDTO;
import com.venclima.model.*;
import com.venclima.repository.IslandRepository;
import com.venclima.repository.TideRepository;
import com.venclima.repository.TokenRepository;
import com.venclima.repository.UserRepository;
import com.venclima.service.FireBaseMessagingService;
import com.venclima.service.IslandService;
import com.venclima.service.NotificationService;
import com.venclima.service.TideService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FireBaseMessagingServiceTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @InjectMocks
    private FireBaseMessagingService fireBaseMessagingService;
    @Autowired
    private TideService tideService;
    @MockitoBean
    private IslandService islandService;
    @Autowired
    private IslandRepository islandRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TideRepository tideRepository;

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


    @Test
    @Transactional
    void sendNotificationsBasedOnCriticIslands() {

        String userEmail = "a";

        Optional<User> user = userRepository.findByEmail(userEmail);
        assert user.isPresent() : "User not found";

        List<Island> islands = islandRepository.findAll();

        when(islandService.getCriticIslands(any(), any(Tide.class), any(Station.class))).thenReturn(islands);

        NotificationDTO notifications = new NotificationDTO(
                new ArrayList<>(),
                true
        );

        for(Island island : islands) {
            notifications.getIslandsIds().add(island.getId());
        }

        notificationService.updateNotification(user.get(), notifications);

        assertDoesNotThrow(() -> {
            tideService.retrieveRealTimeTideLevel();
        }, "Exception should not be thrown");

    }
}
