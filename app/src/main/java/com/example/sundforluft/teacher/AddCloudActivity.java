package com.example.sundforluft.teacher;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.R;
import com.example.sundforluft.services.Globals;

public class AddCloudActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText editText;
    Button submitButton;

    int id;
    String accessToken, deviceId, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_cloud);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.addCloud);

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editText = findViewById(R.id.addCloudEditText);
        submitButton = findViewById(R.id.addCloudSubmitButton);

        Bundle bundle = getIntent().getExtras();
        id = Globals.school.Id;
        accessToken = bundle.getString("accessToken");
        deviceId = bundle.getString("deviceId");


        submitButton.setOnClickListener(v -> {
            name = editText.getText().toString();
            if (name.length() == 0) {
                Toast.makeText(getApplicationContext(), "Venligst udfyld feltet", Toast.LENGTH_SHORT).show();
                return;
            }
            DataAccessLayer dataAccessLayer = DataAccessLayer.getInstance();
            ClassroomModel modelFromDeviceId = dataAccessLayer.getClassroomByDeviceId(deviceId);
            ClassroomModel modelFromName = DataAccessLayer.getInstance().getClassroomBySchooldAndName(id, name);

            if (modelFromDeviceId != null || modelFromName != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (modelFromDeviceId != null){
                    if (modelFromName != null) {
                        builder.setMessage("Dette device er allerede registreret og navnet du har valgt findes allerede\nVil du overskrive eksisterende devices?")
                                .setPositiveButton("Ja", (dialog, which) -> {
                                    dataAccessLayer.removeClassroom(modelFromName);
                                    dataAccessLayer.removeClassroom(modelFromDeviceId);
                                    dataAccessLayer.addClassroom(id, accessToken, deviceId, name);
                                    Intent intent = new Intent(AddCloudActivity.this, TeacherMainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                })
                                .setNegativeButton("Nej", null);
                    }
                    builder.setMessage("Dette device er allerede registreret med navnet: "+modelFromDeviceId.name+"\nVil du overskrive dette device?")
                            .setNegativeButton("Nej", null)
                            .setPositiveButton("Ja", (dialog, which) -> {
                                dataAccessLayer.removeClassroom(modelFromDeviceId);
                                dataAccessLayer.addClassroom(id, accessToken, deviceId, name);
                                Intent intent = new Intent(AddCloudActivity.this, TeacherMainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }).create().show();
                } else if (modelFromName != null){
                    builder.setMessage("Der eksisterer allerede et device med navnet: "+modelFromName.name+"\n Vil du overføre navnet til dette device?")
                            .setNegativeButton("Nej", null)
                            .setPositiveButton("Ja", (dialog, which) -> {
                                dataAccessLayer.removeClassroom(modelFromName);
                                dataAccessLayer.addClassroom(id, accessToken, deviceId, name);
                                Intent intent = new Intent(AddCloudActivity.this, TeacherMainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }).create().show();
                }
            } else {
                dataAccessLayer.addClassroom(id, accessToken, deviceId, name);
                Intent intent = new Intent(AddCloudActivity.this, TeacherMainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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
