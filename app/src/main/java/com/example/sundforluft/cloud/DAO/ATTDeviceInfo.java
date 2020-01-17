package com.example.sundforluft.cloud.DAO;

import java.util.Arrays;

public class ATTDeviceInfo {
    private final ATTDeviceInfoMeasurement[] measurements;

    public ATTDeviceInfo(ATTDeviceInfoMeasurement[] measurements) {
        this.measurements = measurements;
    }

    public ATTDeviceInfoMeasurement[] getMeasurements() {
        return measurements;
    }

    public double getAverageQuality() {
        double averageSum = Arrays.stream(measurements).mapToDouble(c-> c.average).sum();
        return averageSum / measurements.length;
    }
}
