package com.example.sundforluft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sundforluft.DAL.SchoolsLocator;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView, eller;
    private TextView titel;
    private Button elev, laerer, guest;
    private Animation smalltobig, btta, btta2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent intent = getIntent();
        boolean animation = intent.getBooleanExtra("animation", true);

        SchoolsLocator.getInstance(); // Start async loading of all schools.

        // load this animation
        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        btta = AnimationUtils.loadAnimation(this, R.anim.btta);
        btta2 = AnimationUtils.loadAnimation(this, R.anim.btta2);

        imageView = findViewById(R.id.logo);
        titel = findViewById(R.id.sundforluft);

        elev = findViewById(R.id.elev);
        laerer = findViewById(R.id.laerer);

        eller = findViewById(R.id.eller);
        guest = findViewById(R.id.gaest);

        // passing animation and start it
        if (animation) {
            imageView.startAnimation(smalltobig);
            titel.startAnimation(smalltobig);

            elev.startAnimation(btta);
            laerer.startAnimation(btta);

            eller.startAnimation(btta2);
            guest.startAnimation(btta2);
        }

        elev.setOnClickListener(this);
        laerer.setOnClickListener(this);
        guest.setOnClickListener(this);
    }

    public void onClick(View v){
        if (SchoolsLocator.getInstance().isLoaded()) {
            if (v == elev) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (v == laerer) {
                Intent i = new Intent(this, TeacherLoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (v == guest) {
                Intent i = new Intent(this, GuestLogin.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        } else {
            /*TODO: Text in Strings.xml*/
            Toast.makeText(getApplicationContext(), "Henter skoler. Vent venligst.", Toast.LENGTH_SHORT).show();
        }
    }
}
