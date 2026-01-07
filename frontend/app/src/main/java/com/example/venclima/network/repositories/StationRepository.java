package com.example.venclima.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.venclima.models.Station;
import com.example.venclima.network.RetrofitInstance;

import org.maplibre.android.log.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationRepository {

    public static MutableLiveData<List<Station>> getStations() {

        final MutableLiveData<List<Station>> stations = new MutableLiveData<>();

        RetrofitInstance.getStationService().getStations().enqueue(new Callback<List<Station>>() {

            @Override
            public void onResponse(@NonNull Call<List<Station>> call, @NonNull Response<List<Station>> response) {
                stations.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Station>> call, @NonNull Throwable t) {
                Logger.e("StationRepository", t.toString());
                return;
            }
        });

        return stations;

    }

}
