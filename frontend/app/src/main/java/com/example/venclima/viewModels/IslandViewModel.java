package com.example.venclima.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.venclima.models.Island;
import com.example.venclima.network.repositories.IslandRepository;

import java.util.List;


public class IslandViewModel extends ViewModel {

    private final IslandRepository repository;
    private LiveData<List<Island>> islands;

    public IslandViewModel() {
        repository = new IslandRepository();
    }

    public LiveData<List<Island>> getIslands() {
        if (islands == null) {
            islands = repository.getIslands();
        }
        return islands;
    }
}
