package com.ndurska.coco_client.shared;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndurska.coco_client.database.DatabaseActivity;
import com.ndurska.coco_client.database.DogDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DogIntentService extends IntentService {
    /**
     * @param name
     * @deprecated
     */
    public DogIntentService(String name) {
        super(name);
    }


    public DogIntentService( ) {
        super("dogIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String action = intent.getAction();

       // if(action.equals("getAll")){
            String url = "http://10.0.2.2:8080/api/dogs/all";
            OkHttpClient client = new OkHttpClient();
            String jsonResponse = "";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                jsonResponse = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sendBroadcast(jsonResponse);
        // }

    }

    private void sendBroadcast(String response) {
        Intent broadcastIntent = new Intent(getApplicationContext(), DogBroadcastReceiver.class);
        broadcastIntent.setAction("FILER_ACTION");
        broadcastIntent.putExtra("dogs", response);
        sendBroadcast(broadcastIntent);
        Log.d("DogIntentService", "sendBroadcast" + response.substring(0,50));
    }

    public List<DogDto> convert(String jsonString) {
        List<DogDto> personList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonArray = objectMapper.readTree(jsonString);

            for (JsonNode element : jsonArray) {
                DogDto person = objectMapper.treeToValue(element, DogDto.class);
                personList.add(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personList;
    }
}
