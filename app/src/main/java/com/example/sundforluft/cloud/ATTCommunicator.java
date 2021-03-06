package com.example.sundforluft.cloud;

import android.os.NetworkOnMainThreadException;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.cloud.DAO.ATTDevice;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfo;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfoMeasurement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    // Public Enums
    public enum MeasurementInterval {
        MonthPerDay,
        WeekPerHour,
        DayPerTenMin,
        HourPerMin
    }

    private String getResoultion(MeasurementInterval interval) {
        switch (interval) {
            case MonthPerDay: return "day";
            case WeekPerHour: return "hour";
            case DayPerTenMin:
            case HourPerMin:
                return "minute";
            default: return "";
        }
    }

    // Public functions
    // Below methods MUST be called by a thread!
    public ATTDeviceInfo loadMeasurementsForDevice(ATTDevice device, Date start, MeasurementInterval interval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);

        switch (interval) {
            case MonthPerDay:
                cal.add(Calendar.MONTH, 1);
                break;
            case WeekPerHour:
                cal.add(Calendar.DATE, 7);
                break;
            case DayPerTenMin:
                cal.add(Calendar.DATE, 1);
                break;
            case HourPerMin:
                cal.add(Calendar.HOUR, 1);
                break;
        }


        return loadMeasurementsForDevice(device, start, cal.getTime(), interval);
    }
    private ATTDeviceInfo loadMeasurementsForDevice(ATTDevice device, Date start, Date end, MeasurementInterval interval) {
        try {
            String from = start.toInstant().toString().replace("Z", "+00:00");
            String to = end.toInstant().toString().replace("Z", "+00:00");

            String query = String.format("from=%s&to=%s&resolution=%s", URLEncoder.encode(from, "UTF-8"), URLEncoder.encode(to, "UTF-8"), getResoultion(interval));
            String uri = String.format("https://api.allthingstalk.io/asset/%s/activity?%s", device.CO2AssetId, query);

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
                JSONObject entryData = (JSONObject) dataPoint.get("data");
                double min = entryData.getDouble("min");
                double max = entryData.getDouble("max");
                double avg = entryData.getDouble("avg");

                measurementList.add(new ATTDeviceInfoMeasurement(avg, min, max, Date.from(instant)));
            }

            ATTDeviceInfoMeasurement[] measurements = new ATTDeviceInfoMeasurement[measurementList.size()];
            measurementList.toArray(measurements);
            return new ATTDeviceInfo(measurements);
        } catch (NetworkOnMainThreadException nmt) {
            throw nmt;
        } catch (Exception e) {
            // Try to redo action after 1s.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return loadMeasurementsForDevice(device, start, end, interval);
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

    public double getSchoolAverage(int schoolId) {
        waitForLoad();
        ArrayList<ClassroomModel> classrooms = DataAccessLayer.getInstance().getClassroomsBySchoolId(schoolId);

        double sum = 0;
        int count = 0;

        for (ClassroomModel classroom : classrooms) {
            ATTDevice device = getDeviceById(classroom.deviceId);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            ATTDeviceInfo info = loadMeasurementsForDevice(device, cal.getTime(), MeasurementInterval.MonthPerDay);

            sum += info.getAverageQuality();
            count++;
        }

        return sum / count;
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

    private boolean isLoading() {
        return loading;
    }
    public void waitForLoad() {
        try {
            while (isLoading()) {
                Thread.sleep(15);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
