package com.example.venclima.network.services;

import com.example.venclima.models.RegisterUser;
import com.example.venclima.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/api/auth/login")
    Call<Void> login(@Body User user);
    @POST("api/auth/signup")
    Call<Void> signup(@Body RegisterUser user);


}
