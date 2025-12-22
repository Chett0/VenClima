package com.example.venclima.network;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class TokenService {

    private static final Object LOCK = new Object();
    private static boolean refreshing = false;

    /**
     * Ensure there is a fresh access token. If the refresh token is available
     * and a refresh is needed or in progress, this method will perform the
     * refresh synchronously and save the new tokens. Returns true if after
     * the call an access token is available client-side.
     */
    public static boolean ensureFreshToken() {
        try {
            if (TokenManager.getInstance().getRefreshToken() == null) return false;
        } catch (Exception ignored) {
            return false;
        }

        synchronized (LOCK) {
            if (refreshing) {
                try {
                    LOCK.wait(5000);
                } catch (InterruptedException ignored) {}
                try {
                    return !TokenManager.getInstance().isTokenExpired();
                } catch (Exception e) {
                    return false;
                }
            }
            refreshing = true;
        }

        try {
            String refresh;
            try { refresh = TokenManager.getInstance().getRefreshToken(); } catch (Exception e) { refresh = null; }
            if (refresh == null || refresh.isEmpty()) {
                try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                return false;
            }

            OkHttpClient client = new OkHttpClient.Builder().build();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String bodyJson = new Gson().toJson(new RefreshRequest(refresh));
            Request r = new Request.Builder()
                    .url(com.example.venclima.BuildConfig.API_BASE_URL + "api/auth/refresh")
                    .post(RequestBody.create(bodyJson, JSON))
                    .build();

            try (Response resp = client.newCall(r).execute()) {
                if (!resp.isSuccessful()) {
                    try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                    return false;
                }

                ResponseBody rb = resp.body();
                if (rb == null) {
                    try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                    return false;
                }

                String s = rb.string();
                RefreshResponse parsed = new Gson().fromJson(s, RefreshResponse.class);
                if (parsed == null || parsed.getToken() == null) {
                    try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                    return false;
                }

                try {
                    TokenManager.getInstance().saveToken(parsed.getToken(), parsed.getExpiresIn());
                    if (parsed.getRefreshToken() != null) {
                        TokenManager.getInstance().saveRefreshToken(parsed.getRefreshToken(), parsed.getRefreshExpiresIn());
                    }
                } catch (Exception ignored) {}

                return true;
            } catch (IOException e) {
                try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                return false;
            }
        } finally {
            synchronized (LOCK) {
                refreshing = false;
                LOCK.notifyAll();
            }
        }
    }

    public static String getAccessToken() {
        try { return TokenManager.getInstance().getToken(); } catch (Exception e) { return null; }
    }

    public static void clearTokens() {
        try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
    }

    private static class RefreshRequest {
        String refreshToken;
        RefreshRequest(String refreshToken) { this.refreshToken = refreshToken; }
    }

    private static class RefreshResponse {
        private String token;
        private long expiresIn;
        private String refreshToken;
        private long refreshExpiresIn;

        public String getToken() { return token; }
        public long getExpiresIn() { return expiresIn; }
        public String getRefreshToken() { return refreshToken; }
        public long getRefreshExpiresIn() { return refreshExpiresIn; }
    }

}
