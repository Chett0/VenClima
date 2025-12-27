package com.venclima.schedulingtasks;

import com.venclima.model.*;
import com.venclima.repository.IslandRepository;
import com.venclima.repository.TokenRepository;
import com.venclima.service.FireBaseMessaggingService;
import com.venclima.service.StationService;
import com.venclima.service.TideService;
import org.locationtech.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class TideScheduler {

    private final TideService tideService;
    private final StationService stationService;
    private final IslandRepository islandRepository;
    private final FireBaseMessaggingService fireBaseMessaggingService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String urlRealTimeData = "https://dati.venezia.it/sites/default/files/dataset/opendata/livello.json";
    private final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Map<Integer, String> stationLinkName;

    private final List<Island> islands;
    private final TokenRepository tokenRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(TideScheduler.class);

    public TideScheduler(
            TideService tideInfoService,
            StationService stationService,
            IslandRepository islandRepository,
            TokenRepository tokenRepository,
            FireBaseMessaggingService fireBaseMessagingService
    ) {
        this.tideService = tideInfoService;
        this.stationService = stationService;
        this.islandRepository = islandRepository;
        this.tokenRepository = tokenRepository;
        this.fireBaseMessaggingService = fireBaseMessagingService;

        this.islands = islandRepository.findAll();

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
        try{
            DataStation[] data = restTemplate.getForObject(urlRealTimeData, DataStation[].class);
            if(data != null) {
                for (DataStation dataStation : data) {

                    Integer stationId = Integer.parseInt(dataStation.getIdStazione());

                    String linkName = stationLinkName.get(stationId);
                    if (linkName == null) {
                        linkName = "";
                    }

                    Optional<Station> existingStation = stationService.getStationById(stationId);
                    Station savedStation;
                    if (existingStation.isEmpty()) {
                        Station station = new Station(
                                stationId,
                                dataStation.getStazione(),
                                dataStation.getNomeAbbr(),
                                linkName,
                                new Coordinate(Double.parseDouble(dataStation.getLonDDE()), Double.parseDouble(dataStation.getLatDDN()))
                        );
                        savedStation = stationService.addStation(station);
                    } else
                        savedStation = existingStation.get();

                    LocalDateTime dateTime = dataStation.getData() != null ? LocalDateTime.parse(dataStation.getData(), dataFormatter) : LocalDateTime.now();
                    Optional<Tide> existingTide = tideService.getTideByStationIdAndDate(stationId, dateTime);
                    Tide savedTide;

                    if (existingTide.isEmpty()) {

                        Tide tide = new Tide();
                        tide.setDate(dateTime);
                        tide.setLevel(dataStation.getValore() != null ? Double.parseDouble(dataStation.getValore().replaceAll("[^0-9.]", "")) : -1);
                        tide.setStation(savedStation);

                        savedTide = tideService.addTideInfo(tide);
                    } else
                        savedTide = existingTide.get();

                    List<Island> criticIslands = this.islands.stream()
                            .filter(i -> i.getStation().getId().equals(savedStation.getId())
                                    && i.getMaxLevel() <= savedTide.getLevel()
                                    && (i.getLastNotified() == null
                                    || Duration.between(LocalDateTime.now(), i.getLastNotified()).toHours() > 3))
                            .toList();

                    List<User> usersToNotify = new ArrayList<>();
                    List<User> currUsers;
                    for (Island island : criticIslands) {
                        island.setLastNotified(LocalDateTime.now());
                        currUsers = island.getUsers();
                        if (currUsers != null)
                            usersToNotify.addAll(currUsers);

                        islandRepository.flush();
                    }

                    List<Token> tokensToNotify = new ArrayList<>();
                    for (User user : usersToNotify) {
                        tokensToNotify.addAll(tokenRepository.findAllByUser(user));
                    }

                    for (Token token : tokensToNotify) {
                        this.fireBaseMessaggingService.sendPushNotificationService(new NotificationRequest(
                                "Allerta",
                                "Marea alta",
                                token.getToken()
                        ));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

}
