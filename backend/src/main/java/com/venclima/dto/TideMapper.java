package com.venclima.dto;

import com.venclima.model.Tide;

public class TideMapper {

    public static TideDTO toDTO(Tide tide){
        TideDTO dto = new TideDTO();
        dto.setId(tide.getId());
        dto.setDate(tide.getDate());
        dto.setLevel(tide.getLevel());
        dto.setStationId(tide.getStation().getId());
        return dto;
    }

    public static Tide toEntity(TideDTO dto){
        Tide tide = new Tide();
        tide.setId(dto.getId());
        tide.setDate(dto.getDate());
        tide.setLevel(dto.getLevel());
        // tide.setStationId()
        return tide;
    }

}
