package com.example.sundforluft;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sundforluft.models.SchoolModel;
import com.example.sundforluft.services.SchoolBadgeAdapter;

public class HomeActivity extends AppCompatActivity {
    SchoolBadgeAdapter schoolBadgeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        schoolBadgeAdapter = new SchoolBadgeAdapter(this);

        SchoolModel[] schoolModels = new SchoolModel[] {
            new SchoolModel(this, "Gentofte Skole", 29),
            new SchoolModel(this, "Dyssegård Skole", 24),
            new SchoolModel(this, "Ishøj Perker Skole", 4),
            new SchoolModel(this, "Hellerup Skole", 42),
        };
        for (SchoolModel schoolModel : schoolModels) { schoolBadgeAdapter.addSchool(schoolModel); }

        ListView schoolModelListView = null;
        schoolModelListView.setAdapter(schoolBadgeAdapter);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
