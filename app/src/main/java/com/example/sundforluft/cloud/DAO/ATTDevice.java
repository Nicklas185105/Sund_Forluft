package com.example.sundforluft.cloud.DAO;

public class ATTDevice {
    public final String deviceId;
    public final String name;
    public final String CO2AssetId;

    public ATTDevice(String deviceId, String name, String CO2AssetId) {
        this.deviceId = deviceId;
        this.name = name;
        this.CO2AssetId = CO2AssetId;
    }
}
