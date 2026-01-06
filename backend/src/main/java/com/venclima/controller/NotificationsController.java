package com.venclima.controller;

import com.venclima.dto.IslandNotificationDTO;
import com.venclima.dto.NotificationDTO;
import com.venclima.model.User;
import com.venclima.responses.NotificationResponse;
import com.venclima.service.AuthService;
import com.venclima.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {


    private final NotificationService notificationService;
    private final AuthService authService;

    public NotificationsController(NotificationService notificationService, AuthService authService) {
        this.notificationService = notificationService;
        this.authService = authService;
    }

    @PutMapping
    public ResponseEntity<String> updateNotification(@RequestBody NotificationDTO notifications) {
        User user = authService.getAuthenticatedUser();
        this.notificationService.updateNotification(user, notifications);
        return ResponseEntity.ok("Notification updated successfully");
    }

    @GetMapping
    public ResponseEntity<NotificationResponse> getNotifications() {
        try {
            User user = authService.getAuthenticatedUser();
            List<IslandNotificationDTO> islandNotification = this.notificationService.getNotification(user);
            Boolean isActiveNotifications = this.notificationService.getIsActiveNotification(user);
            NotificationResponse response = new NotificationResponse(
                    islandNotification,
                    isActiveNotifications
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping(name = "/isActive")
//    public ResponseEntity<Boolean> getIsActiveNotifications() {
//        try {
//            User user = authService.getAuthenticatedUser();
//            Boolean isActiveNotification = this.notificationService.getIsActiveNotification(user);
//            return ResponseEntity.ok(isActiveNotification);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

}
