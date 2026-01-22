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

    /**
     * Updates notification preferences for the given user.
     * <p>
     * This method:
     * <ul>
     *     <li>Clears the user's current island notification subscriptions</li>
     *     <li>Adds the new list of islands specified in {@link NotificationDTO}</li>
     *     <li>Updates the user's active notification status</li>
     * </ul>
     *
     * @param user the authenticated user whose notifications are being updated
     * @param notifications DTO containing the new notification settings
     */
    @Transactional
    public void updateNotification(User user, NotificationDTO notifications){
        List<Integer> islandIds = notifications.getIslandsIds();
        List<Island> filteredIslands = this.islandRepository.findAllById(islandIds);
        user.getIslands().clear();
        user.getIslands().addAll(filteredIslands);
        user.setActiveNotifications(notifications.getIsActiveNotifications());
    }

    /**
     * Retrieves island notifications for the given user.
     *
     * @param user the user whose island notifications are requested
     * @return list of {@link IslandNotificationDTO} representing the user's notifications
     */
    @Transactional
    public List<IslandNotificationDTO> getNotification(User user){
        return this.islandRepository.getNotification(user.getId());
    }

    /**
     * Returns whether notifications are active for the given user.
     *
     * @param user the user whose notification status is checked
     * @return {@code true} if notifications are active, {@code false} otherwise
     */
    public Boolean getIsActiveNotification(User user){
        return user.isActiveNotifications();
    }

}
