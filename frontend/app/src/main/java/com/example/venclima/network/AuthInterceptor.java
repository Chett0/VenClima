package com.example.venclima.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        String token = null;
        try {
            token = TokenManager.getInstance().getToken();
        } catch (IllegalStateException ignored) {
        }

        if (token == null || token.isEmpty()) {
            return chain.proceed(original);
        }

        // check expired or near-expired (< 30 sec) tokens
        boolean expired = false;
        try {
            expired = TokenManager.getInstance().isTokenExpired();
        } catch (IllegalStateException ignored) {
        }

        if (expired) {
            try {
                boolean refreshed = TokenService.ensureFreshToken();
                if (refreshed) {
                    try { token = TokenManager.getInstance().getToken(); } catch (Exception ignored) {}
                }
            } catch (Exception e) {
            }
        }

        Request.Builder rb = original.newBuilder().method(original.method(), original.body());
        if (token != null && !token.isEmpty()) rb.header("Authorization", "Bearer " + token);
        Request request = rb.build();

        return chain.proceed(request);
    }
}
