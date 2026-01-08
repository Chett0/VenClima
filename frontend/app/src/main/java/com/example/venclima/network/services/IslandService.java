package com.example.venclima.network.services;

import com.example.venclima.models.Island;
import com.example.venclima.models.NotificationResponse;
import com.example.venclima.models.NotificationUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface IslandService {

    @GET("api/notifications")
    Call<NotificationResponse> getNotifications();

    @PUT("api/notifications")
    Call<Void> updateNotifications(@Body NotificationUpdateRequest request);

    @GET("api/islands")
    Call<List<Island>> getIslands();
}
