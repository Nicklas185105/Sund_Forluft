package com.example.sundforluft.cloud;

import android.net.Network;
import android.os.NetworkOnMainThreadException;

import com.example.sundforluft.cloud.DAO.ATTDevice;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfo;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfoMeasurement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import kotlin.NotImplementedError;

public class ATTCommunicator {
    // Singleton instance.
    private static ATTCommunicator instance;

    // Fields
    private boolean loading;
    private final Object mutex = new Object();
    private ArrayList<ATTDevice> devices;
    private ATTOAuthToken token = null;

    // Private constructor to ensure single instance concept.
    private ATTCommunicator() {
        loading = true;
        devices = new ArrayList<>();
    }

    // Properties
    public static ATTCommunicator getInstance() {
        try {
            if (instance == null) {
                instance = new ATTCommunicator();
                instance.token = ATTOAuthToken.getInstance();

                new Thread(() -> {
                    // Wait for ATTOAuthToken to load
                    try {
                        while (instance.token.isLoading()) {
                            Thread.sleep(15);
                        }
                    } catch (InterruptedException ie) {
                        System.out.println("ATTCommunicator thread was interrupted while waiting for authentication token to load");
                    }

                    // Now get devices (OAuth2 token loaded).
                    instance.loadDevices();
                }).start();

                instance.devices = instance.retrieveDevicesFromInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }
    public ArrayList<ATTDevice> getDevices() {
        return devices;
    }

    // Public functions
    // This method must be called from a thread!
    public ATTDeviceInfo loadMeasurementsForDevice(ATTDevice device) {
        try {
            String from = "2020-01-16T14:27:18+00:00";
            String to = "2020-01-16T14:32:18+00:00";

            String query = String.format("from=%s&to=%s", URLEncoder.encode(from, "UTF-8"), URLEncoder.encode(to, "UTF-8"));
            String uri = String.format("https://api.allthingstalk.io/asset/%s/states?%s", device.CO2AssetId, query);

            HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("authorization", "Bearer " + ATTOAuthToken.getInstance().getCurrentToken());
            connection.connect();

            //read the inputstream and print it
            String result;
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result2 = bis.read();
            while(result2 != -1) {
                buf.write((byte) result2);
                result2 = bis.read();
            }
            result = buf.toString();

            // Parse resulting data.
            JSONObject root =  new JSONObject( result );
            JSONArray data = (JSONArray) root.get("data");

            ArrayList<ATTDeviceInfoMeasurement> measurementList = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject dataPoint = (JSONObject) data.get(i);

                Instant instant = Instant.parse(dataPoint.getString("at"));
                measurementList.add(new ATTDeviceInfoMeasurement(dataPoint.getDouble("data"), Date.from(instant)));
            }

            ATTDeviceInfoMeasurement[] measurements = new ATTDeviceInfoMeasurement[measurementList.size()];
            measurementList.toArray(measurements);
            return new ATTDeviceInfo(measurements);
        } catch (NetworkOnMainThreadException nmt) {
            throw nmt;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // TODO: Custom exception here.
        }
    }

    public ATTDevice getDeviceById(String deviceId) {
        for (ATTDevice device : devices) {
            if (device.deviceId.equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    // Internal methods used on load.
    private void loadDevices() {
        synchronized (instance.mutex) { // Lock mutex for operation!
            loading = true;

            try {
                devices = retrieveDevicesFromInternet();

            } catch (Exception e) {
                e.printStackTrace();
            }

            loading = false;
        }
    }
    private ArrayList<ATTDevice> retrieveDevicesFromInternet() throws Exception {
        // Request for OAuth token
        HttpURLConnection connection = (HttpURLConnection) new URL("https://api.allthingstalk.io/devices?includeAssets=true").openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("authorization", "Bearer " + ATTOAuthToken.getInstance().getCurrentToken());
        connection.connect();

        //read the inputstream and print it
        String result;
        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result2 = bis.read();
        while(result2 != -1) {
            buf.write((byte) result2);
            result2 = bis.read();
        }
        result = buf.toString();

        // Parse resulting token.
        JSONArray root =  new JSONArray( result );
        ArrayList<ATTDevice> devices = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            // Get the current device (all info about it) as a json object.
            JSONObject currentDevice = (JSONObject) root.get(i);

            // Retrieve sublist of assets and locate the CO2 asset id.
            String CO2AssetId = "";
            JSONArray assets = (JSONArray)currentDevice.get("assets");
            for (int assetIndex = 0; assetIndex < assets.length(); assetIndex++) {
                JSONObject currentAsset = (JSONObject) assets.get(assetIndex);
                if (currentAsset.get("name").equals("co2")) {
                    CO2AssetId = currentAsset.getString("id");
                }
            }

            // Create device
            ATTDevice parsedDevice = new ATTDevice(
                    currentDevice.getString("id"),
                    currentDevice.getString("name"),
                    CO2AssetId
            );

            // Exclude the WES handle for gateway.
            if (!parsedDevice.name.equals("WES_")) {
                // Add * devices that isn't WES handle
                devices.add(parsedDevice);
            }
        }

        return devices;
    }


    public boolean isLoading() {
        return loading;
    }
    public void waitForLoad() {
        try {
            while (loading) {
                Thread.sleep(15);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
