package com.venclima.controller;

import com.venclima.dto.NotificationDTO;
import com.venclima.service.NotificationService;
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
    public void addNotification(@RequestBody NotificationDTO stationIds) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        this.notificationService.addNotification(userEmail, stationIds);
        return;
    }

}
