package com.example.venclima.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.venclima.BR;
import com.example.venclima.models.Station;
import com.example.venclima.models.Tide;
import com.example.venclima.network.RetrofitInstance;

import org.maplibre.android.log.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class TideForecastViewModel extends ViewModel {

    private MutableLiveData<List<Tide>> dailyTides = new MutableLiveData<>();
    private MutableLiveData<List<Tide>> realTimeTides = new MutableLiveData<>();
    private MutableLiveData<List<Station>> stations = new MutableLiveData<>();

    public TideForecastViewModel() {
        loadStations();
        loadTides();
    }

    public LiveData<List<Tide>> getTides() {
        return dailyTides;
    }

    public LiveData<List<Tide>> getRealTimeTides() {
        return realTimeTides;
    }

    public LiveData<List<Station>> getStations() {
        return stations;
    }


    public void loadTides() {
       RetrofitInstance.getApiInterface().getDailyTides().enqueue(new Callback<List<Tide>>() {
           @Override
           public void onResponse(@NonNull Call<List<Tide>> call, @NonNull Response<List<Tide>> response) {
                dailyTides.setValue(response.body());
                if(dailyTides.getValue() != null) {
                    int stationsSize = 0;
                    if(stations.getValue() != null)
                        realTimeTides.setValue(dailyTides.getValue().subList(0, stations.getValue().size() - 1));
                }
           }

           @Override
           public void onFailure(@NonNull Call<List<Tide>> call, @NonNull Throwable t) {

           }
       });
    }
    public void loadStations() {
        RetrofitInstance.getApiInterface().getStations().enqueue(new Callback<List<Station>>() {

            @Override
            public void onResponse(@NonNull Call<List<Station>> call, @NonNull Response<List<Station>> response) {
                stations.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Station>> call, @NonNull Throwable t) {
                Log.e("TideForecastViewModel", t.toString());
            }
        });
    }



}
