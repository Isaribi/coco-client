package com.ndurska.coco_client.calendar.appointment.web;

import android.content.Context;

import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.shared.AbstractRequestDispatcher;
import com.ndurska.coco_client.shared.JsonConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class AppointmentsRequestDispatcher extends AbstractRequestDispatcher {
    private final String url = "http://10.0.2.2:8080/api/appointments";
    private final JsonConverter<AppointmentDto> appointmentJsonConverter;

    public AppointmentsRequestDispatcher(Context context) {
        super(context);
        appointmentJsonConverter = new JsonConverter<>(AppointmentDto.class);
    }

    public AppointmentDto addAppointment(AppointmentDto appointmentDto) {

        Request request = getRequestBuilder()
                .post(Objects.requireNonNull(appointmentJsonConverter.createJsonRequestBody(appointmentDto)))
                .url(url)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToObject(jsonResponse);
    }

    public List<AppointmentDto> getAppointmentsForTheDay(LocalDate date) {

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

    public boolean deleteAppointment(long id) {
        HttpUrl httpUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(id))
                .build();

        Request request = getRequestBuilder()
                .delete()
                .url(httpUrl)
                .build();

        return callRequestForCode(request) == 200;
    }

    public AppointmentDto getAppointment(long appointmentID) {
        HttpUrl httpUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(appointmentID))
                .build();
        Request request = getRequestBuilder()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToObject(jsonResponse);
    }

    public AppointmentDto editAppointment(AppointmentDto appointmentDto) {
        HttpUrl httpUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(appointmentDto.getId()))
                .build();
        Request request = getRequestBuilder()
                .url(httpUrl)
                .put(Objects.requireNonNull(appointmentJsonConverter.createJsonRequestBody(appointmentDto)))
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToObject(jsonResponse);
    }

    public List<AppointmentDto> getAppointmentsForDog(Integer id) {
        HttpUrl httpUrl = HttpUrl
                .parse(url + "/for-dog")
                .newBuilder()
                .addQueryParameter("dogId", String.valueOf(id))
                .build();

        Request request = getRequestBuilder()
                .get()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToList(jsonResponse);
    }
    public int getLastPaidAmount(int dogId) {
        HttpUrl httpUrl = HttpUrl
                .parse(url+"/last-paid")
                .newBuilder()
                .addQueryParameter("dogId", String.valueOf(dogId))
                .build();
        Request request = getRequestBuilder()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return Integer.parseInt(jsonResponse);
    }
}
