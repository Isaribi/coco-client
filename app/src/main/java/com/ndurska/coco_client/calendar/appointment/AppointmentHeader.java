package com.ndurska.coco_client.calendar.appointment;

import com.ndurska.coco_client.database.dto.DogDto;

/**
 * Works like a Pair<String,String> but can be stored in Lists
 * Stores full name of a client and note for an appointment
 */
public class AppointmentHeader {
    private final DogDto dog;
    private final String note;

    public AppointmentHeader(DogDto dog, String note) {
        this.dog = dog;
        this.note = note;
    }

    public String getClientName() {
        return dog.clientFullName();
    }

    public DogDto getDog() {
        return dog;
    }

    public String getNote() {
        return note;
    }

    public boolean hasNote() {
        return note != null && note.trim().length() > 0;
    }

}
