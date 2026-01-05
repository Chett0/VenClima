package com.example.venclima.network.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.venclima.models.IslandNotification;
import com.example.venclima.network.Callbacks.NotificationUpdateCallback;
import com.example.venclima.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {

    public static MutableLiveData<List<IslandNotification>> getNotification() {

        final MutableLiveData<List<IslandNotification>> islandNotifications = new MutableLiveData<>();

        RetrofitInstance.getIslandService().getNotifications().enqueue(new Callback<List<IslandNotification>>() {

            @Override
            public void onResponse(@NonNull Call<List<IslandNotification>> call, @NonNull Response<List<IslandNotification>> response) {
                islandNotifications.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<IslandNotification>> call, @NonNull Throwable t) {
                return;
            }
        });

        return islandNotifications;

    }

    public static void updateNotification(List<Integer> islandsIds, NotificationUpdateCallback callback) {

        RetrofitInstance.getIslandService().updateNotifications(islandsIds).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    String error = "Error: " + response.code();
                    callback.onFailure(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });



    }

}
