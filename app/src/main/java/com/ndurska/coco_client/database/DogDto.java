package com.ndurska.coco_client.database;

import java.io.Serializable;
import java.util.List;

public class DogDto implements Serializable {
    int id;
    String name;
    String pseudonym;
    String breed;
    List<ContactNameAndPhoneNumberDTO> contactNameList;
    int expectedAppointmentDuration;
    int lastPaidAmount;
    String additionalInfo;
    String photoPath;

    public DogDto(){}

    public DogDto(Integer id, String name, String pseudonym, String breed, List<ContactNameAndPhoneNumberDTO> contactNameList, int expectedAppointmentDuration, int lastPaidAmount, String additionalInfo, String photoPath) {
        this.id = id;
        this.name = name;
        this.pseudonym = pseudonym;
        this.breed = breed;
        this.contactNameList = contactNameList;
        this.expectedAppointmentDuration = expectedAppointmentDuration;
        this.lastPaidAmount = lastPaidAmount;
        this.additionalInfo = additionalInfo;
        this.photoPath = photoPath;
    }
    public String toStringShort() {
        return name + " " + pseudonym + " " + breed + " " + contactNameList.get(0).phoneNumber + " " + contactNameList.get(0).phoneNumber;
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

    public List<ContactNameAndPhoneNumberDTO> getContactNameList() {
        return contactNameList;
    }

    public void setContactNameList(List<ContactNameAndPhoneNumberDTO> contactNameList) {
        this.contactNameList = contactNameList;
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

    public void setLastPaidAmount(int lastPaidAmount) {
        this.lastPaidAmount = lastPaidAmount;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
