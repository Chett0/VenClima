package com.example.venclima.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.venclima.models.IslandsTide;
import com.example.venclima.network.repositories.IslandRepository;



public class IslandViewModel extends ViewModel {

    private final IslandRepository repository = new IslandRepository();
    private LiveData<IslandsTide> islandTides = repository.getIslandTides();

    public LiveData<IslandsTide> getIslandTides() {
        return islandTides;
    }

    public void refreshIslandTides(){
        repository.fetchIslandTides();
    }
}
