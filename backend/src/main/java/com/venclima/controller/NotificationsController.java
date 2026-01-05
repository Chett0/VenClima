package com.venclima.controller;

import com.venclima.dto.IslandNotificationDTO;
import com.venclima.dto.NotificationDTO;
import com.venclima.service.AuthService;
import com.venclima.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationService notificationService;

    public NotificationsController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PutMapping
    public ResponseEntity<String> updateNotification(@RequestBody NotificationDTO islandsIds) {
        this.notificationService.updateNotification(islandsIds);
        return ResponseEntity.ok("Notification updated successfully");
    }

    @GetMapping
    public ResponseEntity<List<IslandNotificationDTO>> getNotifications() {
        try {
            List<IslandNotificationDTO> islandNotification = this.notificationService.getNotification();
            return ResponseEntity.ok(islandNotification);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
