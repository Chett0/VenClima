package com.venclima.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IslandNotificationDTO {

    private String islandName;
    private Integer islandId;
    private Boolean isNotified;

}
