package com.example.sundforluft.services;

import android.content.SharedPreferences;

import com.example.sundforluft.DAO.SchoolModel;

public class Globals {
    public static boolean isTeacher = false;
    public static boolean isAdmin = false;

    public static boolean hasTeacherRights() { return isTeacher || isAdmin; }

    public static SchoolModel school = null;
    public static SharedPreferences favoriteSchoolPreferences = null;
}
