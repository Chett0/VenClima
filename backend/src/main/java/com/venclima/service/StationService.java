package com.venclima.service;

import com.venclima.model.Station;
import com.venclima.model.TideInfo;
import com.venclima.repository.StationRepository;
import com.venclima.repository.TideInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void addStation(Station station) {
        stationRepository.save(station);
    }

}
