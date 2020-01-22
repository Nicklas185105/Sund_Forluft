package com.example.sundforluft.teacher;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.R;
import com.example.sundforluft.services.TeacherCloudsOverviewAdapter;
import com.example.sundforluft.services.Globals;

import java.util.ArrayList;

public class CloudsOverviewActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_clouds_overview);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.oversigt_over_skyer);

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = findViewById(R.id.listView);
        DataAccessLayer dataAccessLayer = DataAccessLayer.getInstance();
        ArrayList<ClassroomModel> models = dataAccessLayer.getClassroomsBySchoolId(Globals.school.Id);
        TeacherCloudsOverviewAdapter adapter = new TeacherCloudsOverviewAdapter(CloudsOverviewActivity.this, models);
        listView.setAdapter(adapter);
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
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
