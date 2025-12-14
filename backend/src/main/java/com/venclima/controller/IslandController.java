package com.venclima.controller;

import com.venclima.dto.IslandDTO;
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

    @GetMapping
    public ResponseEntity<List<IslandDTO>> getIslands() {
        try{
            List<IslandDTO> islands = islandService.getIslands();
            return ResponseEntity.ok(islands);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{latitude}/{longitude}")
    public ResponseEntity<Optional<IslandDTO>> getIsland(@PathVariable double latitude, @PathVariable double longitude) {
        try{
            Optional<IslandDTO> island = islandService.getIslandByCoordinate(latitude, longitude);
            return ResponseEntity.ok(island);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
