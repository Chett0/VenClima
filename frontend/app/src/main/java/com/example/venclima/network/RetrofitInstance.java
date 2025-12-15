package com.example.venclima.network;

import com.example.venclima.BuildConfig;
import com.example.venclima.network.services.AuthService;
import com.example.venclima.network.services.StationService;
import com.example.venclima.network.services.TideService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static final String BASE_URL = BuildConfig.API_BASE_URL;
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

//    public static ApiInterface getApiInterface() {
//        return getRetrofitInstance().create(ApiInterface.class);
//    }

    public static TideService getTideService() {
        return getRetrofitInstance().create(TideService.class);
    }

    public static AuthService getAuthService() {
        return getRetrofitInstance().create(AuthService.class);
    }

    public static StationService getStationService() {
        return getRetrofitInstance().create(StationService.class);
    }


}
