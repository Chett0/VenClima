package com.example.venclima.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.venclima.models.Station;
import com.example.venclima.models.Tide;
import com.example.venclima.network.repositories.StationRepository;
import com.example.venclima.network.repositories.TideRepository;


import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TideForecastViewModel extends ViewModel {

    private MutableLiveData<List<Tide>> dailyTides = new MutableLiveData<>();
    private MutableLiveData<List<Tide>> realTimeTides = new MutableLiveData<>();
    private MutableLiveData<List<Station>> stations = new MutableLiveData<>();
    private MutableLiveData<Map<Integer, List<Tide>>> stationTides = new MutableLiveData<>();

    public TideForecastViewModel() {
        this.loadStations();
        this.loadTides();
    }

    public LiveData<List<Tide>> getDailyTides() {
        return dailyTides;
    }

    public LiveData<Map<Integer, List<Tide>>> getStationTides() {
        return stationTides;
    }

    public LiveData<List<Tide>> getRealTimeTides() {
        return realTimeTides;
    }

    public LiveData<List<Station>> getStations() {
        return stations;
    }

    public void setDailyTides(MutableLiveData<List<Tide>> dailyTides) {
        this.dailyTides = dailyTides;
    }

    public void loadTides() {
            this.setDailyTides(TideRepository.getDailyTides());
            this.stationTides = TideRepository.getDailyTidesMap();
    }

    public void loadStations() {
        this.stations = StationRepository.getStations();
    }



}
