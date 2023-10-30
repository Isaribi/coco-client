package com.ndurska.coco_client.calendar.waiting_list;

import java.time.LocalDate;

public class WaitingListRecord {
    private int ID;
    private int clientID;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String notes;

    public WaitingListRecord(int ID, int clientID, LocalDate dateStart, LocalDate dateEnd, String notes) {
        this.ID = ID;
        this.clientID = clientID;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.notes = notes;
    }

    public WaitingListRecord(int clientID, LocalDate dateStart, LocalDate dateEnd, String notes) {
        this.clientID = clientID;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.notes = notes;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
