package com.venclima.schedulingtasks;

import com.venclima.model.DataStation;
import com.venclima.model.Station;
import com.venclima.model.Tide;
import com.venclima.service.StationService;
import com.venclima.service.TideService;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TideScheduler {

    private final TideService tideService;
    private final StationService stationService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String urlRealTimeData = "https://dati.venezia.it/sites/default/files/dataset/opendata/livello.json";
    private final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Map<Integer, String> stationLinkName;

    public TideScheduler(TideService tideInfoService, StationService stationService) {
        this.tideService = tideInfoService;
        this.stationService = stationService;

        this.stationLinkName = new HashMap<>();
        stationLinkName.put(1001, "");
        stationLinkName.put(1021, "Piattaforma");
        stationLinkName.put(1022, "Diga_Sud_Lido");
        stationLinkName.put(1023, "Diga_Nord_Malamocco");
        stationLinkName.put(1024, "Diga_Sud_Chioggia");
        stationLinkName.put(1025, "Punta_Salute");
        stationLinkName.put(1028, "Laguna_Nord");
        stationLinkName.put(1029, "Misericordia");
        stationLinkName.put(1030, "Burano");
        stationLinkName.put(1031, "Malamocco_Porto");
        stationLinkName.put(1032, "Chioggia_Porto");
        stationLinkName.put(1037, "Fusina");
    }

    @Scheduled(fixedRate = 30000)
    public void retrieveRealTimeTideLevel() {
        DataStation[] data = restTemplate.getForObject(urlRealTimeData, DataStation[].class);
        if(data != null) {
            for (DataStation dataStation : data) {

                Integer stationId = Integer.parseInt(dataStation.getIdStazione());

                String linkName = stationLinkName.get(stationId);
                if(linkName == null) {
                    linkName = "";
                }

                Optional<Station> existingStation = stationService.getStationById(stationId);
                Station savedStation;
                if(existingStation.isEmpty()) {
                    Station station = new Station(
                            stationId,
                            dataStation.getStazione(),
                            dataStation.getNomeAbbr(),
                            linkName,
                            new Coordinate(Double.parseDouble(dataStation.getLonDDE()), Double.parseDouble(dataStation.getLatDDN()))
                    );
                    savedStation = stationService.addStation(station);
                }
                else
                    savedStation = existingStation.get();

                LocalDateTime dateTime = LocalDateTime.parse(dataStation.getData(), dataFormatter);
                Optional<Tide> existingTide = tideService.getTideByStationIdAndDate(stationId, dateTime);

                if(existingTide.isEmpty()) {

                    Tide tide = new Tide();
                    tide.setDate(dateTime);
                    tide.setLevel(Double.parseDouble(dataStation.getValore().replaceAll("[^0-9.]", "")));
                    tide.setStation(savedStation);

                    tideService.addTideInfo(tide);
                }
            }
        }
    }

}
