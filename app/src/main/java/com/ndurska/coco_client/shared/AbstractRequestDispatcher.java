package com.ndurska.coco_client.shared;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class AbstractRequestDispatcher {

    protected String callRequestForBody(Request request) {
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected int callRequestForCode(Request request) {
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 400;
    }
}
