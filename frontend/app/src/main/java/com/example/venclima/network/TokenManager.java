package com.example.venclima.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
 
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.annotation.Nullable;

public class TokenManager {

    private static final String PREFS_NAME = "venclima_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_EXPIRY = "auth_expiry";
    private static final String KEY_REFRESH = "refresh_token";
    private static final String KEY_REFRESH_EXPIRY = "refresh_expiry";

    private static TokenManager instance;
    private final SharedPreferences prefs;
    private final Context appContext;
    private static final long EXPIRY_BUFFER_MS = 30_000; // 30 seconds

    private TokenManager(Context context) {
        this.appContext = context.getApplicationContext();
        this.prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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

    public void saveRefreshToken(String refreshToken, long expiresInMillis) {
        long expiryAt = System.currentTimeMillis() + expiresInMillis;
        prefs.edit().putString(KEY_REFRESH, refreshToken).putLong(KEY_REFRESH_EXPIRY, expiryAt).apply();
    }

    @Nullable
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    @Nullable
    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH, null);
    }

    public boolean isTokenExpired() {
        long expiryAt = prefs.getLong(KEY_EXPIRY, 0);
        if (expiryAt == 0) return true;
        // < 30sec -> token expired
        return System.currentTimeMillis() >= (expiryAt - EXPIRY_BUFFER_MS);
    }

    public void clearToken() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_EXPIRY).apply();
    }

    public void clearRefreshToken() {
        prefs.edit().remove(KEY_REFRESH).remove(KEY_REFRESH_EXPIRY).apply();
    }

    public boolean isRefreshTokenExpired() {
        long expiryAt = prefs.getLong(KEY_REFRESH_EXPIRY, 0);
        return (expiryAt == 0) || (System.currentTimeMillis() >= expiryAt);
    }


    public void notifyTokenExpired() {
        clearToken();
        clearRefreshToken();
        try {
            Intent intent = new Intent("com.example.venclima.ACTION_TOKEN_EXPIRED");
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(intent);
        } catch (Exception ignored) {
        }
    }

}
