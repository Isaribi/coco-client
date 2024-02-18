package com.ndurska.coco_client.calendar.appointment.dto;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ndurska.coco_client.database.dto.DogDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


public class AppointmentDto implements Serializable {
    private long id;
    @JsonAlias("dogTwoContactsDTO")
    private DogDto dogDto;
    private LocalDate date;
    private LocalTime time;
    private boolean absence;
    private int amountPaid;
    private String notes;
    private ProvidedService providedService;

    public AppointmentDto(long id, LocalDate date, LocalTime time, DogDto dogDto, boolean absence, int amountPaid, String notes,ProvidedService providedService) {
        this.id = id;
        this.dogDto = dogDto;
        this.date = date;
        this.time = time;
        this.absence = absence;
        this.amountPaid = amountPaid;
        this.notes = notes;
        this.providedService = providedService;
    }

    public AppointmentDto() {
    }

    public AppointmentDto(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public DogDto getDogDto() {
        return dogDto;
    }

    public void setDogDto(DogDto dogDto) {
        this.dogDto = dogDto;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean getAbsence() {
        return absence;
    }

    public void setAbsence(boolean absence) {
        this.absence = absence;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ProvidedService getProvidedService() {
        return providedService;
    }

    public void setProvidedService(ProvidedService providedService) {
        this.providedService = providedService;
    }

    @NonNull
    @Override
    public String toString() {
        return "AppointmentDto{" +
                "ID=" + id +
                ", clientID=" + dogDto.getId() +
                ", date=" + date +
                ", time=" + time +
                ", attendance=" + absence +
                ", amountPaid=" + amountPaid +
                ", providedService=" + providedService +
                '}';
    }
}
