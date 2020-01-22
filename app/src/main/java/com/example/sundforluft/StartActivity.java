package com.example.sundforluft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.services.SchoolAverageLoader;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private Button elev, laerer, guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        boolean isNotDebug = !BuildConfig.DEBUG;
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isNotDebug);

        Intent intent = getIntent();
        boolean animation = intent.getBooleanExtra("animation", true);

        DataAccessLayer.getInstance(); // Start async loading of all schools.

        // load this animation
        Animation smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        Animation btta = AnimationUtils.loadAnimation(this, R.anim.btta);
        Animation btta2 = AnimationUtils.loadAnimation(this, R.anim.btta2);

        ImageView imageView = findViewById(R.id.logo);
        TextView titel = findViewById(R.id.sundforluft);

        elev = findViewById(R.id.elev);
        laerer = findViewById(R.id.laerer);

        ImageView eller = findViewById(R.id.eller);
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
        if (DataAccessLayer.getInstance().isLoaded()) {
            if (v == elev) {
                if (SchoolAverageLoader.getInstance().isLoaded()) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else{
                    DataAccessLayer.getInstance().reloadFromInternet();
                }
            } else if (v == laerer) {
                Intent i = new Intent(this, TeacherLoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (v == guest) {
                Intent i = new Intent(this, QRScanner.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
        else{
            DataAccessLayer.getInstance().reloadFromInternet();
        }
    }

    @Override
    public void onBackPressed() {

    }

}
