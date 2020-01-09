package com.example.sundforluft.services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.sundforluft.DAO.SchoolModel;

import java.util.HashMap;
import java.util.HashSet;

import kotlin.NotImplementedError;

public class CacheSchoolMananger {
    private static CacheSchoolMananger instance;


    private CacheSchoolMananger() {}

    public static CacheSchoolMananger getInstance() {
        if (instance == null) {
            instance = new CacheSchoolMananger();


        }

        return instance;
    }

    public SchoolModel[] getFavoriteSchools() {
        /*
        HashSet<String> favoriteSchools = (HashSet<String>)Globals.favoriteSchoolPreferences.getStringSet("schools", new HashSet<>());

        int[] favSchoolIds = favoriteSchools.stream()
                .mapToInt(entry -> Integer.parseInt(entry))
                .toArray();
        SchoolModel[]

        for (int favSchoolId : favSchoolIds) {

        }
        */


        throw new NotImplementedError();
    }

    public void addFavoriteSchool(int schoolId) {
        HashSet<String> favoriteSchools = (HashSet<String>)Globals.favoriteSchoolPreferences.getStringSet("schools", new HashSet<>());
        SharedPreferences.Editor editor = Globals.favoriteSchoolPreferences.edit();
        favoriteSchools.add("" + schoolId);
        editor.putStringSet("schools", favoriteSchools);
    }
    public void removeFavoriteSchool(int schoolId) {
        HashSet<String> favoriteSchools = (HashSet<String>)Globals.favoriteSchoolPreferences.getStringSet("schools", new HashSet<>());
        SharedPreferences.Editor editor = Globals.favoriteSchoolPreferences.edit();
        favoriteSchools.remove("" + schoolId);
        editor.putStringSet("schools", favoriteSchools);

    }
}
