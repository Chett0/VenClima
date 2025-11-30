package com.venclima.service;

import com.venclima.model.Island;
import com.venclima.model.IslandInitializer;
import com.venclima.model.Station;
import com.venclima.repository.IslandRepository;
import com.venclima.repository.StationRepository;
import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IslandService {

    private final StationRepository stationRepository;
    private final IslandRepository islandRepository;
    private final static GeometryFactory gf = new GeometryFactory();

    public IslandService(StationRepository stationRepository, IslandRepository islandRepository) {
        this.stationRepository = stationRepository;
        this.islandRepository = islandRepository;
    }

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

    private static Station nearestStation(List<Station> stations, Coordinate coordinates) {
        if(stations.isEmpty()) return null;
        Station nearestStation = null;
        double minDistance = Double.MAX_VALUE;
        double currDistance;
        for(Station station : stations) {
            currDistance = distance(station.getLatitude(), station.getLongitude(), coordinates.x, coordinates.y);
            if(currDistance < minDistance) {
                minDistance = currDistance;
                nearestStation = station;
            }
        }
        return nearestStation;
    }

    private static Polygon createPolygon(Coordinate[] coordinates) {
        LinearRing shell = gf.createLinearRing(coordinates);
        return gf.createPolygon(shell);
    }

    private void createIsland(List<Station> stations, IslandInitializer islandInitializer) {

        Island island = new Island();
        island.setName(islandInitializer.getName());
        island.setMinLevel(islandInitializer.getMinLevel());
        island.setMaxLevel(islandInitializer.getMaxLevel());
        island.setArea(createPolygon(islandInitializer.getCoordinates()));
        island.setStation(nearestStation(stations, islandInitializer.getCoordinates()[0]));

        islandRepository.save(island);
    }

    @PostConstruct
    public void init() {

        List<Station> stations = stationRepository.findAll();
        List<IslandInitializer> islands;
        List<IslandInitializer> currentIslands;
        String currDistrict;

        currDistrict = "Canareggio";

        islands = List.of(
                new IslandInitializer(
                        "Sant'Alvise",
                        103,
                        149,
                        currDistrict,
                        new Coordinate[] {
                                new Coordinate(45.448073, 12.330755),
                                new Coordinate(45.446851, 12.329943),
                                new Coordinate(45.448417, 12.324440),
                                new Coordinate(45.448644, 12.324551),
                                new Coordinate(45.449317, 12.326282),
                                new Coordinate(45.448073, 12.330755)
                        }
                ),
                new IslandInitializer(
                        "Madonna dell'Orto",
                        92,
                        176,
                        currDistrict,
                        new Coordinate[] {
                                new Coordinate(45.448078, 12.330926),
                                new Coordinate(45.446368, 12.335547),
                                new Coordinate(45.445181, 12.334377),
                                new Coordinate(45.446754, 12.330058),
                                new Coordinate(45.448078, 12.330926),
                        }
                ),
                new IslandInitializer(
                        "Sensa",
                        94,
                        242,
                        currDistrict,
                        new Coordinate[] {
                                new Coordinate(45.448269, 12.324215),
                                new Coordinate(45.447848, 12.323980),
                                new Coordinate(45.446079, 12.329489),
                                new Coordinate(45.446700, 12.329848),
                                new Coordinate(45.448269, 12.324215)
                        }
                ),
                new IslandInitializer(
                        "Brazzo",
                        98,
                        146,
                        currDistrict,
                        new Coordinate[] {
                                new Coordinate(45.446701, 12.329927),
                                new Coordinate(45.446043, 12.332029),
                                new Coordinate(45.445553, 12.331459),
                                new Coordinate(45.446059, 12.329571),
                                new Coordinate(45.446701, 12.329927)
                        }
                ),
                new IslandInitializer(
                        "Bori",
                        88,
                        192,
                        currDistrict,
                        new Coordinate[] {
                                new Coordinate(45.445517, 12.331593),
                                new Coordinate(45.446052, 12.332151),
                                new Coordinate(45.445230, 12.333992),
                                new Coordinate(45.444735, 12.333313),
                                new Coordinate(45.445517, 12.331593)
                        }
                ),
                new IslandInitializer(
                        "Santa Maria di Valverde",
                        84,
                        144,
                        currDistrict,
                        new Coordinate[] {
                                new Coordinate(45.445190, 12.334077),
                                new Coordinate(45.444292, 12.335964),
                                new Coordinate(45.443654, 12.335114),
                                new Coordinate(45.444686, 12.333435),
                                new Coordinate(45.445190, 12.334077)
                        }
                ),
                new IslandInitializer(
                        "San Girolamo",
                        88,
                        227,
                        currDistrict,
                        new Coordinate[] {
                                new Coordinate(45.44602102438526, 12.326427257539848),
                                new Coordinate(45.447168464037134, 12.32168514504815),
                                new Coordinate(45.447993572360815, 12.322054833119921),
                                new Coordinate(45.44828421350476, 12.323052504621842),
                                new Coordinate(45.448388543387495, 12.323941014388737),
                                new Coordinate(45.448379634724006, 12.323970318657246),
                                new Coordinate(45.44835839098269, 12.323984970790889),
                                new Coordinate(45.44774728570118, 12.32370684105021),
                                new Coordinate(45.44737413415484, 12.325099594224156),
                                new Coordinate(45.44718736991288, 12.325610754106293),
                                new Coordinate(45.447140084445124, 12.325612707724162),
                                new Coordinate(45.44679897284789, 12.326675198989818),
                                new Coordinate(45.44602102438526, 12.326427257539848)
                        }
                )
        );



        for(IslandInitializer island : islands) {
            createIsland(stations, island);
        }

    }

}
