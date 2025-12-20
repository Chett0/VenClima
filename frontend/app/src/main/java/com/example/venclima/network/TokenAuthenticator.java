package com.example.venclima.network;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.ResponseBody;
import com.google.gson.Gson;

public class TokenAuthenticator implements Authenticator {

    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {
        // prevent infinite loops
        if (responseCount(response) >= 2) return null;

        String refresh = null;
        try {
            refresh = TokenManager.getInstance().getRefreshToken();
        } catch (Exception e) {
        }

        if (refresh == null || refresh.isEmpty()) {
            // no refresh token -> force logout
            try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
            return null;
        }

        // Delegate refresh to centralized TokenService which serializes refresh attempts
        try {
            boolean ok = TokenService.ensureFreshToken();
            if (!ok) {
                try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                return null;
            }

            String newToken = TokenService.getAccessToken();
            if (newToken == null || newToken.isEmpty()) {
                try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                return null;
            }

            return response.request().newBuilder()
                    .header("Authorization", "Bearer " + newToken)
                    .build();
        } catch (Exception e) {
            try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
            return null;
        }
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) result++;
        return result;
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
