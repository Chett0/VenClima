package com.venclima.service;

import com.venclima.dto.NotificationDTO;
import com.venclima.model.Station;
import com.venclima.model.User;
import com.venclima.repository.StationRepository;
import com.venclima.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final StationRepository stationRepository;

    public NotificationService(UserRepository userRepository, StationRepository stationRepository) {
        this.userRepository = userRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void addNotification(String userEmail, NotificationDTO stationsDTO){
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Integer> stationIds = stationsDTO.getStationIds();
        List<Station> stations = stationRepository.findAllById(stationIds);
        user.getStations().addAll(stations);
    }

}
