package com.example.venclima.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.venclima.models.Station;
import com.example.venclima.models.Tide;
import com.example.venclima.network.Callbacks.NetworkErrorCallback;
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

    private MutableLiveData<Boolean> isError = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsError() {
        return isError;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }


    public TideForecastViewModel() {
        this.loadData();
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

    public void refreshData() {
        this.loadData();
    }

    private void loadData(){
        this.isLoading.setValue(true);
        this.loadStations();
        this.loadTides();
    }

    public void loadStations() {
        this.stations = StationRepository.getStations(new NetworkErrorCallback() {
            @Override
            public void onSuccess() {
                isError.setValue(false);
                isLoading.setValue(false);
            }

            @Override
            public void onError() {
                isError.setValue(true);
                isLoading.setValue(false);
            }
        });
    }



}
