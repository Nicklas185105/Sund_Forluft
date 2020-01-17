package com.example.sundforluft.models;

import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfo;

public class FavoriteDetailedListViewModel {

    private String name;
    private double airQuality;
    private int schoolId;
    private final Fragment fragment;

    private ATTDeviceInfo deviceInfo;

    public FavoriteDetailedListViewModel(Fragment fragment, String name, double airQuality, int schoolId) {
        this.fragment = fragment;
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

    public void setAirQuality(double airQuality) {
        this.airQuality = airQuality;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getAirQualityString() {
        switch (getAirQualityType()) {
            case 1: return fragment.getString(R.string.bad_air_quality);
            case 2: return fragment.getString(R.string.medium_air_quality);
            case 3: return fragment.getString(R.string.good_air_quality);
            default: return fragment.getString(R.string.best_air_quality);
        }
    }

    public int getAirQualityType() {
        final int BAD_AIR_QUALITY = 10;
        final int MEDIUM_AIR_QUALITY = 20;
        final int GOOD_AIR_QUALITY = 30;

        if (airQuality < BAD_AIR_QUALITY) { return 1; }
        if (airQuality < MEDIUM_AIR_QUALITY) { return 2; }
        if (airQuality < GOOD_AIR_QUALITY) { return 3; }
        return 4;
    }

    public ATTDeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(ATTDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
