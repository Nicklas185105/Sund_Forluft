package com.example.sundforluft;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.DAL.SchoolsLocator;
import com.example.sundforluft.services.Globals;
import com.example.sundforluft.services.MD5Converter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherLoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lærer Login");

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v == button){
            EditText username = findViewById(R.id.usernameEditText);
            EditText password = findViewById(R.id.passwordEditText);

            tryLoginToAccount(
                    username.getText().toString(),
                    password.getText().toString()
            );
        }
    }

    private void tryLoginToAccount(String username, String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String compare = MD5Converter.md5(password);

        DatabaseReference myRef = database.getReference("users/" + username);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String password = dataSnapshot.child("password").getValue(String.class);
                if (password.equals(compare)) {
                    Globals.isTeacher = true;

                    // Obtain school
                    Globals.school = SchoolsLocator.getInstance().getSchoolById(dataSnapshot.child("schoolId").getValue(Integer.class));

                    // School Obtained & Logged In.
                    Intent intent = new Intent(TeacherLoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    /*TODO: Text in strings.xml*/
                    Toast.makeText(getApplicationContext(), "Forket brugernavn/kodeord", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {  }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        return super.onOptionsItemSelected(item);
    }
}
