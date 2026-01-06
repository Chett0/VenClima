package com.example.venclima.models;

import java.util.List;

public class NotificationUpdateRequest {

    private List<Integer> islandsIds;
    private Boolean isActiveNotifications;

    public NotificationUpdateRequest(List<Integer> islandsIds, Boolean isActiveNotifications){
        this.islandsIds = islandsIds;
        this.isActiveNotifications = isActiveNotifications;
    }

    public Boolean getActiveNotifications() {
        return isActiveNotifications;
    }

    public List<Integer> getIslandsIds() {
        return islandsIds;
    }
}
