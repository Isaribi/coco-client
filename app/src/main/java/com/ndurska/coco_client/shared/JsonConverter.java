package com.ndurska.coco_client.shared;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class JsonConverter<T> {
    private Class<T> type;
    private ObjectMapper objectMapper;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public JsonConverter(Class<T> type) {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        this.type = type;
    }

    public RequestBody createJsonRequestBody(T object) {
        try {
            ObjectWriter objectWriter = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .writer();
            String json = objectWriter.writeValueAsString(object);
            return RequestBody.create(JSON, json);
        } catch (JsonProcessingException e) {
            Log.println(Log.ERROR, "json", "Error while processing object to JSON. " + e.getMessage());
        }
        return null;
    }

    public T convertToObject(String jsonString) {

        try {
            return objectMapper.readValue(jsonString, type);

        } catch (JsonProcessingException e) {
            Log.println(Log.ERROR, "json", "Error while processing object to JSON. " + e.getMessage());
        }
        return null;
    }

    public List<T> convertToList(String jsonString) {
        List<T> list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            JsonNode jsonArray = objectMapper.readTree(jsonString);

            for (JsonNode element : jsonArray) {
                T dto = objectMapper.treeToValue(element, type);
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
