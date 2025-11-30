package com.venclima.controller;

import com.venclima.service.IslandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/islands")
public class IslandController {

    private IslandService islandService;

    public IslandController(IslandService islandService) {
        this.islandService = islandService;
    }

}
