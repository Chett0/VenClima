package com.venclima.mapper;

import com.venclima.dto.StationDTO;
import com.venclima.model.Station;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public StationDTO toDTO(Station station) {
        StationDTO dto = new StationDTO();
        dto.setId(station.getId());
        dto.setName(station.getName());
        dto.setCoordinate(new Coordinate(station.getCoordinate().x, station.getCoordinate().y, 0.0));
        dto.setNameAbbreviation(station.getName_abbreviation());
        return dto;
    }

    public Station toEntity(StationDTO dto) {
        Station station = new Station();
        station.setId(dto.getId());
        station.setName(dto.getName());
        station.setCoordinate(new Coordinate(dto.getCoordinate().x, dto.getCoordinate().y, 0.0));
        station.setName_abbreviation(dto.getNameAbbreviation());
        return station;
    }

}
