package com.ndurska.coco_client.calendar.unavailable_period;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class UnavailablePeriodDto implements Serializable {
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    public UnavailablePeriodDto() {
    }

    public UnavailablePeriodDto(LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }
    public LocalDate getDate(){return date;}

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }
}
