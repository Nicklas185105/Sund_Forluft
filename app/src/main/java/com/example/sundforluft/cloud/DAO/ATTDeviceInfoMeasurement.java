package com.example.sundforluft.cloud.DAO;

import java.util.Date;

public class ATTDeviceInfoMeasurement {
    public final double average;
    public final double minimum;
    public final double maximum;
    public final Date time;

    public ATTDeviceInfoMeasurement(double average, double min, double max, Date time) {
        this.average = average;
        this.minimum = min;
        this.maximum = max;
        this.time = time;
    }
}
