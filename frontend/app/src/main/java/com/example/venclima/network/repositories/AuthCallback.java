package com.example.venclima.network.repositories;

public interface AuthCallback {
    void onSuccess(String message);
    void onError(String message);
}
