package com.ndurska.coco_client.calendar.appointment.web;

import android.content.Context;

import com.ndurska.coco_client.calendar.appointment.dto.ProvidedService;
import com.ndurska.coco_client.shared.AbstractRequestDispatcher;
import com.ndurska.coco_client.shared.JsonConverter;

import java.util.Objects;

import okhttp3.Request;

public class ProvidedServicesRequestDispatcher extends AbstractRequestDispatcher {

    private final String url = "http://10.0.2.2:8080/api/appointments/provided-services";
    private final JsonConverter<ProvidedService> appointmentJsonConverter;

    public ProvidedServicesRequestDispatcher(Context context) {
        super(context);
        appointmentJsonConverter = new JsonConverter<>(ProvidedService.class);
    }


    public Object getProvidedServices() {

        Request request = getRequestBuilder()
                .get()
                .url(url)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToList(jsonResponse);
    }

    public ProvidedService addProvidedService(ProvidedService providedService) {

        Request request = getRequestBuilder()
                .post(Objects.requireNonNull(appointmentJsonConverter.createJsonRequestBody(providedService)))
                .url(url)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToObject(jsonResponse);
    }
}
