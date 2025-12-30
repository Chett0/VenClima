package com.venclima.service;

import com.venclima.dto.TideDTO;
import com.venclima.mapper.TideMapper;
import com.venclima.model.Station;
import com.venclima.model.Tide;
import com.venclima.repository.StationRepository;
import com.venclima.repository.TideRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TideService {

    private static final Logger logger = LoggerFactory.getLogger(TideService.class);

    private final TideRepository tideRepository;
    private final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StationRepository stationRepository;
    private final TideMapper tideMapper;

    public TideService(TideRepository tideInfoRepository, StationRepository stationRepository, TideMapper tideMapper) {
        this.tideRepository = tideInfoRepository;
        this.stationRepository = stationRepository;
        this.tideMapper = tideMapper;
    }

    public Tide addTideInfo(Tide tide) {
        return tideRepository.save(tide);
    }

    public List<TideDTO> getAllTides() {
        return tideRepository.findAll()
                .stream()
                .map(tideMapper::toDTO)
                .sorted(Comparator.comparing(TideDTO::getStationId))
                .collect(Collectors.toList());
    }

    public List<TideDTO> getTidesByStationId(int stationId) {
        return tideRepository.findAll()
                .stream()
                .map(tideMapper::toDTO)
                .sorted(Comparator.comparing(TideDTO::getStationId))
                .collect(Collectors.toList());
    }

    public List<TideDTO> getDailyTides() {
        return tideRepository.findDailyTides()
                .stream()
                .map(tideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<Tide> getTideByStationIdAndDate(Integer stationId, LocalDateTime date) {
        return this.tideRepository.findByStation_IdAndDate(stationId, date);
    }

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

}
