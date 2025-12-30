package com.example.venclima.network.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.venclima.models.Tide;
import com.example.venclima.network.RetrofitInstance;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.maplibre.android.log.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TideRepository {
    private static final long CACHE_TTL_MS = 2 * 60 * 1000; // 2 min cache
    private static Map<Integer, List<Tide>> cachedMap = null;
    private static long lastFetchMillis = 0;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static MutableLiveData<List<Tide>> getDailyTides() {

        final MutableLiveData<List<Tide>> dailyTides = new MutableLiveData<>();

        RetrofitInstance.getTideService().getDailyTides().enqueue(new Callback<List<Tide>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tide>> call, @NonNull Response<List<Tide>> response) {
                dailyTides.setValue(response.body());
                Logger.i("TideRepository", "Tides loaded");
            }

            @Override
            public void onFailure(@NonNull Call<List<Tide>> call, @NonNull Throwable t) {
                Logger.e("TideRepository", t.toString());
                return;
            }
        });

        return dailyTides;

    }

    public static synchronized MutableLiveData<Map<Integer, List<Tide>>> getDailyTidesMap() {
        final MutableLiveData<Map<Integer, List<Tide>>> result = new MutableLiveData<>();

        long nowMillis = System.currentTimeMillis();
        if (cachedMap != null && (nowMillis - lastFetchMillis) < CACHE_TTL_MS) {
            result.setValue(cachedMap);
            Logger.i("TideRepository", "Returning cached daily tides map");
            return result;
        }

        RetrofitInstance.getTideService().getDailyTides().enqueue(new Callback<List<Tide>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tide>> call, @NonNull Response<List<Tide>> response) {
                List<Tide> body = response.body();
                Map<Integer, List<Tide>> map = new HashMap<>();

                if (body != null) {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime start = LocalDate.now().atStartOfDay();

                    for (Tide t : body) {
                        if (t == null || t.getStationId() == null || t.getDate() == null)
                            continue;
                        try {
                            LocalDateTime dt = LocalDateTime.parse(t.getDate(), FORMATTER);
                            if (dt.isBefore(start) || dt.isAfter(now))
                                continue; // keep only from midnight to now

                            Integer sid = t.getStationId();
                            map.computeIfAbsent(sid, k -> new ArrayList<>()).add(t);
                        } catch (Exception ex) {
                            Logger.e("TideRepository", "Failed to parse tide date: " + t.getDate());
                        }
                    }

                    // sort each list ascending by date
                    for (List<Tide> list : map.values()) {
                        Collections.sort(list, new Comparator<Tide>() {
                            @Override
                            public int compare(Tide o1, Tide o2) {
                                LocalDateTime d1 = LocalDateTime.parse(o1.getDate(), FORMATTER);
                                LocalDateTime d2 = LocalDateTime.parse(o2.getDate(), FORMATTER);
                                return d1.compareTo(d2);
                            }
                        });
                    }
                }

                cachedMap = map;
                lastFetchMillis = System.currentTimeMillis();
                result.setValue(map);
                Logger.i("TideRepository", "Daily tides map built and cached");
            }

            @Override
            public void onFailure(@NonNull Call<List<Tide>> call, @NonNull Throwable t) {
                Logger.e("TideRepository", t.toString());
            }
        });

        return result;
    }

    public static synchronized void clearCache() {
        cachedMap = null;
        lastFetchMillis = 0;
    }

}
