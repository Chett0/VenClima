package com.venclima.service;

import com.venclima.dto.StationDTO;
import com.venclima.mapper.StationMapper;
import com.venclima.model.Station;
import com.venclima.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    public StationService(StationRepository stationRepository, StationMapper stationMapper) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
    }

    /**
     * Adds a new {@link Station} to the repository.
     *
     * @param station the station entity to persist
     * @return the persisted {@link Station} entity
     */
    public Station addStation(Station station) {
        return stationRepository.save(station);
    }

    /**
     * Retrieves all stations as {@link StationDTO}.
     *
     * @return list of all stations mapped to {@link StationDTO}
     */
    public List<StationDTO> getAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(stationMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a {@link Station} by its ID.
     *
     * @param id the ID of the station
     * @return an {@link Optional} containing the station if found, otherwise empty
     */
    public Optional<Station> getStationById(Integer id) {
        return stationRepository.findById(id);
    }

}
