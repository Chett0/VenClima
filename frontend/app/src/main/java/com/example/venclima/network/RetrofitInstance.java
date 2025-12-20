package com.example.venclima.network;

import com.example.venclima.BuildConfig;
import com.example.venclima.network.services.AuthService;
import com.example.venclima.network.services.StationService;
import com.example.venclima.network.services.TideService;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitInstance {

    private static final String BASE_URL = BuildConfig.API_BASE_URL;
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // call init(context) first to configure TokenManager/Interceptor if needed
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Initialize Retrofit with AuthInterceptor and TokenManager. Call once from Application.onCreate()
     */
    public static synchronized void init(Context context) {
        if (retrofit != null) return;

        TokenManager.init(context);

        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(new AuthInterceptor())
            .addInterceptor(new LoggingInterceptor())
            .authenticator(new TokenAuthenticator())
            .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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
