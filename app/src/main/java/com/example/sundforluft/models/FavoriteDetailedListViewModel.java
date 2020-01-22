package com.example.sundforluft.models;

public class FavoriteDetailedListViewModel {

    private String name;
    private double airQuality;
    private int schoolId;

    public FavoriteDetailedListViewModel(String name, double airQuality, int schoolId) {
        this.name = name;
        this.airQuality = airQuality;
        this.schoolId = schoolId;
    }



    public String getName() {
        return name;
    }

    public double getAirQuality() {
        return airQuality;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setDeviceInfo() {
    }
}
