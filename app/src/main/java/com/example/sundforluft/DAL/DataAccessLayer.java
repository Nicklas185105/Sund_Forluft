package com.example.sundforluft.DAL;

import androidx.annotation.NonNull;

import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.DAO.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataAccessLayer {
    private static DataAccessLayer instance = null;
    private ArrayList<SchoolModel> schools;
    private boolean loaded = false;
    private ArrayList<UserModel> users;
    private ArrayList<ClassroomModel> classrooms;

    private DataAccessLayer(){
        schools = new ArrayList<>();
        users = new ArrayList<>();
        classrooms = new ArrayList<>();
    }

    public static DataAccessLayer getInstance() {
        if (instance == null) {
            instance = new DataAccessLayer();
            instance.reloadFromInternet();
        }
        return instance;
    }

    public int getMaxId() {
        int id = 1;
        for (SchoolModel school : schools) {
            if (school.Id >= id) {
                id = school.Id + 1;
            }
        }
        return id;
    }

    public void addSchool(String schoolName, UserModel user) {
        int newId = getMaxId();

        // Cache School
        SchoolModel school = new SchoolModel(newId, schoolName);
        schools.add(school);

        // Cache user
        user.setSchoolId(school.Id);
        users.add(user);

        // Tilføj username til users og tilføj password
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + user.username + "/password");
        myRef.setValue(user.hashedPassword);

        // Tjek hvad næste skole id skal være og tilføj skole til "schools"
        myRef = database.getReference("schools/" + newId);
        myRef.setValue(schoolName);

        // Tag id og tilføj det til den nye user
        myRef = database.getReference("users/" + user.username + "/schoolId");
        myRef.setValue(newId);
    }

    public void addClassroom(int schoolId, String accessToken, String deviceId, String name){
        //Cache classroom
        ClassroomModel classroom = new ClassroomModel(schoolId, accessToken, deviceId, name);
        classrooms.add(classroom);

        //Db insert
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("classrooms/"+classroom.id+"/"+deviceId+"/accessToken");
        reference.setValue(classroom.accessToken);

        reference = database.getReference("classrooms/"+classroom.id+"/"+deviceId+"/name");
        reference.setValue(classroom.name);

    }

    public void removeSchool(SchoolModel model) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("schools/" + model.Id);
        myRef.removeValue();

        UserModel user = getUserBySchool(model);
        myRef = database.getReference("users/" + user.username);
        myRef.removeValue();

        schools.remove(model);
    }

    public void removeClassroom(ClassroomModel model){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("classrooms/"+model.id+"/"+model.deviceId);
        ref.removeValue();
        boolean idsjd = classrooms.remove(model);
    }


    public void reloadFromInternet() {
        loaded = false;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference schoolRef = database.getReference("schools");
        schoolRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                instance.schools = new ArrayList<>();
                instance.users = new ArrayList<>();

                // Retrieve Schools
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    int schoolId = Integer.parseInt( child.getKey() );
                    String schoolName = child.getValue(String.class);

                    instance.schools.add(new SchoolModel(schoolId, schoolName));
                }

                // Retrieve Users
                DatabaseReference userRefs = database.getReference("users");
                userRefs.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren())
                        {
                            String username;
                            String passwordHashed = "";
                            int schoolId = 0;

                            username = child.getKey();
                            for (DataSnapshot member : child.getChildren()) {
                                switch (member.getKey()) {
                                    case "password":
                                        passwordHashed = member.getValue(String.class);
                                        break;

                                    case "schoolId":
                                        schoolId = member.getValue(Integer.class);
                                        break;
                                }
                            }

                            instance.users.add(new UserModel(username, passwordHashed, schoolId));
                        }

                        instance.loaded = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {  }
        });
        DatabaseReference classroomRef = database.getReference("classrooms");
        classroomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                instance.classrooms = new ArrayList<>();

                
                //Retrieve classrooms
                for (DataSnapshot classroomParent : dataSnapshot.getChildren()) {
                    //Id from this particular record
                    int id = Integer.parseInt(classroomParent.getKey());
                    //Child records
                    for (DataSnapshot deviceIdChild : classroomParent.getChildren()){
                        String deviceId = deviceIdChild.getKey();
                        String accessToken = deviceIdChild.child("accessToken").getValue(String.class);
                        String name = deviceIdChild.child("name").getValue(String.class);
                        ClassroomModel classroom = new ClassroomModel(id, accessToken, deviceId, name);
                        classrooms.add(classroom);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
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

    public ArrayList<ClassroomModel> getClassroomsBySchoolId(int schoolId){
        ArrayList<ClassroomModel> classroomModels = new ArrayList();
        classrooms.forEach(c -> { if (c.id == schoolId) classroomModels.add(c); });
        return classroomModels;
    }

    public ClassroomModel getClassroomByDeviceId(String deviceId){
        for ( ClassroomModel model : classrooms ){
            if (model.deviceId.equals(deviceId)){
                return model;
            }
        }
        return null;
    }

    public ClassroomModel getClassroomBySchooldAndName(int schooldId, String name){
        for( ClassroomModel model : classrooms ){
            if (model.id == schooldId && model.name.equals(name)){
                return model;
            }
        }
        return null;
    }

    public UserModel getUserBySchool(SchoolModel model) {
        for (UserModel user : users) {
            if (model.Id == user.getSchoolId()) {
                return user;
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
