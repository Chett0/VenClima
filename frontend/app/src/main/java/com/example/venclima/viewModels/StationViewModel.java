package com.example.venclima.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.venclima.models.Station;
import com.example.venclima.network.repositories.StationRepository;

import java.util.List;

public class StationViewModel extends ViewModel {

    private final StationRepository repository;

    private MutableLiveData<List<Station>> station;

    public  StationViewModel(){
        repository = new StationRepository();
    }

    public MutableLiveData<List<Station>> getStation(){
        if(station == null){
            station = repository.getStations();
        }
        return station;
    }

}


