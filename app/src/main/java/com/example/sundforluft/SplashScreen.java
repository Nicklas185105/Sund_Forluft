package com.example.sundforluft;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.ATTOAuthToken;
import com.example.sundforluft.services.SchoolAverageLoader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

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
                Intent homeIntent = new Intent(SplashScreen.this, StartActivity.class);
                startActivity(homeIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    public static boolean isNetworkAvailable () {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

}
