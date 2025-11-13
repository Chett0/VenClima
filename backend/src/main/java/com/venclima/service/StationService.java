package com.venclima.service;

import com.venclima.dto.StationDTO;
import com.venclima.dto.StationMapper;
import com.venclima.model.Station;
import com.venclima.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void addStation(Station station) {
        stationRepository.save(station);
    }

    public List<StationDTO> getAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(StationMapper::toDTO)
                .collect(Collectors.toList());
    }

}
