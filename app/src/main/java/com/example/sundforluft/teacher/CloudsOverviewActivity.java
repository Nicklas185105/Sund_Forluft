package com.example.sundforluft.teacher;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.R;
import com.example.sundforluft.services.TeacherCloudsOverviewAdapter;
import com.example.sundforluft.services.Globals;

import java.util.ArrayList;

public class CloudsOverviewActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_clouds_overview);

        listView = findViewById(R.id.teacherCloudOverviewListView);
        DataAccessLayer dataAccessLayer = DataAccessLayer.getInstance();
        ArrayList<ClassroomModel> models = dataAccessLayer.getClassroomsBySchoolId(Globals.school.Id);
        TeacherCloudsOverviewAdapter adapter = new TeacherCloudsOverviewAdapter(CloudsOverviewActivity.this, models);
        listView.setAdapter(adapter);
    }
}
