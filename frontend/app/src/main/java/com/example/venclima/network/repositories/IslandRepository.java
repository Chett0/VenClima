package com.example.venclima.network.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.venclima.models.Island;
import com.example.venclima.models.IslandsTide;
import com.example.venclima.network.services.IslandService;
import com.example.venclima.network.RetrofitInstance;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IslandRepository {

    private final IslandService islandService;
    private final MutableLiveData<IslandsTide> data = new MutableLiveData<>();
    public IslandRepository() {
        islandService = RetrofitInstance.getIslandService();
    }

    public LiveData<IslandsTide> getIslandTides() {
        return data;
    }

    public void fetchIslandTides(){
        islandService.getIslandTides().enqueue(new Callback<IslandsTide>() {
            @Override
            public void onResponse(Call<IslandsTide> call, Response<IslandsTide> response) {
                if(response.isSuccessful() && response.body() !=null) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<IslandsTide> call, Throwable t) {
                data.setValue(new IslandsTide());
            }
        });
    }

}