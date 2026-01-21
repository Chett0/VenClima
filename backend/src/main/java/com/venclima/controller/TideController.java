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

    /**
     * Retrieves all tide records.
     *
     * @return {@link ResponseEntity} containing a list of {@link TideDTO},
     *         or {@code 404 Not Found} if retrieval fails
     */
    @GetMapping
    public ResponseEntity<List<TideDTO>> getTides() {
        try {
            List<TideDTO> tides = tideService.getAllTides();
            return ResponseEntity.ok(tides);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves tide records for the current day.
     *
     * @return {@link ResponseEntity} containing a list of {@link TideDTO} for the day,
     *         or {@code 404 Not Found} if retrieval fails
     */
    @GetMapping("/day")
    public ResponseEntity<List<TideDTO>> getDailyTides() {
        try {
            List<TideDTO> tides = tideService.getDailyTides();
            return ResponseEntity.ok(tides);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates or updates daily tide records.
     * <p>
     * This endpoint triggers the service to calculate and persist daily tide data.
     *
     * @return {@link ResponseEntity} with HTTP {@code 200 OK} if successful,
     *         or {@code 400 Bad Request} if the operation fails
     * @throws IOException if an I/O error occurs during tide data processing
     */
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
