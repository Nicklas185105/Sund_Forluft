package com.example.sundforluft.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sundforluft.R;
import com.example.sundforluft.StartActivity;

import carbon.view.View;

public class AdminLoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button addSchool, allSchools, removeSchool, logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        addSchool = findViewById(R.id.addSchool);
        allSchools = findViewById(R.id.schools);
        removeSchool = findViewById(R.id.removeSchool);
        logOut = findViewById(R.id.logout);

        addSchool.setOnClickListener(this);
        allSchools.setOnClickListener(this);
        removeSchool.setOnClickListener(this);
        logOut.setOnClickListener(this);
    }

    @Override
    public void onClick(android.view.View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.addSchool:
                i = new Intent(AdminLoginActivity.this, AddSchoolActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.schools:
                i = new Intent(AdminLoginActivity.this, AllSchoolsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.removeSchool:
                i = new Intent(AdminLoginActivity.this, RemoveSchoolActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.logout:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(AdminLoginActivity.this, StartActivity.class);
        intent.putExtra("animation", false);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
