package com.example.venclima.network.services;

import com.example.venclima.models.RegisterUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/api/auth/login")
    Call<ResponseBody> login();
    @POST("api/auth/signup")
    Call<Void> signup(@Body RegisterUser user);


}
