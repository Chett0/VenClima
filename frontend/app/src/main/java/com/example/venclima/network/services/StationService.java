package com.example.venclima.network.services;

import com.example.venclima.models.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StationService {

    @GET("api/stations")
    Call<List<Station>> getStations();

}
