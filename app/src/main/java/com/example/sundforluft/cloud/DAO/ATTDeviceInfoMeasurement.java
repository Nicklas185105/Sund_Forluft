package com.example.sundforluft.cloud.DAO;

import java.util.Date;

public class ATTDeviceInfoMeasurement implements Comparable<ATTDeviceInfoMeasurement> {
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

    @Override
    public int compareTo(ATTDeviceInfoMeasurement o) {
        return Long.compare(this.time.getTime(), o.time.getTime());
    }
}
