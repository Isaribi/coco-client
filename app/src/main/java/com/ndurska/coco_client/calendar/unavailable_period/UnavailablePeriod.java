package com.ndurska.coco_client.calendar.unavailable_period;

import java.time.LocalTime;

public class UnavailablePeriod {
    private final LocalTime start;
    private final LocalTime end;

    public UnavailablePeriod(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}
