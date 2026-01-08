package com.example.venclima.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.venclima.models.IslandNotification;
import com.example.venclima.models.NotificationResponse;
import com.example.venclima.models.NotificationUpdateRequest;
import com.example.venclima.network.Callbacks.NotificationUpdateCallback;
import com.example.venclima.network.repositories.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<List<IslandNotification>> notifications = new MutableLiveData<>();
    private MutableLiveData<Boolean> isActiveNotifications = new MutableLiveData<>();
    private MutableLiveData<NotificationResponse> notificationResponse = new MutableLiveData<>();
    private NotificationUpdateCallback notificationUpdateCallback;


    public NotificationsViewModel(){
        this.loadNotifications();
    }

    public void loadNotifications() {
        this.setNotificationResponse(NotificationRepository.getNotification());
        this.notificationResponse.observeForever(notificationResponse -> {
            this.notifications.setValue(notificationResponse.getNotifications());
            this.isActiveNotifications.setValue(notificationResponse.getIsActiveNotifications());
        });
    }

    public LiveData<List<IslandNotification>> getNotifications() {
        return this.notifications;
    }

    public void setNotificationResponse(MutableLiveData<NotificationResponse> notificationResponse) {
        this.notificationResponse = notificationResponse;
    }

    public void setNotificationUpdateCallback (NotificationUpdateCallback callback) {
        this.notificationUpdateCallback = callback;
    }

    public void updateNotifications(){
        List<Integer> islandIds = new ArrayList<>();
        if(notifications.getValue() != null) {
            for (IslandNotification notification : notifications.getValue()) {
                if (notification.getIsNotified())
                    islandIds.add(notification.getIslandId());
            }
        }

        NotificationUpdateRequest request = new NotificationUpdateRequest(islandIds, isActiveNotifications.getValue());

        NotificationRepository.updateNotification(request, this.notificationUpdateCallback);
    }


    public void onIslandSelectionChanged(Integer islandId, boolean isChecked) {
        List<IslandNotification> currentNotifications = this.notifications.getValue();
        if(currentNotifications == null) return;

        List<IslandNotification> updatedNotifications = new ArrayList<>(currentNotifications);

        for(int i = 0; i < updatedNotifications.size(); i++) {
            IslandNotification current = updatedNotifications.get(i);
            if(current.getIslandId().equals(islandId))
                current.SetIsNotified(isChecked);
        }

        notifications.setValue(updatedNotifications);
    }

    public MutableLiveData<Boolean> getIsActiveNotifications() {
        return isActiveNotifications;
    }

    public void setIsActiveNotifications(Boolean isActive) {
        if (isActiveNotifications.getValue() != isActive) {
            isActiveNotifications.setValue(isActive);
        }
    }

    public void onSaveButtonClicked() {
        this.updateNotifications();
    }

}
