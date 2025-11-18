package com.venclima.mapper;

import com.venclima.dto.TideDTO;
import com.venclima.model.Tide;
import org.springframework.stereotype.Component;

@Component
public class TideMapper {

    public TideDTO toDTO(Tide tide){
        TideDTO dto = new TideDTO();
        dto.setId(tide.getId());
        dto.setDate(tide.getDate());
        dto.setLevel(tide.getLevel());
        dto.setStationId(tide.getStation().getId());
        return dto;
    }

    public Tide toEntity(TideDTO dto){
        Tide tide = new Tide();
        tide.setId(dto.getId());
        tide.setDate(dto.getDate());
        tide.setLevel(dto.getLevel());
        // tide.setStationId()
        return tide;
    }

}
