package com.venclima.service;

import com.venclima.dto.NotificationDTO;
import com.venclima.model.Island;
import com.venclima.model.Station;
import com.venclima.model.User;
import com.venclima.repository.IslandRepository;
import com.venclima.repository.StationRepository;
import com.venclima.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final IslandRepository islandRepository;

    public NotificationService(UserRepository userRepository, IslandRepository islandRepository) {
        this.userRepository = userRepository;
        this.islandRepository = islandRepository;
    }

    @Transactional
    public void addNotification(String userEmail, NotificationDTO islands){
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Integer> islandIds = islands.getIslandsIds();
        List<Island> filteredIslands = this.islandRepository.findAllById(islandIds);
        user.getIslands().addAll(filteredIslands);
    }

}
