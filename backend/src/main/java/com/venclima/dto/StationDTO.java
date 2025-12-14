package com.venclima.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationDTO {

    private Integer id;
    private String name;
    private String nameAbbreviation;
    private Coordinate coordinate;

}
