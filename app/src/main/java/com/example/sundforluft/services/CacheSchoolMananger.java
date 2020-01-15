package com.example.sundforluft.services;

import android.content.SharedPreferences;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.SchoolModel;

import java.util.ArrayList;
import java.util.HashSet;

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
        HashSet<String> favoriteSchools = (HashSet<String>)Globals.favoriteSchoolPreferences.getStringSet("schools", new HashSet<>());

        int[] favSchoolIds = favoriteSchools.stream()
                .mapToInt(entry -> Integer.parseInt(entry))
                .toArray();

        ArrayList<SchoolModel> models = new ArrayList<>();
        for (int i = 0; i < favSchoolIds.length; i++) {
            if (favSchoolIds[i] != 0) {
                SchoolModel model = DataAccessLayer.getInstance().getSchoolById(favSchoolIds[i]);
                if (model != null) {
                    models.add(model);
                } else {
                    /*TODO: Add toast that this school was removed*/
                    CacheSchoolMananger.getInstance().removeFavoriteSchool(favSchoolIds[i]);
                }
            }
        }

        SchoolModel[] array = new SchoolModel[models.size()];
        models.toArray(array);
        return array;
    }

    public void addFavoriteSchool(int schoolId) {
        HashSet<String> favoriteSchools = (HashSet<String>)Globals.favoriteSchoolPreferences.getStringSet("schools", new HashSet<>());
        SharedPreferences.Editor editor = Globals.favoriteSchoolPreferences.edit();
        favoriteSchools.add("" + schoolId);
        editor.putStringSet("schools", favoriteSchools);
        editor.apply();
    }
    public void removeFavoriteSchool(int schoolId) {
        HashSet<String> favoriteSchools = (HashSet<String>)Globals.favoriteSchoolPreferences.getStringSet("schools", new HashSet<>());
        SharedPreferences.Editor editor = Globals.favoriteSchoolPreferences.edit();

        if (favoriteSchools.size()==1) {
            editor.remove("schools");
        } else {
            favoriteSchools.remove("" + schoolId);
            editor.putStringSet("schools", favoriteSchools);
            editor.apply();
        }
    }
}
