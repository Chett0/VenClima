package com.example.venclima.models;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NotificationResponse {

    private List<IslandNotification> notifications;
    private Boolean isActiveNotifications;

    public Boolean getIsActiveNotifications() {
        return isActiveNotifications;
    }

    public List<IslandNotification> getNotifications() {
        return notifications;
    }
}
