package com.venclima.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationDTO {

    private Integer id;
    private String name;
    private double latitude;
    private double longitude;
    private String name_abbreviation;

}
