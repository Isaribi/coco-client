package com.ndurska.coco_client.shared;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ndurska.coco_client.database.DogDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestDispatcher {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Context context;

    public RequestDispatcher(Context context) {
        this.context = context;
    }

    public int getLastPaidAmount(int id) {
        return 0;
    }

    public List<DogDto> getSameOwnerDogs(int id) {
        return List.of();
    }

    public boolean nameTakenByDifferentDog(String fullName) {
        return false;
    }

    public void deleteDog(int dogID) {
    }

    public DogDto getDog(int dogID) {

        return null;
    }

    public List<DogDto> getDogs() {
        String url = "http://10.0.2.2:8080/api/dogs";
        OkHttpClient client = new OkHttpClient();
        String jsonResponse = "";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            jsonResponse = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertToDogList(jsonResponse);
    }


    public boolean editDog(DogDto dog) {
        return true;
    }

    public DogDto addDog(DogDto dog) {
        String url = "http://10.0.2.2:8080/api/dogs";
        OkHttpClient client = new OkHttpClient();
        String jsonResponse = "";
        Request request = new Request.Builder()
                .post(Objects.requireNonNull(convertToJson(dog)))
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            jsonResponse = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertToDog(jsonResponse);
    }

    private RequestBody convertToJson(DogDto dogDto) {
        try {
            ObjectWriter objectWriter = new ObjectMapper().writer();
            String json = objectWriter.writeValueAsString(dogDto);
            return RequestBody.create(JSON, json);
        } catch (JsonProcessingException e) {
            Log.println(Log.ERROR, "json", "Error while processing dog to JSON. " + e.getMessage());
        }
        return null;
    }

    public DogDto convertToDog(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, DogDto.class);

        } catch (JsonProcessingException e) {
            Log.println(Log.ERROR, "json", "Error while processing dog to JSON. " + e.getMessage());
        }
        return new DogDto();
    }

    public List<DogDto> convertToDogList(String jsonString) {
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
