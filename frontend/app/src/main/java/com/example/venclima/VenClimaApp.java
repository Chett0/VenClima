package com.example.venclima;

import android.app.Application;
import com.example.venclima.network.RetrofitInstance;

public class VenClimaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Retrofit and TokenManager
        RetrofitInstance.init(getApplicationContext());
    }

}
