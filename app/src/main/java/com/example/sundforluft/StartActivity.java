package com.example.sundforluft;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.ATTOAuthToken;
import com.example.sundforluft.services.SchoolAverageLoader;

import java.util.Calendar;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView, eller;
    private TextView titel;
    private Button elev, laerer, guest;
    private Animation smalltobig, btta, btta2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (isNetworkAvailable()) {
            loadCriticalInternetData();
        } else {
            // TODO: Strings.xml
            Toast.makeText(getApplicationContext(), "You must have internet to use this application!", Toast.LENGTH_LONG).show();
        }


        Intent intent = getIntent();
        boolean animation = intent.getBooleanExtra("animation", true);

        DataAccessLayer.getInstance(); // Start async loading of all schools.

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
        if (DataAccessLayer.getInstance().isLoaded()) {
            if (v == elev) {
                if (SchoolAverageLoader.getInstance().isLoaded()) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    // TODO: Strings.xml
                    Toast.makeText(getApplicationContext(), "Henter CO2 gennemsnit for skoler. Vent venligst.", Toast.LENGTH_SHORT).show();
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
        } else {
            if (isNetworkAvailable()) {
                loadCriticalInternetData();
                Toast.makeText(getApplicationContext(), R.string.gettingSchools, Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Strings.xml
                Toast.makeText(getApplicationContext(), "You must have internet to use this application!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void loadCriticalInternetData() {
        ATTOAuthToken.getInstance(); // retrieve token async.
        ATTCommunicator.getInstance();
        SchoolAverageLoader schoolAverages = SchoolAverageLoader.getInstance();
        StartActivity self = this;
        if (!schoolAverages.isLoaded()) {
            new Thread(() -> {
                DataAccessLayer.getInstance().waitForLoad();
                self.runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Loading school Co2 averages", Toast.LENGTH_SHORT).show());
                schoolAverages.setSchools(DataAccessLayer.getInstance().getSchools());
                new Thread(schoolAverages::loadSchoolAverages).start();
            }).start();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
