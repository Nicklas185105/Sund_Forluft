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

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.teacher.TeacherMainActivity;
import com.example.sundforluft.admin.AdminMainActivity;
import com.example.sundforluft.services.Globals;
import com.example.sundforluft.services.MD5Converter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherLoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button button, teacher, admin;
    Toolbar toolbar;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);


        button = findViewById(R.id.button);
        teacher = findViewById(R.id.teacherLoginButton);
        admin = findViewById(R.id.adminLoginButton);
        button.setOnClickListener(this);
        teacher.setOnClickListener(this);
        admin.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.teacherLogin);

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v == button){
            tryLoginToAccount(
                    username.getText().toString(),
                    password.getText().toString()
            );
        }
        else if (v == teacher){
            username.setText("brugernavn");
            password.setText("password");
            tryLoginToAccount(
                    username.getText().toString(),
                    password.getText().toString()
            );
        }
        else if (v == admin){
            username.setText("admin");
            password.setText("password");
            tryLoginToAccount(
                    username.getText().toString(),
                    password.getText().toString()
            );
        }
    }

    private void tryLoginToAccount(String username, String password) {
        if (username.length()==0 || password.length()==0){
            Toast.makeText(this, R.string.fill, Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String compare = MD5Converter.md5(password);
        DatabaseReference myRef = database.getReference("users/" + username);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String password = dataSnapshot.child("password").getValue(String.class);
                if (password.equals(compare)) {
                    // Obtain school
                    Globals.school = DataAccessLayer.getInstance().getSchoolById(dataSnapshot.child("schoolId").getValue(Integer.class));

                    // School Obtained & Logged In.
                    if (username.toLowerCase().equals("admin")){
                        Globals.isAdmin = true;
                        Intent intent = new Intent(TeacherLoginActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    else {
                        Globals.isTeacher = true;

                        Intent intent = new Intent(TeacherLoginActivity.this, TeacherMainActivity.class);
                        intent.putExtra("name", username);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), R.string.toastWrongUserPass, Toast.LENGTH_SHORT).show();
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
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*Intent intent = new Intent(TeacherLoginActivity.this, StartActivity.class);
        intent.putExtra("animation", false);
        startActivity(intent);*/
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
