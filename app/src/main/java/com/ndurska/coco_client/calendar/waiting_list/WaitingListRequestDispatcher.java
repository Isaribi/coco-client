package com.ndurska.coco_client.calendar.waiting_list;

import com.ndurska.coco_client.shared.AbstractRequestDispatcher;
import com.ndurska.coco_client.shared.JsonConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class WaitingListRequestDispatcher extends AbstractRequestDispatcher {
    private final String url = "http://10.0.2.2:8080/api/waiting-list";
    private final JsonConverter<WaitingListRecordDto> appointmentJsonConverter;

    public WaitingListRequestDispatcher() {
        appointmentJsonConverter = new JsonConverter<>(WaitingListRecordDto.class);
    }

    public WaitingListRecordDto addWaitlingListRecord(WaitingListRecordDto waitingListRecordDto) {

        Request request = new Request.Builder()
                .post(Objects.requireNonNull(appointmentJsonConverter.createJsonRequestBody(waitingListRecordDto)))
                .url(url)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToObject(jsonResponse);
    }

    public List<WaitingListRecordDto> getWaitingListForChosenDay(LocalDate date) {
        HttpUrl httpUrl = HttpUrl
                .parse(url + "/for-day")
                .newBuilder()
                .addQueryParameter("date", String.valueOf(date))
                .build();

        Request request = new Request.Builder()
                .get()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToList(jsonResponse);
    }

    public List<WaitingListRecordDto> getWaitingListForChosenWeek(LocalDate monday) {
        HttpUrl httpUrl = HttpUrl
                .parse(url + "/for-week")
                .newBuilder()
                .addQueryParameter("monday", String.valueOf(monday))
                .build();

        Request request = new Request.Builder()
                .get()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToList(jsonResponse);
    }

    public List<WaitingListRecordDto> getWaitingList() {
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        String jsonResponse = callRequestForBody(request);
        return appointmentJsonConverter.convertToList(jsonResponse);
    }

    public boolean deleteWaitingListRecord(long id) {
        HttpUrl httpUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(id))
                .build();

        Request request = new Request.Builder()
                .delete()
                .url(httpUrl)
                .build();

        return callRequestForCode(request) == 200;
    }
}
