package com.ndurska.coco_client.database.dto;

import java.io.Serializable;

public class DogDto implements Serializable {
    int id;
    String name;
    String pseudonym;
    String breed;
    String phoneNumber1;
    String phoneNumber2;
    String phoneNumberLabel1;
    String phoneNumberLabel2;
    int expectedAppointmentDuration;
    int lastPaidAmount;
    String additionalInfo;
    byte[] photo;

    public DogDto() {
    }

    public DogDto(Integer id, String name, String pseudonym, String breed, String phoneNumber1, String phoneNumber2, String phoneNumberLabel1, String phoneNumberLabel2, int expectedAppointmentDuration, int lastPaidAmount, String additionalInfo, byte[] photo) {
        this.id = id;
        this.name = name;
        this.pseudonym = pseudonym;
        this.breed = breed;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.phoneNumberLabel1 = phoneNumberLabel1;
        this.phoneNumberLabel2 = phoneNumberLabel2;
        this.expectedAppointmentDuration = expectedAppointmentDuration;
        this.lastPaidAmount = lastPaidAmount;
        this.additionalInfo = additionalInfo;
        this.photo = photo;
    }

    public String toStringShort() {
        return name + " " + pseudonym + " " + breed + " " + phoneNumber1 + " " + phoneNumber2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getPhoneNumberLabel1() {
        return phoneNumberLabel1;
    }

    public void setPhoneNumberLabel1(String phoneNumberLabel1) {
        this.phoneNumberLabel1 = phoneNumberLabel1;
    }

    public String getPhoneNumberLabel2() {
        return phoneNumberLabel2;
    }

    public void setPhoneNumberLabel2(String phoneNumberLabel2) {
        this.phoneNumberLabel2 = phoneNumberLabel2;
    }

    public int getExpectedAppointmentDuration() {
        return expectedAppointmentDuration;
    }

    public void setExpectedAppointmentDuration(int expectedAppointmentDuration) {
        this.expectedAppointmentDuration = expectedAppointmentDuration;
    }

    public int getLastPaidAmount() {
        return lastPaidAmount;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String clientFullName() {
        return name + " " + pseudonym + " " + breed;
    }
}
