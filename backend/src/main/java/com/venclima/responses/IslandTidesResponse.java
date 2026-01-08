package com.venclima.responses;

import com.venclima.dto.IslandDTO;
import com.venclima.dto.TideDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IslandTidesResponse {

    List<IslandDTO> islands;
    List<TideDTO> tides;

}
