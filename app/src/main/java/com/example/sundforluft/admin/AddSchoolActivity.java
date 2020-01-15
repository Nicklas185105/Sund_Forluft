package com.example.sundforluft.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.UserModel;
import com.example.sundforluft.R;

import carbon.view.View;

public class AddSchoolActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    Button add;
    EditText username, password, school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_school);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.addSchool);

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        add = findViewById(R.id.button);
        add.setOnClickListener(this);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        school = findViewById(R.id.schoolEditText);
    }

    @Override
    public void onClick(android.view.View v) {
        if (v == add){
            if (!username.getText().toString().equals("") && !password.getText().toString().equals("") && !school.getText().toString().equals("")){
                DataAccessLayer.getInstance().addSchool(
                        school.getText().toString(),
                        new UserModel(username.getText().toString(), password.getText().toString())
                );
                while (!DataAccessLayer.getInstance().isLoaded()){}
                Toast.makeText(getApplicationContext(),
                        R.string.schoolAdded + school.getText().toString() + R.string.schoolAddedRemoved,
                        Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
                school.setText("");
            }
        }
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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
