package com.venclima.controller;

import com.venclima.dto.TideDTO;
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
    public ResponseEntity<List<TideDTO>> getTides() {
        try {
            List<TideDTO> tides = tideService.getAllTides();
            return ResponseEntity.ok(tides);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/day")
    public ResponseEntity<List<TideDTO>> getDailyTides() {
        try {
            List<TideDTO> tides = tideService.getDailyTides();
            return ResponseEntity.ok(tides);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> createDailyTide() throws IOException {
        try {
            tideService.setDailyTides();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
