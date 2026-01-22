package com.venclima.controller;

import com.venclima.dto.IslandDTO;
import com.venclima.responses.IslandTidesResponse;
import com.venclima.service.IslandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/islands")
public class IslandController {

    private final IslandService islandService;

    public IslandController(IslandService islandService) {
        this.islandService = islandService;
    }

    /**
     * Retrieves a list of all available islands.
     *
     * @return {@link ResponseEntity} containing a list of {@link IslandDTO},
     *         or {@code 404 Not Found} if islands cannot be retrieved
     */
    @GetMapping
    public ResponseEntity<List<IslandDTO>> getIslands() {
        try{
            List<IslandDTO> islands = islandService.getIslands();
            return ResponseEntity.ok(islands);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves an island by its geographic coordinates.
     *
     * @param latitude the latitude of the island
     * @param longitude the longitude of the island
     * @return {@link ResponseEntity} containing an {@link Optional} of {@link IslandDTO},
     *         or {@code 400 Bad Request} if the request parameters are invalid
     */
    @GetMapping("/{latitude}/{longitude}")
    public ResponseEntity<Optional<IslandDTO>> getIsland(@PathVariable double latitude, @PathVariable double longitude) {
        try{
            Optional<IslandDTO> island = islandService.getIslandByCoordinate(latitude, longitude);
            return ResponseEntity.ok(island);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves tide information for all islands.
     *
     * @return {@link ResponseEntity} containing an {@link IslandTidesResponse}
     *         with tide data, or {@code 404 Not Found} if the data cannot be retrieved
     */
    @GetMapping("/tides")
    public ResponseEntity<IslandTidesResponse> getIslandTides() {
        try{
            IslandTidesResponse response = islandService.getIslandsTides();
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}