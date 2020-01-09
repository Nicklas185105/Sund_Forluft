package com.example.sundforluft.DAL;

import com.example.sundforluft.DAO.SchoolModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchoolsLocator {
    private static SchoolsLocator instance = null;
    private SchoolModel[] schools;
    private boolean loaded = false;

    private SchoolsLocator(){}

    public static SchoolsLocator getInstance() {
        if (instance == null) {
            instance = new SchoolsLocator();

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference myRef = database.getReference("schools");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    instance.schools = new SchoolModel[(int)dataSnapshot.getChildrenCount()];

                    int currentSchoolIndex = 0;
                    for (DataSnapshot child : dataSnapshot.getChildren())
                    {
                        int schoolId = Integer.parseInt( child.getKey() );
                        String schoolName = child.getValue(String.class);

                        instance.schools[currentSchoolIndex++] = new SchoolModel(schoolId, schoolName);
                    }

                    instance.loaded = true;
                }

                @Override
                public void onCancelled(DatabaseError error) {  }
            });


        }
        return instance;
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
        return schools;
    }
}
