package com.example.sundforluft;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.ATTOAuthToken;
import com.example.sundforluft.services.SchoolAverageLoader;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Activity self = this;

        new Thread(() -> {
            while (!isNetworkAvailable()) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            loadCriticalInternetData();
            self.runOnUiThread(() -> {
                Intent homeIntet = new Intent(SplashScreen.this, StartActivity.class);
                startActivity(homeIntet);
                finish();
            });
        }).start();


    }
    private void loadCriticalInternetData() {
        ATTOAuthToken.getInstance(); // retrieve token async.
        ATTCommunicator.getInstance();
        SchoolAverageLoader schoolAverages = SchoolAverageLoader.getInstance();
        if (!schoolAverages.isLoaded()) {
            new Thread(() -> {
                DataAccessLayer.getInstance().waitForLoad();
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
