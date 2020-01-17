package com.example.sundforluft.services;

import android.content.SharedPreferences;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.SchoolModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class CacheSchoolMananger {
    private static CacheSchoolMananger instance;

    private CacheSchoolMananger() {
        SharedPreferences.Editor editor = Globals.favoriteSchoolPreferences.edit();
        editor.remove("schools");
        editor.apply();
    }

    public static CacheSchoolMananger getInstance() {
        if (instance == null) {
            instance = new CacheSchoolMananger();
        }

        return instance;
    }

    public SchoolModel[] getFavoriteSchools() {
        //SharedPreferences.Editor editor = Globals.favoriteSchoolPreferences.edit();
        //editor.remove("schools");
        //editor.apply();

        ArrayList<SchoolModel> models = new ArrayList<>();
        for (Map.Entry<String,?> entry : Globals.favoriteSchoolPreferences.getAll().entrySet()) {
            Integer id = Integer.parseInt(entry.getKey());

            if (id != 0) {
                SchoolModel model = DataAccessLayer.getInstance().getSchoolById(id);
                if (model != null) {
                    models.add(model);
                } else {
                    /*TODO: Add toast that this school was removed*/
                    CacheSchoolMananger.getInstance().removeFavoriteSchool(id);
                }
            }
        }

        SchoolModel[] array = new SchoolModel[models.size()];
        models.toArray(array);
        return array;
    }

    public void addFavoriteSchool(int schoolId) {
        SharedPreferences.Editor editor = Globals.favoriteSchoolPreferences.edit();
        editor.putInt("" + schoolId, schoolId);
        editor.apply();
    }

    public void removeFavoriteSchool(int schoolId) {
        SharedPreferences.Editor editor = Globals.favoriteSchoolPreferences.edit();
        editor.remove("" + schoolId);
        editor.apply();
    }
}
