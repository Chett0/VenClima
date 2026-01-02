package com.venclima.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IslandDTO {

    private Integer id;
    private String name;
    private Integer minLevel;
    private Integer maxLevel;
    private String district;
    // private String area;  -> not needed for drawing island polygons, mapLibre needs geoJSON data format
    private Integer stationId;
    private String geoJson;

}
