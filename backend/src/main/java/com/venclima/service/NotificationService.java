package com.venclima.service;

import com.venclima.dto.IslandNotificationDTO;
import com.venclima.dto.NotificationDTO;
import com.venclima.model.Island;
import com.venclima.model.User;
import com.venclima.repository.IslandRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final IslandRepository islandRepository;

    public NotificationService(IslandRepository islandRepository) {
        this.islandRepository = islandRepository;
    }

    @Transactional
    public void updateNotification(User user, NotificationDTO notifications){
        List<Integer> islandIds = notifications.getIslandsIds();
        List<Island> filteredIslands = this.islandRepository.findAllById(islandIds);
        user.getIslands().clear();
        user.getIslands().addAll(filteredIslands);
        user.setActiveNotifications(notifications.getIsActiveNotifications());
    }

    @Transactional
    public List<IslandNotificationDTO> getNotification(User user){
        return this.islandRepository.getNotification(user.getId());
    }

    public Boolean getIsActiveNotification(User user){
        return user.isActiveNotifications();
    }

}
