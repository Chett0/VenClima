package com.example.venclima.network.services;

import com.example.venclima.models.IslandNotification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface IslandService {

    @GET("api/notifications")
    Call<List<IslandNotification>> getNotifications();

    @PUT("api/notifications")
    Call<Void> updateNotifications(List<Integer> islandsIds);

}
