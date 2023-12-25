package com.ndurska.coco_client.database.web;

import com.ndurska.coco_client.database.dto.DogDto;
import com.ndurska.coco_client.shared.AbstractRequestDispatcher;
import com.ndurska.coco_client.shared.JsonConverter;

import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class DogsRequestDispatcher extends AbstractRequestDispatcher {

    private final String url = "http://10.0.2.2:8080/api/dogs";

    private final JsonConverter<DogDto> jsonConverter;

    public DogsRequestDispatcher() {
        jsonConverter = new JsonConverter<>(DogDto.class);
    }


    public List<DogDto> getSameOwnerDogs(int id) {

        HttpUrl httpUrl = HttpUrl
                .parse(url + "/sameOwner")
                .newBuilder()
                .addQueryParameter("id", String.valueOf(id))
                .build();

        Request request = new Request.Builder()
                .get()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return jsonConverter.convertToList(jsonResponse);
    }

    public boolean nameTakenByDifferentDog(String fullName) {
        return false;
    }

    public boolean deleteDog(int dogID) {
        HttpUrl httpUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(dogID))
                .build();

        Request request = new Request.Builder()
                .delete()
                .url(httpUrl)
                .build();

        return callRequestForCode(request) == 200;
    }

    public DogDto getDog(int dogID) {
        HttpUrl httpUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(dogID))
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return jsonConverter.convertToObject(jsonResponse);
    }

    public List<DogDto> getDogs() {
        Request request = new Request.Builder()
                .url(url + "/all")
                .build();

        String jsonResponse = callRequestForBody(request);

        return jsonConverter.convertToList(jsonResponse);
    }


    public DogDto editDog(DogDto dog) {
        HttpUrl httpUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(dog.getId()))
                .build();

        Request request = new Request.Builder()
                .put(Objects.requireNonNull(jsonConverter.createJsonRequestBody(dog)))
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return jsonConverter.convertToObject(jsonResponse);
    }

    public DogDto addDog(DogDto dog) {
        Request request = new Request.Builder()
                .post(Objects.requireNonNull(jsonConverter.createJsonRequestBody(dog)))
                .url(url)
                .build();

        String jsonResponse = callRequestForBody(request);

        return jsonConverter.convertToObject(jsonResponse);
    }


    public List<DogDto> getDogsByIds(List<Integer> iDs) {
        HttpUrl httpUrl = HttpUrl
                .parse(url + "byIds")
                .newBuilder()
                .addQueryParameter("ids", String.valueOf(iDs))
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return jsonConverter.convertToList(jsonResponse);
    }
}
