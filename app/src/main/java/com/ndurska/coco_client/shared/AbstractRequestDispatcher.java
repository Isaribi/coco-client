package com.ndurska.coco_client.shared;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class AbstractRequestDispatcher {

    private OkHttpClient client;
    private Context context;

    public AbstractRequestDispatcher(Context context) {
        this.context = context;
        SharedPreferences editor = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        TokenHandler.jwtToken = editor.getString("jwtToken", null);
        client = MyHttpClient.getClient(context);
    }

    protected String callRequestForBody(Request request) {
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    protected Response callRequestForResponse(Request request) {
        try (Response response = client.newCall(request).execute()) {
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected int callRequestForCode(Request request) {
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 400;
    }

    public Request.Builder getRequestBuilder() {
        return new Request.Builder()
                .addHeader("Authorization", "Bearer " + TokenHandler.jwtToken);
    }
}
