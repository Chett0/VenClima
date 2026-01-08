package com.example.venclima.network.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.venclima.models.IslandNotification;
import com.example.venclima.models.NotificationResponse;
import com.example.venclima.models.NotificationUpdateRequest;
import com.example.venclima.network.Callbacks.NotificationUpdateCallback;
import com.example.venclima.network.RetrofitInstance;

import org.maplibre.android.log.Logger;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {

    public static MutableLiveData<NotificationResponse> getNotification() {

        final MutableLiveData<NotificationResponse> notificationsResponse = new MutableLiveData<>();

        RetrofitInstance.getIslandService().getNotifications().enqueue(new Callback<NotificationResponse>() {

            @Override
            public void onResponse(@NonNull Call<NotificationResponse> call, @NonNull Response<NotificationResponse> response) {
                if(response.isSuccessful()) {
                    notificationsResponse.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationResponse> call, @NonNull Throwable t) {
                return;
            }
        });

        return notificationsResponse;

    }

    public static void updateNotification(NotificationUpdateRequest request, NotificationUpdateCallback callback) {

        RetrofitInstance.getIslandService().updateNotifications(request).enqueue(new Callback<Void>() {

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
