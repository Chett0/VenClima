package com.venclima.dto;

import com.venclima.model.Station;

public class StationMapper {

    public static StationDTO toDTO(Station station) {
        StationDTO dto = new StationDTO();
        dto.setId(station.getId());
        dto.setName(station.getName());
        dto.setLatitude(station.getLatitude());
        dto.setLongitude(station.getLongitude());
        dto.setName_abbreviation(station.getName_abbreviation());
        return dto;
    }

    public static Station toEntity(StationDTO dto) {
        Station station = new Station();
        station.setId(dto.getId());
        station.setName(dto.getName());
        station.setLatitude(dto.getLatitude());
        station.setLongitude(dto.getLongitude());
        station.setName_abbreviation(dto.getName_abbreviation());
        return station;
    }

}
