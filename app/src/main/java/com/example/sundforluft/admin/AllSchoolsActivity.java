package com.example.sundforluft.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.R;
import com.example.sundforluft.services.AdminCloudsOverviewAdapter;

import java.util.ArrayList;

public class AllSchoolsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_schools);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.allSchools);

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = findViewById(R.id.listView);
        DataAccessLayer dataAccessLayer = DataAccessLayer.getInstance();
        ArrayList<ClassroomModel> classroomModels = new ArrayList<>();
        SchoolModel[] schoolModels;

        for (SchoolModel schoolModel : schoolModels = dataAccessLayer.getSchools()){
            classroomModels.addAll(dataAccessLayer.getClassroomsBySchoolId(schoolModel.Id));
        }
        AdminCloudsOverviewAdapter adapter = new AdminCloudsOverviewAdapter(this, classroomModels, schoolModels);
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
