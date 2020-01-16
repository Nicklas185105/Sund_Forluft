package com.example.sundforluft.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.R;
import com.example.sundforluft.services.Globals;

public class AddCloudActivity extends AppCompatActivity {

    EditText editText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_cloud);

        editText = findViewById(R.id.addCloudEditText);
        submitButton = findViewById(R.id.addCloudSubmitButton);

        Bundle bundle = getIntent().getExtras();
        int id = Globals.school.Id;
        String accessToken = bundle.getString("accessToken");
        String deviceId = bundle.getString("deviceId");
        String name = editText.getText().toString();


        submitButton.setOnClickListener(v -> {
            DataAccessLayer.getInstance().addClassroom(id, accessToken, deviceId, editText.getText().toString());
            Intent intent = new Intent(AddCloudActivity.this, TeacherMainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
}
