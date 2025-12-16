package com.example.venclima.network.repositories;

import androidx.annotation.NonNull;

import com.example.venclima.models.RegisterUser;
import com.example.venclima.models.User;
import com.example.venclima.network.RetrofitInstance;
import com.example.venclima.notifications.FirebaseNotificationService;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.maplibre.android.log.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    public static void registerUser(RegisterUser user) {

        user.setFcmToken(FirebaseNotificationService.getToken());

        RetrofitInstance.getAuthService().signup(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Logger.i("RegistrationViewModel", "Registration successful");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Logger.e("RegistrationViewModel", t.toString());
            }
        });
    }

    public static void login(User user) {

        user.setFcmToken(FirebaseNotificationService.getToken());

        RetrofitInstance.getAuthService().login(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Logger.i("RegistrationViewModel", "Login successful");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Logger.e("RegistrationViewModel", t.toString());
            }
        });
    }

}
