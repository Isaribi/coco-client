package com.ndurska.coco_client.calendar.appointment;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


public class Appointment implements Serializable {
    private int ID;
    private int clientID;
    private LocalDate date;
    private LocalTime time;
    private boolean attendance;
    private int amountPaid;
    private String notes;

    public Appointment(int ID, LocalDate date, LocalTime time, int clientID, boolean attendance, int amountPaid, String notes) {
        this.ID = ID;
        this.clientID = clientID;
        this.date = date;
        this.time = time;
        this.attendance = attendance;
        this.amountPaid = amountPaid;
        this.notes = notes;
    }

    public Appointment() {
    }

    public Appointment(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
        this.attendance = true;
    }

    public int getID() {
        return ID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
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
        return "Appointment{" +
                "ID=" + ID +
                ", clientID=" + clientID +
                ", date=" + date +
                ", time=" + time +
                ", attendance=" + attendance +
                ", amountPaid=" + amountPaid +
                '}';
    }
}
