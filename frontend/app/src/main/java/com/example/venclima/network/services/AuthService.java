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
    Call<com.example.venclima.models.LoginResponse> signup(@Body RegisterUser user);

    @GET("/api/auth/me")
    Call<com.example.venclima.models.UserDTO> me();

    @POST("/api/auth/logout")
    Call<java.util.Map<String, String>> logout(@Body java.util.Map<String, String> body);


}
