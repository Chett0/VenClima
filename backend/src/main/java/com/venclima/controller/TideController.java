package com.venclima.controller;

import com.venclima.dto.StationDTO;
import com.venclima.dto.TideDTO;
import com.venclima.service.StationService;
import com.venclima.service.TideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/tides")
public class TideController {

    TideService tideService;

    public TideController(TideService tideService) {
        this.tideService = tideService;
    }

    @GetMapping
    public ResponseEntity<List<TideDTO>> getStation() {
        List<TideDTO> tides = tideService.getAllTides();
        return ResponseEntity.ok(tides);
    }

    @PostMapping
    public ResponseEntity<Void> createDailyTide() throws IOException {
        try {
            tideService.setDailyTides();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
