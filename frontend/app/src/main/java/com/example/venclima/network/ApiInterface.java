package com.example.venclima.network;

import com.example.venclima.models.RegisterUser;
import com.example.venclima.models.User;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("/api/tides")
    Call<ResponseBody> postTides();

    @POST("/api/auth/login")
    Call<ResponseBody> login();

    @POST("/api/auth/signup")
    Call<Void> signup(@Body RegisterUser user);



}
