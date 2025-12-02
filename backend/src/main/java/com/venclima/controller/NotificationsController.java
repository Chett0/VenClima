package com.venclima.controller;

import com.venclima.dto.NotificationDTO;
import com.venclima.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationService notificationService;

    public NotificationsController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> addNotification(@RequestBody NotificationDTO islandsIds) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        this.notificationService.addNotification(userEmail, islandsIds);
        return ResponseEntity.ok("");
    }

}
