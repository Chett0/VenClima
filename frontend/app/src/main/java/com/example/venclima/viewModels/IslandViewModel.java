package com.example.venclima.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.venclima.models.IslandsTide;
import com.example.venclima.network.repositories.IslandRepository;



public class IslandViewModel extends ViewModel {

    private final IslandRepository repository;
    private LiveData<IslandsTide> islandTides;

    public IslandViewModel() {
        repository = new IslandRepository();
    }

    public LiveData<IslandsTide> getIslandTides() {
        if (islandTides == null) {
            islandTides = repository.getIslandTides();
        }
        return islandTides;
    }
}
