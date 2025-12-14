package com.venclima.mapper;

import com.venclima.dto.IslandDTO;
import com.venclima.model.Island;
import org.springframework.stereotype.Component;
import org.locationtech.jts.io.WKTWriter;

@Component
public class IslandMapper {

    public IslandDTO toDTO(Island island) {
        IslandDTO islandDTO = new IslandDTO();
        islandDTO.setId(island.getId());
        islandDTO.setName(island.getName());
        islandDTO.setArea(new WKTWriter().write(island.getArea()));
        islandDTO.setMaxLevel(island.getMaxLevel());
        islandDTO.setMinLevel(island.getMinLevel());
        islandDTO.setDistrict(island.getDistrict());
        islandDTO.setStationId(island.getStation().getId());
        return islandDTO;
    }

}
