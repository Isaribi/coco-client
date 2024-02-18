package com.ndurska.coco_client.calendar.appointment.dto;


import java.io.Serializable;

public class ProvidedService implements Serializable {
    private Integer id;
    private String name;
    private int priceMin;
    private int priceMax;
    private String additionalInfo;

    public ProvidedService() {
    }

    public ProvidedService(Integer id, String name, int priceMin, int priceMax, String additionalInfo) {
        this.id = id;
        this.name = name;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.additionalInfo = additionalInfo;
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

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
