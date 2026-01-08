package com.example.venclima.network.services;

import com.example.venclima.models.Island;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IslandService {
    @GET("api/islands")
    Call<List<Island>> getIslands();
}