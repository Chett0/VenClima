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

        Request request = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .method(original.method(), original.body())
                .build();

        return chain.proceed(request);
    }
}
