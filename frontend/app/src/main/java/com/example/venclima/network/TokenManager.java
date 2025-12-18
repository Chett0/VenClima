package com.example.venclima.network;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;

public class TokenManager {

    private static final String PREFS_NAME = "venclima_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_EXPIRY = "auth_expiry";

    private static TokenManager instance;
    private final SharedPreferences prefs;

    private TokenManager(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
    }

    public static synchronized TokenManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TokenManager not initialized. Call RetrofitInstance.init(context) or TokenManager.init(context) first.");
        }
        return instance;
    }

    public void saveToken(String token, long expiresInMillis) {
        long expiryAt = System.currentTimeMillis() + expiresInMillis;
        prefs.edit().putString(KEY_TOKEN, token).putLong(KEY_EXPIRY, expiryAt).apply();
    }

    @Nullable
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public boolean isTokenExpired() {
        long expiryAt = prefs.getLong(KEY_EXPIRY, 0);
        return expiryAt == 0 || System.currentTimeMillis() >= expiryAt;
    }

    public void clearToken() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_EXPIRY).apply();
    }

}
