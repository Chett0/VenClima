package com.venclima.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TideDTO {

    private Integer id;
    private LocalDateTime date;
    private double level;
    private Integer stationId;

}
