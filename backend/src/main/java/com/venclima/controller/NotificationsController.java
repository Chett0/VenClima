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

    /**
     * Updates notification preferences for the currently authenticated user.
     *
     * @param notifications DTO containing updated notification settings
     * @return {@link ResponseEntity} with a success message
     */
    @PutMapping
    public ResponseEntity<String> updateNotification(@RequestBody NotificationDTO notifications) {
        User user = authService.getAuthenticatedUser();
        this.notificationService.updateNotification(user, notifications);
        return ResponseEntity.ok("Notification updated successfully");
    }

    /**
     * Retrieves notifications and notification status for the currently authenticated user.
     *
     * @return {@link ResponseEntity} containing a {@link NotificationResponse} with:
     *         <ul>
     *             <li>Island notifications</li>
     *             <li>Active notification status</li>
     *         </ul>
     *         Returns {@code 404 Not Found} if notifications cannot be retrieved.
     */
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

}
