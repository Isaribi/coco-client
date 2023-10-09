package com.ndurska.coco_client.shared;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.ndurska.coco_client.database.Dog;
import com.ndurska.coco_client.database.DogDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestDispatcher {
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
//        String url = "http://10.0.2.2:8080/api/dogs";
//        AtomicReference<Dog> dog = new AtomicReference<>(new Dog());
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response ->
//                {
//                    ObjectMapper mapper = new ObjectMapper();
//                    try {
//                        dog.set(mapper.readValue(response, Dog.class));
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(context, "hi" + response.substring(0, 23), Toast.LENGTH_LONG).show();
//                },
//                error -> {
//                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
//                    Log.println(Log.ERROR, "response", error.toString());
//
//                });
//        queue.add(stringRequest);
//
//        return dog.get();
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
            jsonResponse = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return convert(jsonResponse);
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
    public boolean editDog(DogDto dog) {
        return true;
    }

    public DogDto addDog(DogDto dog) {
        return new DogDto();
    }

}
