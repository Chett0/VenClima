package com.venclima.service;

import com.venclima.model.TideInfo;
import com.venclima.repository.TideInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TideInfoService {

    private final TideInfoRepository tideInfoRepository;

    public TideInfoService(TideInfoRepository tideInfoRepository) {
        this.tideInfoRepository = tideInfoRepository;
    }

    public void addTideInfo(TideInfo tideInfo) {
        tideInfoRepository.save(tideInfo);
    }

}
