package com.venclima.service;

import com.venclima.dto.TideDTO;
import com.venclima.mapper.TideMapper;
import com.venclima.model.*;
import com.venclima.repository.IslandRepository;
import com.venclima.repository.StationRepository;
import com.venclima.repository.TideRepository;
import com.venclima.repository.TokenRepository;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.locationtech.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TideService {

    private static final Logger logger = LoggerFactory.getLogger(TideService.class);

    private final TideRepository tideRepository;
    private final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StationRepository stationRepository;
    private final TideMapper tideMapper;

    private final StationService stationService;
    private final IslandRepository islandRepository;
    private final FireBaseMessagingService fireBaseMessagingService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String urlRealTimeData = "https://dati.venezia.it/sites/default/files/dataset/opendata/livello.json";
    private final Map<Integer, String> stationLinkName;

    private List<Island> islands;
    private final TokenRepository tokenRepository;
    private final IslandService islandService;
    private final NotificationService notificationService;

    public TideService(
            TideRepository tideInfoRepository,
            StationRepository stationRepository,
            TideMapper tideMapper,
            StationService stationService,
            IslandRepository islandRepository,
            TokenRepository tokenRepository,
            FireBaseMessagingService fireBaseMessagingService,
            IslandService islandService, NotificationService notificationService) {

        this.tideRepository = tideInfoRepository;
        this.stationRepository = stationRepository;
        this.tideMapper = tideMapper;

        this.stationService = stationService;
        this.islandRepository = islandRepository;
        this.tokenRepository = tokenRepository;
        this.fireBaseMessagingService = fireBaseMessagingService;

        this.stationLinkName = new HashMap<>();
        stationLinkName.put(1001, "");
        stationLinkName.put(1021, "Piattaforma");
        stationLinkName.put(1022, "Diga_Sud_Lido");
        stationLinkName.put(1023, "Diga_Nord_Malamocco");
        stationLinkName.put(1024, "Diga_Sud_Chioggia");
        stationLinkName.put(1025, "Punta_Salute");
        stationLinkName.put(1033, "Chioggia_Porto");
        stationLinkName.put(1036, "Chioggia_Citta");
        stationLinkName.put(1028, "Laguna_Nord");
        stationLinkName.put(1029, "Misericordia");
        stationLinkName.put(1030, "Burano");
        stationLinkName.put(1031, "Malamocco_Porto");
        stationLinkName.put(1037, "Fusina");
        this.islandService = islandService;
        this.notificationService = notificationService;
    }

    /**
     * Persists a new {@link Tide} entity.
     *
     * @param tide the tide entity to save
     * @return the persisted tide entity
     */
    public Tide addTideInfo(Tide tide) {
        return tideRepository.save(tide);
    }

    /**
     * Retrieves all tides sorted by station ID.
     *
     * @return list of {@link TideDTO} for all tide records
     */
    public List<TideDTO> getAllTides() {
        return tideRepository.findAll()
                .stream()
                .map(tideMapper::toDTO)
                .sorted(Comparator.comparing(TideDTO::getStationId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves tide records for a specific station.
     *
     * @param stationId the ID of the station
     * @return list of {@link TideDTO} for the specified station
     */
    public List<TideDTO> getTidesByStationId(int stationId) {
        return tideRepository.findAll()
                .stream()
                .map(tideMapper::toDTO)
                .sorted(Comparator.comparing(TideDTO::getStationId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves tide records for the current day.
     *
     * @return list of {@link TideDTO} representing daily tides
     */
    public List<TideDTO> getDailyTides() {
        return tideRepository.findDailyTides()
                .stream()
                .map(tideMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a tide record for a specific station and date.
     *
     * @param stationId the ID of the station
     * @param date the date and time of the tide
     * @return {@link Optional} containing the tide if found
     */
    public Optional<Tide> getTideByStationIdAndDate(Integer stationId, LocalDateTime date) {
        return this.tideRepository.findByStation_IdAndDate(stationId, date);
    }

    /**
     * Parses and stores daily tide data from official station websites.
     * <p>
     * Creates new {@link Tide} entities if no existing record is found.
     *
     * @throws IOException if unable to fetch or parse tide data
     */
    public void setDailyTides() throws IOException {
        int inserted = 0;
        List<Station> stations = stationRepository.findAll();

        for (Station station : stations) {
            if (!station.getName_link().isEmpty()) {
                Document doc = Jsoup.connect(String.format("https://www.comune.venezia.it/sites/default/files/publicCPSM2/stazioni/temporeale/%s.html", station.getName_link())).get();
                Element table = doc.select("table").first();
                if (table == null) {
                    logger.warn("setDailyTides: no table found for station {}", station.getId());
                    continue;
                }

                Elements rows = table.select("tbody tr");

                for (Element row : rows) {
                    Elements cols = row.select("td");

                    if (cols.size() < 2) {
                        logger.warn("setDailyTides: unexpected row format for station {}: {}", station.getId(), row.text());
                        continue;
                    }

                    String datetime = cols.get(0).text();
                    String level = cols.get(1).text();

                    LocalDateTime dateTime = LocalDateTime.parse(datetime, dataFormatter);
                    Optional<Tide> existingTide = tideRepository.findByStation_IdAndDate(station.getId(), dateTime);

                    if(existingTide.isEmpty()) {

                        Tide tide = new Tide();
                        tide.setDate(dateTime);

                        double levelValue = -1;
                        String cleaned = level != null ? level.replaceAll("[^0-9.-]", "").trim() : "";
                        if (!cleaned.isEmpty()) {
                            try {
                                levelValue = Double.parseDouble(cleaned);
                            } catch (NumberFormatException ex) {
                                logger.warn("setDailyTides: invalid level '{}' for station {} at {}", level, station.getId(), datetime);
                            }
                        } else {
                            logger.warn("setDailyTides: empty level for station {} at {}", station.getId(), datetime);
                        }

                        tide.setLevel(levelValue);
                        tide.setStation(station);

                        tideRepository.save(tide);
                        inserted++;
                    }

                }
            }
        }

        logger.info("setDailyTides: completed - inserted {} new records", inserted);
    }


    /**
     * Scheduled task that runs every 5 minutes to fetch real-time tide data.
     * <p>
     * Updates existing tide records, creates new ones, and triggers
     * notifications for islands exceeding critical tide levels.
     */
    @Scheduled(fixedRate = 300000)
    public void retrieveRealTimeTideLevel() {
        try{
            DataStation[] data = restTemplate.getForObject(urlRealTimeData, DataStation[].class);
            Set<String> tokensToNotify = new HashSet<>();
            if(data != null) {
                for (DataStation dataStation : data) {

                    if(dataStation.getStazione().equals("Venezia Misericordia"))
                        continue;

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
                    Optional<Tide> existingTide = this.getTideByStationIdAndDate(stationId, dateTime);
                    Tide savedTide;

                    if (existingTide.isEmpty()) {

                        Tide tide = new Tide();
                        tide.setDate(dateTime);
                        tide.setLevel(dataStation.getValore() != null ? Double.parseDouble(dataStation.getValore().replaceAll("[^0-9.]", "")) : -1);
                        //tide.setLevel(10000);
                        tide.setStation(savedStation);

                        savedTide = this.addTideInfo(tide);
                    } else
                        savedTide = existingTide.get();

                    if(islands == null)
                        this.islands = islandRepository.findAll();

                    List<Island> criticIslands = islandService.getCriticIslands(islands, savedTide, savedStation);

                    List<User> usersToNotify = notificationService.getUserToNotify(criticIslands);

                    List<Token> currTokens;
                    for (User user : usersToNotify) {
                        currTokens = tokenRepository.findAllByUser(user);
                        for(Token currToken : currTokens){
                            tokensToNotify.add(currToken.getToken());
                        }
                    }

                }

                for (String token : tokensToNotify) {
                    String res = this.fireBaseMessagingService.sendPushNotificationService(new NotificationRequest(
                            "Allerta",
                            "Marea alta",
                            token
                    ));
                    if(res.equals("NOT FOUND"))
                        tokenRepository.deleteByToken(token);
                    System.out.println(res);
                }

            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * Initializes daily tide data on application startup.
     * <p>
     * Invokes {@link #setDailyTides()} to populate the database with the current day's tides.
     */
    @PostConstruct
    public void init() {
        try{
            this.setDailyTides();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

}
