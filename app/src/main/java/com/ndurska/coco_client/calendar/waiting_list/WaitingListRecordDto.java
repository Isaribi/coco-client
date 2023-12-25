package com.ndurska.coco_client.calendar.waiting_list;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ndurska.coco_client.database.dto.DogDto;

import java.io.Serializable;
import java.time.LocalDate;

public class WaitingListRecordDto implements Serializable {
    private int ID;
    @JsonAlias("dogTwoContactsDTO")
    private DogDto dogDto;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String notes;

    public WaitingListRecordDto(int ID, DogDto dogDto, LocalDate dateStart, LocalDate dateEnd, String notes) {
        this.ID = ID;
        this.dogDto = dogDto;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.notes = notes;
    }

    public WaitingListRecordDto(DogDto dogDto, LocalDate dateStart, LocalDate dateEnd, String notes) {
        this.dogDto = dogDto;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.notes = notes;
    }

    public WaitingListRecordDto(){

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public DogDto getDogDto() {
        return dogDto;
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
