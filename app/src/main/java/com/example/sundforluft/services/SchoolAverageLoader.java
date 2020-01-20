package com.example.sundforluft.services;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.cloud.ATTCommunicator;

import java.util.HashMap;
import java.util.Objects;

public class SchoolAverageLoader {
    private static SchoolAverageLoader instance;
    private SchoolAverageLoader() {}

    private boolean loaded = false;
    private SchoolModel[] schools;
    private HashMap<Integer,Double> averages;

    public static SchoolAverageLoader getInstance() {
        if (instance == null) {
            instance = new SchoolAverageLoader();
        }

        return instance;
    }

    public void loadSchoolAverages() {
        loaded = false;

        if (schools == null) {
            throw new RuntimeException("Error: Schools must be put before loading averages!");
        }

        averages = new HashMap<>();

        ATTCommunicator communicator = ATTCommunicator.getInstance();
        communicator.waitForLoad();

        for (SchoolModel school : DataAccessLayer.getInstance().getSchools()) {
            double average = communicator.getSchoolAverage(school.Id);
            averages.put(school.Id, average);
        }

        loaded = true;
    }

    public double getCachedAverageBySchoolId(int schoolId) {
        if (schools == null || averages == null || !loaded) {
            throw new RuntimeException("Error: Schools must be loaded before loading averages!");
        }

        return Objects.requireNonNull(averages.get(schoolId));
    }

    public void waitForLoad() {
        while (!isLoaded()) { /*wait for the load to occur*/ }
    }

    public void setSchools(SchoolModel[] schools) {
        this.schools = schools;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
