package com.venclima.service;

import com.venclima.dto.IslandDTO;
import com.venclima.dto.TideDTO;
import com.venclima.mapper.IslandMapper;
import com.venclima.mapper.TideMapper;
import com.venclima.model.Island;
import com.venclima.model.IslandInitializer;
import com.venclima.model.Station;
import com.venclima.repository.IslandRepository;
import com.venclima.repository.StationRepository;
import com.venclima.repository.TideRepository;
import com.venclima.responses.IslandTidesResponse;
import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.*;
import org.springframework.boot.jdbc.HikariCheckpointRestoreLifecycle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.*;

@Service
public class IslandService {

    private final StationRepository stationRepository;
    private final IslandRepository islandRepository;
    private final static GeometryFactory gf = new GeometryFactory();
    private final IslandMapper islandMapper;
    private final TideRepository tideRepository;
    private final TideMapper tideMapper;

    public IslandService(StationRepository stationRepository, IslandRepository islandRepository, IslandMapper islandMapper, TideRepository tideRepository, TideMapper tideMapper) {
        this.stationRepository = stationRepository;
        this.islandRepository = islandRepository;
        this.islandMapper = islandMapper;
        this.tideRepository = tideRepository;
        this.tideMapper = tideMapper;
    }

    /**
     * Calculates the great-circle distance between two geographic points
     * using the Haversine formula.
     *
     * @param lat1 latitude of the first point
     * @param lon1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param lon2 longitude of the second point
     * @return distance between the points in kilometers
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * Finds the nearest {@link Station} to a given coordinate.
     *
     * @param stations list of available stations
     * @param coordinates reference coordinates
     * @return the nearest station, or {@code null} if no stations are available
     */
    private static Station nearestStation(List<Station> stations, Coordinate coordinates) {
        if(stations.isEmpty()) return null;
        Station nearestStation = null;
        double minDistance = Double.MAX_VALUE;
        double currDistance;
        for(Station station : stations) {
            currDistance = distance(station.getCoordinate().y, station.getCoordinate().x, coordinates.y, coordinates.x);
            if(currDistance < minDistance) {
                minDistance = currDistance;
                nearestStation = station;
            }
        }
        return nearestStation;
    }

    /**
     * Creates a polygon from an array of JTS coordinates.
     *
     * @param coordinates array defining the polygon shell
     * @return a {@link Polygon} representing the island area
     */
    private static Polygon createPolygon(Coordinate[] coordinates) {
        LinearRing shell = gf.createLinearRing(coordinates);
        return gf.createPolygon(shell);
    }

    /**
     * Creates or updates an {@link Island} entity based on initialization data.
     * <p>
     * If an island with the same name already exists, it is updated;
     * otherwise, a new island is persisted.
     *
     * @param stations list of available tide stations
     * @param islandInitializer initialization data for the island
     */
    private void createIsland(List<Station> stations, IslandInitializer islandInitializer) {

        Island island = new Island();
        island.setName(islandInitializer.getName());
        island.setMinLevel(islandInitializer.getMinLevel());
        island.setMaxLevel(islandInitializer.getMaxLevel());
        island.setDistrict(islandInitializer.getDistrict());
        island.setArea(createPolygon(islandInitializer.getCoordinates()));
        island.setStation(nearestStation(stations, islandInitializer.getCoordinates()[0]));
        island.setLastNotified(null);

        Optional<Island> existingIsland = islandRepository.findByName(island.getName());
        if(existingIsland.isPresent()) {
            Island update = existingIsland.get();
            update.setMinLevel(island.getMinLevel());
            update.setMaxLevel(island.getMaxLevel());
            update.setDistrict(island.getDistrict());
            update.setArea(island.getArea());
            update.setStation(island.getStation());
            islandRepository.save(update);
        }
        else
            islandRepository.save(island);
    }

    /**
     * Initializes island data at application startup.
     * <p>
     * Loads island definitions from the {@code VeniceIslands.json} resource,
     * creates geospatial polygons, and associates each island with
     * the nearest tide station.
     */
    @PostConstruct
    public void init() {

        try {

            ClassPathResource resource = new ClassPathResource("VeniceIslands.json");
            String jsonText;
            try (InputStream is = resource.getInputStream()) {
                jsonText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

            JSONArray rootArray = new JSONArray(jsonText);
            List<Station> stations = stationRepository.findAll();
            List<IslandInitializer> islands = new ArrayList<>();

            String name;
            int minLevel;
            int maxLevel;
            String district;

            for (int i = 0; i < rootArray.length(); i++) {

                JSONObject curr = rootArray.getJSONObject(i);

                name = curr.getString("name");
                minLevel = curr.getInt("minLevel");
                maxLevel = curr.getInt("maxLevel");
                district = curr.getString("district");

                JSONObject geometry = curr.getJSONObject("geometry");
                JSONArray coordsArray = geometry.getJSONArray("coordinates")
                        .getJSONArray(0);

                Coordinate[] coordinates = new Coordinate[coordsArray.length()];
                for (int j = 0; j < coordsArray.length(); j++) {
                    JSONArray currCoords = coordsArray.getJSONArray(j);

                    double lon = currCoords.getDouble(0);
                    double lat = currCoords.getDouble(1);

                    coordinates[j] = new Coordinate(lon, lat);

                }

                islands.add(
                        new IslandInitializer(
                                name,
                                minLevel,
                                maxLevel,
                                district,
                                coordinates
                        )
                );

            }

            for (IslandInitializer island : islands) {
                createIsland(stations, island);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Retrieves all islands as DTOs.
     *
     * @return list of {@link IslandDTO}
     */
    public List<IslandDTO> getIslands() {
        return islandRepository.findAll()
                .stream()
                .map(islandMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves island data along with real-time tide information.
     *
     * @return {@link IslandTidesResponse} containing islands and tide data
     */
    public IslandTidesResponse getIslandsTides() {
        List<IslandDTO> islands = islandRepository.findAll()
                .stream()
                .map(islandMapper::toDTO)
                .toList();

        List<TideDTO> realTimeTides = tideRepository.findRealTimeTides()
                .stream()
                .map(tideMapper::toDTO)
                .toList();

        return new IslandTidesResponse(
                islands,
                realTimeTides
        );
    }

    /**
     * Retrieves an island containing the given geographic coordinate.
     *
     * @param latitude latitude of the point
     * @param longitude longitude of the point
     * @return an {@link Optional} containing the matching {@link IslandDTO}, if found
     */
    public Optional<IslandDTO> getIslandByCoordinate(double latitude, double longitude) {
        Point point = gf.createPoint(new Coordinate(longitude, latitude));
        return islandRepository.findAll()
                .stream()
                .filter(i -> i.getArea().contains(point))
                .findFirst()
                .map(islandMapper::toDTO);
    }

}
