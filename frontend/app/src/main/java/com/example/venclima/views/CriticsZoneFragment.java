package com.example.venclima.views;
import static org.maplibre.android.style.layers.PropertyFactory.fillColor;
import static org.maplibre.android.style.layers.PropertyFactory.fillOpacity;

import com.example.venclima.R;
import com.example.venclima.databinding.CriticsZoneBinding;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.lifecycle.ViewModelProvider;

import org.locationtech.jts.geom.Coordinate;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.MapLibre;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.geometry.LatLngBounds;
import org.maplibre.android.maps.MapView;

import org.maplibre.android.maps.Style;
import org.maplibre.android.style.layers.FillLayer;
import org.maplibre.android.style.layers.PropertyFactory;
import org.maplibre.android.style.layers.SymbolLayer;
import org.maplibre.android.style.sources.GeoJsonSource;
import org.maplibre.android.utils.BitmapUtils;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.FeatureCollection;
import org.maplibre.geojson.Point;

import com.example.venclima.models.Island;
import com.example.venclima.models.Station;
import com.example.venclima.models.Tide;
import com.example.venclima.viewModels.IslandViewModel;
import com.example.venclima.viewModels.StationViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class CriticsZoneFragment extends Fragment {

    private ImageButton btnPasserelle;
    private CriticsZoneBinding binding;
    private MapView mapView;
    private IslandViewModel viewModel;
    private StationViewModel station;


    // Array di coordinate [lat, lon] dei punti dove viene misurata l'acqua -> to convert with api
    double[][] points;

    List<Coordinate> station_coordinates = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            ViewGroup container,
                            Bundle savedInstanceState) {
        MapLibre.getInstance(requireContext());

        binding = CriticsZoneBinding.inflate(inflater, container, false);
        this.btnPasserelle = binding.circularPasserelleButton;

        btnPasserelle.setOnClickListener(v -> NavHostFragment.findNavController(CriticsZoneFragment.this).navigate(R.id.PasserelleFragment));

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);

        //setup della mappa
        mapView.getMapAsync(map -> {

            viewModel = new ViewModelProvider(this).get(IslandViewModel.class);

            station = new ViewModelProvider(this).get(StationViewModel.class);

            map.setStyle("https://tiles.openfreemap.org/styles/liberty", style->{
                    //posizione in cui la mappa si apre
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(45.4340,12.3380))
                            .zoom(14.0)
                            .build();
                    map.setCameraPosition(position);

                    //limita a mappa solo ad un determinato territorio -- da mettere il commento


                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(new LatLng(45.25, 12.05))  // nord-ovest
                            .include(new LatLng(45.55, 12.65))  // sud-est
                            .build();
                    map.setLatLngBoundsForCameraTarget(bounds);

                    map.setMinZoomPreference(11.5);
                    map.setMaxZoomPreference(16.0);



                station.getStation().observe(this, stations -> {
                    List<Feature> features = new ArrayList<>();

                    for (Station s : stations){
                        double lng = s.getCoordinate().getX();
                        double lat = s.getCoordinate().getY();

                        if (
                                Double.isNaN(lat) || Double.isNaN(lng) ||
                                        Double.isInfinite(lat) || Double.isInfinite(lng) ||
                                        lat < -90 || lat > 90 ||
                                        lng < -180 || lng > 180
                        ) {
                            Log.e("MAP", "Invalid coordinate skipped: lat=" + lat + " lng=" + lng);
                            continue;
                        }

                        features.add(Feature.fromGeometry(Point.fromLngLat(lng,lat)));

                    }
                    FeatureCollection fc = FeatureCollection.fromFeatures(features);

                    GeoJsonSource source = new GeoJsonSource("marker-source", fc);
                    style.addSource(source);

                    //now add the image of the marker
                    if(style.getImage("marker-icon") == null) {
                        style.addImage(
                                "marker-icon",
                                BitmapUtils.getBitmapFromDrawable(
                                        getResources().getDrawable(R.drawable.map_marker)
                                )
                        );
                    }

                    SymbolLayer layer = new SymbolLayer("marker-layer", "marker-source");
                    layer.setProperties(
                            PropertyFactory.iconImage("marker-icon"),
                            PropertyFactory.iconSize(1.0f),
                            PropertyFactory.iconAllowOverlap(true)
                    );

                    style.addLayer(layer);
                });
            });

            map.getStyle(style -> {

                viewModel.getIslandTides().observe(getViewLifecycleOwner(), response -> {
                    List<Island> islands = response.getIslands();
                    List<Tide> tides = response.getTides(); // nuova parte

                    if (islands != null) {
                        for (Island island : islands) {
                            // Passa anche le tide al metodo addIslandGeoJson
                            addIslandGeoJson(style, island, tides);
                        }
                    }
                });
            });
        });

        return binding.getRoot();
    }

    // ---- Forward lifecycle events ----
    @Override public void onStart() { super.onStart(); mapView.onStart(); }
    @Override public void onResume() { super.onResume(); mapView.onResume(); }
    @Override public void onPause() { mapView.onPause(); super.onPause(); }
    @Override public void onStop() { mapView.onStop(); super.onStop(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        mapView.onDestroy();
        super.onDestroyView();
        binding = null;
    }

//method that colors the islands of Venice, checks if there is a high tide level, and then colors the map
    private void addIslandGeoJson(@NonNull Style style, Island island, List<Tide> tides) {

        String sourceId = "island-source-" + island.getId();
        String layerId  = "island-layer-" + island.getId();

        // Crea GeoJson source
        GeoJsonSource source = new GeoJsonSource(sourceId, island.getGeoJson());
        style.addSource(source);

        FillLayer layer = new FillLayer(layerId, sourceId);

        layer.setProperties(
                fillColor(getColorForIsland(island, tides)),
                fillOpacity(0.6f)
        );

        style.addLayer(layer);
    }

    private int getColorForIsland(Island island, List<Tide> tides) {
        if (tides == null || tides.isEmpty()) {
            return Color.parseColor("#a83271"); // fucsia per vedere se ottengo o meno tides
        }

        double tideLevel = -9999;
        double minLevel = island.getMinLevel();
        double maxLevel = island.getMaxLevel();
        double tideRange = maxLevel - minLevel;



        Iterator<Tide> it = tides.iterator();
        //care with this loop
        while(it.hasNext() && tideLevel == -9999){
            Tide temp = it.next();
            if(Objects.equals(island.getStationId(), temp.getStationId())){
                tideLevel = temp.getLevel();
            }
        }

        tideLevel *= 100; //convert from m to cm

        // Filtra le tide relative a questa isola
        //Log.d("IslandDebug", String.valueOf(tideLevel) + " stazione" + island.getStationId() + " min" + island.getMinLevel() + " max" + island.getMaxLevel());

        //black color for missing data
        if(minLevel == -1 || maxLevel == -1 || tideLevel == -9999){
            return Color.parseColor("#AA000000");
        }


        if(tideLevel <= minLevel){
            return Color.parseColor("#AA00FF00");
        } else if(tideLevel > minLevel && tideLevel <= minLevel + tideRange / 3){
            return Color.parseColor("#AAFFFF00");
        } else if(tideLevel > minLevel + tideRange / 3 && tideLevel <= minLevel + 2 * (tideRange / 3)){
            return Color.parseColor("#AAFFA500");
        } else {
            return Color.parseColor("#AAFF0000");
        }
    }

}
