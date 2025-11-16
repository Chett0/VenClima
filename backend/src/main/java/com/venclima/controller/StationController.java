package com.venclima.controller;

import com.venclima.dto.StationDTO;
import com.venclima.service.StationService;
import com.venclima.service.TideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/stations")
public class StationController {

    StationService stationService;
    TideService tideService;

    public StationController(StationService stationService, TideService tideService) {
        this.stationService = stationService;
        this.tideService = tideService;
    }

    @GetMapping
    public ResponseEntity<List<StationDTO>> getStation() {
        List<StationDTO> stations = stationService.getAllStations();
        return ResponseEntity.ok(stations);
    }

}
