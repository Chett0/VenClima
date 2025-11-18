package com.venclima.mapper;

import com.venclima.dto.StationDTO;
import com.venclima.model.Station;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public StationDTO toDTO(Station station) {
        StationDTO dto = new StationDTO();
        dto.setId(station.getId());
        dto.setName(station.getName());
        dto.setLatitude(station.getLatitude());
        dto.setLongitude(station.getLongitude());
        dto.setName_abbreviation(station.getName_abbreviation());
        return dto;
    }

    public Station toEntity(StationDTO dto) {
        Station station = new Station();
        station.setId(dto.getId());
        station.setName(dto.getName());
        station.setLatitude(dto.getLatitude());
        station.setLongitude(dto.getLongitude());
        station.setName_abbreviation(dto.getName_abbreviation());
        return station;
    }

}
