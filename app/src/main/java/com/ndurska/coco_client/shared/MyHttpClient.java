package com.ndurska.coco_client.shared;

import android.content.Context;

import okhttp3.OkHttpClient;

public class MyHttpClient {
    public static OkHttpClient getClient(Context context) {
        
        // Add the unauthorized interceptor to handle 401 responses
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        UnauthorizedInterceptor unauthorizedInterceptor = new UnauthorizedInterceptor(context); // 'this' is the context
        httpClient.addInterceptor(unauthorizedInterceptor);

        return httpClient.build();
    }
}