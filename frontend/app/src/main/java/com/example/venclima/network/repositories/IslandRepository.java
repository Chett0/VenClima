package com.example.venclima.network.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.venclima.models.Island;
import com.example.venclima.network.services.IslandService;
import com.example.venclima.network.RetrofitInstance;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IslandRepository {

    private final IslandService islandService;

    public IslandRepository() {
        islandService = RetrofitInstance.getIslandService();
    }

    public LiveData<List<Island>> getIslands() {
        MutableLiveData<List<Island>> data = new MutableLiveData<>();

        islandService.getIslands().enqueue(new Callback<List<Island>>() {
            @Override
            public void onResponse(
                    Call<List<Island>> call,
                    Response<List<Island>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<List<Island>> call, Throwable t) {
                data.setValue(Collections.emptyList());
            }
        });

        return data;
    }
}