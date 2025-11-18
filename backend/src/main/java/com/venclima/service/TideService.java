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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TideService {

    private final TideRepository tideRepository;
    private final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StationRepository stationRepository;
    private final TideMapper tideMapper;

    public TideService(TideRepository tideInfoRepository, StationRepository stationRepository, TideMapper tideMapper) {
        this.tideRepository = tideInfoRepository;
        this.stationRepository = stationRepository;
        this.tideMapper = tideMapper;
    }

    public void addTideInfo(Tide tide) {
        tideRepository.save(tide);
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

    public void setDailyTides() throws IOException {

        List<Station> stations = stationRepository.findAll();

        for (Station station : stations) {
            if (!station.name_link.isEmpty()) {
                Document doc = Jsoup.connect(String.format("https://www.comune.venezia.it/sites/default/files/publicCPSM2/stazioni/temporeale/%s.html", station.name_link)).get();
                Element table = doc.select("table").first();
                if (table == null)
                    return;

                Elements rows = table.select("tbody tr");

                for (Element row : rows) {
                    Elements cols = row.select("td");

                    String datetime = cols.get(0).text();
                    String level = cols.get(1).text();

                    Tide tide = new Tide();

                    tide.setDate(LocalDateTime.parse(datetime, dataFormatter));
                    tide.setLevel(Double.parseDouble(level));
                    tide.setStation(station);

                    tideRepository.save(tide);
                }
            }
        }
    }

}
