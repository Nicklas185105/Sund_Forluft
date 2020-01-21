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
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.services.Globals;
import com.example.sundforluft.services.SchoolAverageLoader;

import java.util.Locale;

public class TeacherMainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView text;
    Button addCloud, allClouds, removeCloud, logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        text = findViewById(R.id.welcome);
        text.setText(Globals.school.Name);

        TextView average = findViewById(R.id.average);
        average.setText(String.format(Locale.ENGLISH,  getResources().getString(R.string.airquality_avg), SchoolAverageLoader.getInstance().getCachedAverageBySchoolId(Globals.school.Id)));

        addCloud = findViewById(R.id.addCloud);
        allClouds = findViewById(R.id.allClouds);
        removeCloud = findViewById(R.id.removeCloud);
        logout = findViewById(R.id.logout);

        addCloud.setOnClickListener(this);
        allClouds.setOnClickListener(this);
        removeCloud.setOnClickListener(this);
        logout.setOnClickListener(this);
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
                i = new Intent(getApplicationContext(), CloudsOverviewActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.removeCloud:
                i = new Intent(getApplicationContext(), QRScanner.class);
                i.putExtra("isRemove", true);
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
        Globals.isTeacher = false; // Revoke teacher rights.
        Intent intent = new Intent(TeacherMainActivity.this, StartActivity.class);
        intent.putExtra("animation", false);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
