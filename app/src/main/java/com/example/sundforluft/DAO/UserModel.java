package com.example.sundforluft.DAO;

import com.example.sundforluft.services.MD5Converter;

public class UserModel {
    public final String hashedPassword;
    public final String username;

    private int schoolId;

    public UserModel(String username, String password) {
        this.username = username;
        this.hashedPassword = MD5Converter.md5(password);
    }

    public UserModel(String username, String hashedPassword, int schoolId) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.schoolId = schoolId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}
