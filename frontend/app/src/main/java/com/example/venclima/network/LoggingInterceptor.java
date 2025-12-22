package com.example.venclima.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.maplibre.android.log.Logger;

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Logger.i("HTTP", "--> " + request.method() + " " + request.url());
        if (request.body() != null) {
            try {
                Logger.i("HTTP", "Request headers: " + request.headers().toString());
            } catch (Exception ignored) {}
        }

        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        String bodyString = null;
        if (responseBody != null) {
            MediaType contentType = responseBody.contentType();
            byte[] bytes = responseBody.bytes();
            bodyString = new String(bytes);
            // recreate response body because .bytes() consumed it
            ResponseBody newBody = ResponseBody.create(contentType, bytes);
            response = response.newBuilder().body(newBody).build();
        }

        Logger.i("HTTP", "<-- " + response.code() + " " + request.url() + (bodyString != null ? " body=" + bodyString : ""));

        return response;
    }
}
