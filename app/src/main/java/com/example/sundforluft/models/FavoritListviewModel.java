package com.example.sundforluft.models;

import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;

public class FavoritListviewModel {
    private String name;
    private int currentAir;
    private final Fragment fragment;

    public FavoritListviewModel(Fragment fragment, String name, int currentAir) {
        this.fragment = fragment;
        this.name = name;
        this.currentAir = currentAir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentAir() {
        return currentAir;
    }

    public void setCurrentAir(int currentAir) {
        this.currentAir = currentAir;
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

        if (currentAir < BAD_AIR_QUALITY) { return 1; }
        if (currentAir < MEDIUM_AIR_QUALITY) { return 2; }
        if (currentAir < GOOD_AIR_QUALITY) { return 3; }
        return 4;
    }



}