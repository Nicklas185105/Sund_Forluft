package com.example.sundforluft.cloud.DAO;

import java.util.Date;

public class ATTDeviceInfoMeasurement {
    public final double CO2;
    public final Date Time;

    public ATTDeviceInfoMeasurement(double CO2, Date Time) {
        this.CO2 = CO2;
        this.Time = Time;
    }
}
