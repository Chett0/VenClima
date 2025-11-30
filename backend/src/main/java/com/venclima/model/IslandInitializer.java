package com.venclima.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IslandInitializer {

    public IslandInitializer(String name, Integer minLevel, Integer maxLevel, Coordinate[] coordinates) {
        this.name = name;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.coordinates = coordinates;
    }

    private String name;
    private Integer minLevel;
    private Integer maxLevel;
    private String district;
    private Coordinate[] coordinates;

}
