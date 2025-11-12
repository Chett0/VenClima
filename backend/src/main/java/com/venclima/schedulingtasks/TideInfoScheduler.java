package com.venclima.schedulingtasks;

import com.venclima.model.DataStation;
import com.venclima.model.Station;
import com.venclima.model.TideInfo;
import com.venclima.service.StationService;
import com.venclima.service.TideInfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TideInfoScheduler {

    private final TideInfoService tideInfoService;
    private final StationService stationService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String urlRealTimeData = "https://dati.venezia.it/sites/default/files/dataset/opendata/livello.json";
    private final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TideInfoScheduler(TideInfoService tideInfoService, StationService stationService) {
        this.tideInfoService = tideInfoService;
        this.stationService = stationService;
    }

    @Scheduled(fixedRate = 30000)
    public void saveTideInfo() {
        DataStation[] data = restTemplate.getForObject(urlRealTimeData, DataStation[].class);
        if(data != null) {
            for (DataStation dataStation : data) {

                long stationId = Long.parseLong(dataStation.getIdStazione());

                Station station = new Station(
                        stationId,
                        dataStation.getStazione(),
                        Double.parseDouble(dataStation.getLatDDN()),
                        Double.parseDouble(dataStation.getLonDDE()),
                        dataStation.getNomeAbbr()
                );

                stationService.addStation(station);

                LocalDateTime dateTime = LocalDateTime.parse(dataStation.getData(), dataFormatter);

                TideInfo tideInfo = new TideInfo();
                tideInfo.setDate(dateTime);
                tideInfo.setLevel(Double.parseDouble(dataStation.getValore().replaceAll("[^0-9.]", "")));
                tideInfo.setStation(station);

                tideInfoService.addTideInfo(tideInfo);
            }
        }
    }

}
