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

    private int getCurrentAir() {
        return currentAir;
    }

    public String getAirQualityString() {
        switch (getAirQuality()) {
            case 1: return String.format(fragment.getString(R.string.bad_air_quality), getCurrentAir());
            case 2: return String.format(fragment.getString(R.string.medium_air_quality), getCurrentAir());
            case 3: return String.format(fragment.getString(R.string.good_air_quality), getCurrentAir());
            default: return String.format(fragment.getString(R.string.best_air_quality), getCurrentAir());
        }
    }

    public int getAirQuality() {
        final int BAD_AIR_QUALITY = 800;
        final int MEDIUM_AIR_QUALITY = 600;
        final int GOOD_AIR_QUALITY = 450;

        if (currentAir >= BAD_AIR_QUALITY) { return 1; }
        if (currentAir >= MEDIUM_AIR_QUALITY) { return 2; }
        if (currentAir >= GOOD_AIR_QUALITY) { return 3; }
        return 4;
    }



}