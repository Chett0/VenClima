package com.example.venclima.network.repositories;

import androidx.annotation.NonNull;

import com.example.venclima.models.LoginResponse;
import com.example.venclima.models.RegisterUser;
import com.example.venclima.models.User;
import com.example.venclima.network.Callbacks.AuthCallback;
import com.example.venclima.network.RetrofitInstance;
import com.example.venclima.network.TokenManager;
import com.example.venclima.notifications.FirebaseNotificationService;

import org.maplibre.android.log.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    public static void registerUser(RegisterUser user, AuthCallback callback) {

        user.setFcmToken(FirebaseNotificationService.getToken());

        RetrofitInstance.getAuthService().signup(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                Logger.i("RegistrationViewModel", "Registration response: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();
                    try {
                        TokenManager.getInstance().saveToken(body.getToken(), body.getExpiresIn());
                        if (body.getRefreshToken() != null) {
                            TokenManager.getInstance().saveRefreshToken(body.getRefreshToken(), body.getRefreshExpiresIn());
                        }
                    } catch (Exception e) {
                        Logger.e("AuthRepository", "TokenManager not initialized: " + e.getMessage());
                    }
                    if (callback != null) callback.onSuccess("Registrazione avvenuta con successo");
                } else {
                    String err = response.body() != null ? response.body().toString() : "Errore registrazione";
                    if (callback != null) callback.onError(err);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Logger.e("RegistrationViewModel", t.toString());
                if (callback != null) callback.onError("Connessione Fallita");
            }
        });
    }

    public static void login(User user, AuthCallback callback) {

        user.setFcmToken(FirebaseNotificationService.getToken());

        RetrofitInstance.getAuthService().login(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                Logger.i("RegistrationViewModel", "Login response: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();
                    try {
                        TokenManager.getInstance().saveToken(body.getToken(), body.getExpiresIn());
                        if (body.getRefreshToken() != null) {
                            TokenManager.getInstance().saveRefreshToken(body.getRefreshToken(), body.getRefreshExpiresIn());
                        }
                    } catch (Exception e) {
                        Logger.e("AuthRepository", "TokenManager not initialized: " + e.getMessage());
                    }
                    if (callback != null) callback.onSuccess("Login effettuato con successo");
                } else {
                    String err = response.code() == 401 ? "Credenziali errate" : "Errore login";
                    if (callback != null) callback.onError(err);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Logger.e("RegistrationViewModel", t.toString());
                if (callback != null) callback.onError("Connessione Fallita");
            }
        });
    }

}
