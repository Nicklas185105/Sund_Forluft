package com.example.sundforluft.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.QRScanner;
import com.example.sundforluft.R;
import com.example.sundforluft.StartActivity;
import com.example.sundforluft.services.Globals;

public class TeacherMainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView text;
    Button addCloud;
    Button allClouds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setTitle(R.string.welcome);

            // Arrow Click
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        text = findViewById(R.id.welcome);
        Intent intent = getIntent();

        text.setText(R.string.welcome2);
        String welcome = text.getText() + " " + intent.getStringExtra("name");
        text.setText(welcome);

        addCloud = findViewById(R.id.addCloud);
        allClouds = findViewById(R.id.allClouds);

        addCloud.setOnClickListener(this);
        allClouds.setOnClickListener(this);
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
    public void onClick(View v) {
        Intent i;
        switch(v.getId()){
            case R.id.addCloud:
                i = new Intent(TeacherMainActivity.this, QRScanner.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.allClouds:
                i = new Intent(this, CloudsOverviewActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Globals.isTeacher = false; // Revoke teacher rights.
        Intent intent = new Intent(TeacherMainActivity.this, StartActivity.class);
        intent.putExtra("animation", false);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}