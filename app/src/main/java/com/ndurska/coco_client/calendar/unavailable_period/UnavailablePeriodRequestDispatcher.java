package com.ndurska.coco_client.calendar.unavailable_period;

import android.content.Context;

import com.ndurska.coco_client.shared.AbstractRequestDispatcher;
import com.ndurska.coco_client.shared.JsonConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.Request;


public class UnavailablePeriodRequestDispatcher extends AbstractRequestDispatcher {
    private final String url = "http://10.0.2.2:8080/api/unavailable-periods";
    private final JsonConverter<UnavailablePeriodDto> appointmentJsonConverter;

    public UnavailablePeriodRequestDispatcher(Context context) {
        super(context);
        appointmentJsonConverter = new JsonConverter<>(UnavailablePeriodDto.class);
    }

    public UnavailablePeriodDto addUnavailablePeriod(UnavailablePeriodDto appointmentDto) {

        Request request = getRequestBuilder()
                .post(Objects.requireNonNull(appointmentJsonConverter.createJsonRequestBody(appointmentDto)))
                .url(url)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToObject(jsonResponse);
    }

    public List<UnavailablePeriodDto> getUnavailablePeriodsForTheDay(LocalDate date) {

        HttpUrl httpUrl = HttpUrl
                .parse(url + "/for-day")
                .newBuilder()
                .addQueryParameter("date", String.valueOf(date))
                .build();

        Request request = getRequestBuilder()
                .get()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToList(jsonResponse);
    }

    public boolean deleteUnavailablePeriodsForDay(LocalDate date) {

        HttpUrl httpUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("date", String.valueOf(date))
                .build();

        Request request = getRequestBuilder()
                .delete()
                .url(httpUrl)
                .build();

        return callRequestForCode(request) == 200;
    }
}
