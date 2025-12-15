package com.example.venclima.network.services;

import com.example.venclima.models.Tide;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TideService {

    @GET("api/tides/day")
    Call<List<Tide>> getDailyTides();

}
