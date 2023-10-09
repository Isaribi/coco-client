package com.ndurska.coco_client.database;

import androidx.annotation.NonNull;

import java.io.Serializable;

@Deprecated
public class Dog implements Serializable {
    private int id;
    private String name;
    private String pseudonym;
    private String breed;
    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumberLabel1;
    private String phoneNumberLabel2;
    private int lastPayment;
    private int expectedVisitDuration;
    private String additionalInfo;
    private String photoUUID;

    public Dog(int id, String name, String pseudonym, String breed, String phoneNumber1, String phoneNumber2, String phoneNumberLabel1, String phoneNumberLabel2, int visitDurationInMinutes, String additionalInfo, String photoUUID) {
        this.id = id;
        this.name = name;
        this.pseudonym = pseudonym;
        this.breed = breed;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.phoneNumberLabel1 = phoneNumberLabel1;
        this.phoneNumberLabel2 = phoneNumberLabel2;
        this.expectedVisitDuration = visitDurationInMinutes;
        this.additionalInfo = additionalInfo;
        this.photoUUID = photoUUID;
    }

    public Dog() {
        this.lastPayment = 0;
        this.expectedVisitDuration = 90;
        this.phoneNumberLabel1 = "Telefon:";
        this.phoneNumberLabel2 = "Telefon:";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public String getBreed() {
        return breed;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
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

    public int getLastPayment() {
        return lastPayment;
    }

    public int getExpectedVisitDuration() {
        return expectedVisitDuration;
    }

    public String getPhotoUUID() {
        return photoUUID;
    }

    public void setPhotoUUID(String photoUUID) {
        this.photoUUID = photoUUID;
    }

    public void setExpectedVisitDuration(int expectedVisitDuration) {
        this.expectedVisitDuration = expectedVisitDuration;
    }

    @NonNull
    @Override
    public String toString() {
        return "Dog{" +
                "clientId=" + id +
                ", name='" + name + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", breed='" + breed + '\'' +
                '}';
    }

    public String toStringShort() {
        return name + " " + pseudonym + " " + breed + " " + phoneNumber1 + " " + phoneNumber2;
    }

    public String getFullName() {
        if (pseudonym.isEmpty())
            return name + " " + breed;
        return name + " " + pseudonym + " " + breed;
    }
}
