package com.example.sundforluft.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.DAL.SchoolsLocator;
import com.example.sundforluft.R;
import com.example.sundforluft.StartActivity;
import com.example.sundforluft.services.MD5Converter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import carbon.view.View;

public class AddSchoolActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    Button add;
    EditText username, password, school;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    long schoolCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_school);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*TODO: Text in strings.xml*/
        getSupportActionBar().setTitle("Tilføj Skole");

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        add = findViewById(R.id.button);
        add.setOnClickListener(this);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        school = findViewById(R.id.schoolEditText);

        schoolCount = SchoolsLocator.getInstance().getMaxId();
    }

    @Override
    public void onClick(android.view.View v) {
        if (v == add){
            if (!username.getText().toString().equals("") && !password.getText().toString().equals("") && !school.getText().toString().equals("")){
                // Tilføj username til users og tilføj password (Kryptering mangler)
                myRef = database.getReference("users/" + username.getText().toString() + "/password");
                myRef.setValue(MD5Converter.md5(password.getText().toString()));

                // Tjek hvad næste skole id skal være og tilføj skole til "schools"myRef = database.getReference("schools/" + schoolCount);
                myRef.setValue(school.getText().toString());

                // Tag id og tilføj det til den nye user
                myRef = database.getReference("users/" + username.getText().toString() + "/schoolId");
                myRef.setValue(schoolCount);

                // Hvis brugeren skal tilføje flere brugere efter hinanden opdatere vi schoolCount
                schoolCount = SchoolsLocator.getInstance().getMaxId();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
