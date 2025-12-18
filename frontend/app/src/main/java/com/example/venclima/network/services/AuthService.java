package com.example.venclima.network.services;

import com.example.venclima.models.RegisterUser;
import com.example.venclima.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface AuthService {

    @POST("/api/auth/login")
    Call<com.example.venclima.models.LoginResponse> login(@Body User user);
    @POST("api/auth/signup")
    Call<Void> signup(@Body RegisterUser user);

    @GET("/api/auth/me")
    Call<com.example.venclima.models.UserDTO> me();


}
