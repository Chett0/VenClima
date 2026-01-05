package com.example.venclima.viewModels;

import static java.security.AccessController.getContext;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.BoolArrayNavType;

import com.example.venclima.models.IslandNotification;
import com.example.venclima.network.Callbacks.NotificationUpdateCallback;
import com.example.venclima.network.repositories.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<List<IslandNotification>> notifications = new MutableLiveData<>();
    private MutableLiveData<List<IslandNotification>> filteredNotifications = new MutableLiveData<>();
    private NotificationUpdateCallback notificationUpdateCallback;


    public NotificationsViewModel(){
        this.loadNotifications();
    }

    public void loadNotifications() {
        this.setNotifications(NotificationRepository.getNotification());
    }

    public LiveData<List<IslandNotification>> getNotifications() {
        return this.notifications;
    }
    public LiveData<List<IslandNotification>> getFilteredNotifications() {
        return this.filteredNotifications;
    }

    public void setNotifications(MutableLiveData<List<IslandNotification>> notifications) {
        this.notifications = notifications;
    }

    public void setFilteredNotifications(MutableLiveData<List<IslandNotification>> filteredNotifications) {
        this.filteredNotifications = filteredNotifications;
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
        NotificationRepository.updateNotification(islandIds, this.notificationUpdateCallback);
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

}
