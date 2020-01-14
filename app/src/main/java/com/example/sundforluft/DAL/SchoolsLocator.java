package com.example.sundforluft.DAL;

import com.example.sundforluft.DAO.SchoolModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SchoolsLocator {
    private static SchoolsLocator instance = null;
    private ArrayList<SchoolModel> schools;
    private boolean loaded = false;

    private SchoolsLocator(){
        schools = new ArrayList<>();
    }

    public static SchoolsLocator getInstance() {
        if (instance == null) {
            instance = new SchoolsLocator();
            instance.reloadFromInternet();
        }
        return instance;
    }

    public int getMaxId() {
        int id = 1;
        for (SchoolModel school : schools) {
            if (school.Id > id) {
                id = school.Id + 1;
            }
        }
        return id;
    }

    public void removeSchool(SchoolModel model) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("schools/" + model.Id);
        myRef.removeValue();
        schools.remove(model);
    }

    public void reloadFromInternet() {
        loaded = false;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("schools");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                instance.schools = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    int schoolId = Integer.parseInt( child.getKey() );
                    String schoolName = child.getValue(String.class);

                    instance.schools.add(new SchoolModel(schoolId, schoolName));
                }

                instance.loaded = true;
            }

            @Override
            public void onCancelled(DatabaseError error) {  }
        });
    }



    public boolean isLoaded() {
        return loaded;
    }
    public SchoolModel getSchoolById(int schoolId) {
        for (SchoolModel school : schools) {
            if (school.Id == schoolId) {
                return school;
            }
        }

        return null;
    }
    public SchoolModel getSchoolByName(String name) {
        for (SchoolModel school : schools) {
            if (school.Name.equals(name)) {
                return school;
            }
        }

        return null;
    }
    public SchoolModel[] getSchools() {
        SchoolModel[] array = new SchoolModel[schools.size()];
        schools.toArray(array);
        return array;
    }
}
