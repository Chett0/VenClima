package com.example.venclima.views;
import com.example.venclima.R;
import com.example.venclima.databinding.CriticsZoneBinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.MapLibre;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.geometry.LatLngBounds;
import org.maplibre.android.maps.MapView;

import org.maplibre.android.style.layers.PropertyFactory;
import org.maplibre.android.style.layers.SymbolLayer;
import org.maplibre.android.style.sources.GeoJsonSource;
import org.maplibre.android.utils.BitmapUtils;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.FeatureCollection;
import org.maplibre.geojson.Point;



public class CriticsZoneFragment extends Fragment {

    private CriticsZoneBinding binding;
    private MapView mapView;


    // Array di coordinate [lat, lon] dei punti dove viene misurata l'acqua
    double[][] points = {
            {45.323056, 12.514722},
            {45.42, 12.424},
            {45.334444, 12.341389},
            {45.22855, 12.312767},
            {45.4311, 12.3364},
            {45.495556, 12.471944},
            {45.4875, 12.415486},
            {45.339722, 12.291944},
            {45.2325, 12.280556},
            {45.223619, 12.280425},
            {45.408889, 12.261389},
            {45.430556, 12.336664},
            {45.44252, 12.32603},
            {45.445278, 12.336111}
    };



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            ViewGroup container,
                            Bundle savedInstanceState) {
        MapLibre.getInstance(requireContext());

        binding = CriticsZoneBinding.inflate(inflater, container, false);

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);

        //setup della mappa
        mapView.getMapAsync(map -> {

                map.setStyle("https://tiles.openfreemap.org/styles/liberty", style->{
                    //posizione in cui la mappa si apre
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(45.4340,12.3380))
                            .zoom(14.0)
                            .build();
                    map.setCameraPosition(position);

                    //limita a mappa solo ad un determinato territorio -- da mettere il commento


                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(new LatLng(45.45786, 12.29712))  // nord-ovest
                            .include(new LatLng(45.41893, 12.36959))  // sud-est
                            .build();
                    map.setLatLngBoundsForCameraTarget(bounds);

                    map.setMinZoomPreference(11.5);
                    map.setMaxZoomPreference(16.0);

                    //serve per la creazione di rettangoli attorno i punti del sensore
                    for (int i = 0; i < points.length; i++) {

                        double lat = points[i][0];
                        double lng = points[i][1];

                        // 1) Crea il Point
                        Point point = Point.fromLngLat(lng, lat);

                        // 2) Feature
                        Feature feature = Feature.fromGeometry(point);

                        // 3) FeatureCollection
                        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);

                        // 4) Source
                        GeoJsonSource source = new GeoJsonSource("marker-source-" + i, featureCollection);
                        style.addSource(source);

                        // 5) Aggiungi un’icona da drawable (MapLibre accetta Bitmap)
                        style.addImage(
                                "marker-icon",
                                BitmapUtils.getBitmapFromDrawable(
                                        getResources().getDrawable(R.drawable.map_marker)
                                )
                        );

                        // 6) SymbolLayer = marker
                        SymbolLayer layer = new SymbolLayer("marker-layer-" + i, "marker-source-" + i);
                        layer.setProperties(
                                PropertyFactory.iconImage("marker-icon"),
                                PropertyFactory.iconSize(1.0f),
                                PropertyFactory.iconAllowOverlap(true)
                        );

                        style.addLayer(layer);
                    }

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
}
