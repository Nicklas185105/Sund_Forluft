package com.example.sundforluft.DAO;

public class ClassroomModel {
    public final int id;
    public final String accessToken;
    public final String deviceId;
    public final String name;


    public ClassroomModel(int id, String accessToken, String deviceId, String name) {
        this.id = id;
        this.accessToken = accessToken;
        this.deviceId = deviceId;
        this.name = name;
    }
}
