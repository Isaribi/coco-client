package com.ndurska.coco_client.calendar.monthlysummary;

import com.ndurska.coco_client.shared.AbstractRequestDispatcher;
import com.ndurska.coco_client.shared.JsonConverter;

import java.time.LocalDate;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class MonthlySummaryRequestDispatcher extends AbstractRequestDispatcher {

    private final String url = "http://10.0.2.2:8080/api/monthly-summary";

    private final JsonConverter<MonthSummaryDTO> appointmentJsonConverter;

    public MonthlySummaryRequestDispatcher() {
        appointmentJsonConverter = new JsonConverter<>(MonthSummaryDTO.class);
    }

    public MonthSummaryDTO getMonthlySummary(LocalDate date) {
        HttpUrl httpUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("date", String.valueOf(date))
                .build();

        Request request = new Request.Builder()
                .get()
                .url(httpUrl)
                .build();

        String jsonResponse = callRequestForBody(request);

        return appointmentJsonConverter.convertToObject(jsonResponse);
    }
}
