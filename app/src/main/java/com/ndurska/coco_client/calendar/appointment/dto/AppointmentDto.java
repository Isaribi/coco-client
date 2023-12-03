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
    private boolean attendance;
    private int amountPaid;
    private String notes;

    public AppointmentDto(long id, LocalDate date, LocalTime time, DogDto dogDto, boolean attendance, int amountPaid, String notes) {
        this.id = id;
        this.dogDto = dogDto;
        this.date = date;
        this.time = time;
        this.attendance = attendance;
        this.amountPaid = amountPaid;
        this.notes = notes;
    }

    public AppointmentDto() {
    }

    public AppointmentDto(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
        this.attendance = true;
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

    public boolean getAttendance() {
        return attendance;
    }

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
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


    @NonNull
    @Override
    public String toString() {
        return "AppointmentDto{" +
                "ID=" + id +
                ", clientID=" + dogDto.getId() +
                ", date=" + date +
                ", time=" + time +
                ", attendance=" + attendance +
                ", amountPaid=" + amountPaid +
                '}';
    }
}
