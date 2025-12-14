package com.venclima.service;

import com.venclima.dto.StationDTO;
import com.venclima.mapper.StationMapper;
import com.venclima.model.Station;
import com.venclima.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    public StationService(StationRepository stationRepository, StationMapper stationMapper) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
    }

    public Station addStation(Station station) {
        return stationRepository.save(station);
    }

    public List<StationDTO> getAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(stationMapper::toDTO)
                .collect(Collectors.toList());
    }

//    public Optional<Station> getStationById(long id) {
//        //return stationRepository.findById(id);
//
//    }

}
