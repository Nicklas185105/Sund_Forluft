package com.example.sundforluft.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.R;
import com.example.sundforluft.StartActivity;

public class TeacherMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*TODO: Text in strings.xml*/
        getSupportActionBar().setTitle("Velkommen");

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        text = findViewById(R.id.welcome);
        Intent intent = getIntent();
        text.setText("Velkommen " + intent.getStringExtra("name"));
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
        //super.onBackPressed();
        Intent intent = new Intent(TeacherMainActivity.this, StartActivity.class);
        intent.putExtra("animation", false);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
