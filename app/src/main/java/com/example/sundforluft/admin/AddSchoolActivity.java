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
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.DAO.UserModel;
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

    }

    @Override
    public void onClick(android.view.View v) {
        if (v == add){
            if (!username.getText().toString().equals("") && !password.getText().toString().equals("") && !school.getText().toString().equals("")){
                SchoolsLocator.getInstance().addSchool(
                        school.getText().toString(),
                        new UserModel(username.getText().toString(), password.getText().toString())
                );
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
