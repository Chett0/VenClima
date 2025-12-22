package com.example.venclima.network.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.venclima.models.Tide;
import com.example.venclima.network.RetrofitInstance;

import java.util.List;
import org.maplibre.android.log.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TideRepository {

    public static MutableLiveData<List<Tide>> getDailyTides() {

        final MutableLiveData<List<Tide>> dailyTides = new MutableLiveData<>();

        RetrofitInstance.getTideService().getDailyTides().enqueue(new Callback<List<Tide>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tide>> call, @NonNull Response<List<Tide>> response) {
                dailyTides.setValue(response.body());
                Logger.i("TideRepository", "Tides loaded");
            }

            @Override
            public void onFailure(@NonNull Call<List<Tide>> call, @NonNull Throwable t) {
                Logger.e("TideRepository", t.toString());
                return;
            }
        });

        return dailyTides;

    }

}
