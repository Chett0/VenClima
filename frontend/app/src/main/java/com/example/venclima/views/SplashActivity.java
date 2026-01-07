package com.example.venclima.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.venclima.R;
import com.example.venclima.models.Tide;
import com.example.venclima.models.Station;
import com.example.venclima.network.repositories.StationRepository;
import com.example.venclima.network.repositories.TideRepository;

import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static final long MIN_DISPLAY_MS = 800; // min 0.8s
    private static final long MAX_WAIT_MS = 2500; // max 2.5s for requests

    private boolean dailyLoaded = false;
    private boolean mapLoaded = false;
    private boolean stationsLoaded = false;

    private long startMillis;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startMillis = System.currentTimeMillis();

        
        TideRepository.getDailyTides().observe(this, new Observer<List<Tide>>() {
            @Override
            public void onChanged(List<Tide> tides) {
                if (tides != null) dailyLoaded = true;
                checkProceed();
            }
        });

        TideRepository.getDailyTidesMap().observe(this, new Observer<Map<Integer, List<Tide>>>() {
            @Override
            public void onChanged(Map<Integer, List<Tide>> map) {
                if (map != null) mapLoaded = true;
                checkProceed();
            }
        });

        StationRepository.getStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(List<Station> stations) {
                if (stations != null) stationsLoaded = true;
                checkProceed();
            }
        });

        // Fallback after 2,5 ms if fetches fail
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkProceed(true);
            }
        }, MAX_WAIT_MS);
    }

    private void checkProceed() {
        checkProceed(false);
    }

    private void checkProceed(boolean forced) {
        long elapsed = System.currentTimeMillis() - startMillis;

        boolean ready = (dailyLoaded && mapLoaded && stationsLoaded);
        if (ready || forced) {
            long remaining = MIN_DISPLAY_MS - elapsed;
            if (remaining > 0 && !forced) {
                handler.postDelayed(this::startMain, remaining);
            } else {
                startMain();
            }
        }
    }

    private void startMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
