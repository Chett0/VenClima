package com.venclima.service;

import com.venclima.dto.TideDTO;
import com.venclima.dto.TideMapper;
import com.venclima.model.Tide;
import com.venclima.repository.TideRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TideService {

    private final TideRepository tideRepository;

    public TideService(TideRepository tideInfoRepository) {
        this.tideRepository = tideInfoRepository;
    }

    public void addTideInfo(Tide tide) {
        tideRepository.save(tide);
    }

    public List<TideDTO> getAllTides() {
        return tideRepository.findAll()
                .stream()
                .map(TideMapper::toDTO)
                .sorted(Comparator.comparing(TideDTO::getStationId))
                .collect(Collectors.toList());
    }

}
