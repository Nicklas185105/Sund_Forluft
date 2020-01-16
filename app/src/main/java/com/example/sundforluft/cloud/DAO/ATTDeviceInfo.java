package com.example.sundforluft.cloud.DAO;

public class ATTDeviceInfo {
    private final ATTDeviceInfoMeasurement[] measurements;

    public ATTDeviceInfo(ATTDeviceInfoMeasurement[] measurements) {
        this.measurements = measurements;
    }

    public ATTDeviceInfoMeasurement[] getMeasurements() {
        return measurements;
    }
}
