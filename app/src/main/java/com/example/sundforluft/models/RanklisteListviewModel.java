package com.example.sundforluft.models;

import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;

public class RanklisteListviewModel {

    private String name;
    private double getAverageQuality;
    private final Fragment fragment;

    public RanklisteListviewModel(Fragment fragment, String name, double getAverageQuality) {
        this.fragment = fragment;
        this.name = name;
        this.getAverageQuality = getAverageQuality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentAir() {
        return getAverageQuality;
    }

    public void setCurrentAir(int currentAir) {
        this.getAverageQuality = currentAir;
    }

    public String getAirQualityString() {
        switch (getAirQuality()) {
            case 1: return fragment.getString(R.string.bad_air_quality);
            case 2: return fragment.getString(R.string.medium_air_quality);
            case 3: return fragment.getString(R.string.good_air_quality);
            default: return fragment.getString(R.string.best_air_quality);
        }
    }

    public int getAirQuality() {
        final int BAD_AIR_QUALITY = 10;
        final int MEDIUM_AIR_QUALITY = 20;
        final int GOOD_AIR_QUALITY = 30;

        if (getAverageQuality < BAD_AIR_QUALITY) { return 1; }
        if (getAverageQuality < MEDIUM_AIR_QUALITY) { return 2; }
        if (getAverageQuality < GOOD_AIR_QUALITY) { return 3; }
        return 4;
    }
}
