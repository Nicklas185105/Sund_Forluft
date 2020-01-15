package com.example.sundforluft.services.DataBroker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kotlin.NotImplementedError;

public class CloudDataBroker implements DataBroker {
    public final String deviceName;
    private final String accessToken;

    private ArrayList<AirQualityDataModel> data;

    public CloudDataBroker(String deviceName, String accessToken) {
        this.deviceName = deviceName;
        this.accessToken = accessToken;

        data = new ArrayList<>();
    }

    @Override
    public boolean load(LocalDateTime start, LocalDateTime end) {
        /*
            curl -X GET "https://api.allthingstalk.io/asset/{deviceName}/states?from=2019-12-01T00:01:00+0100&to=2019-12-02T23:59:00" -H "Authorization: Bearer {accessToken}"
        */
        try {
            // Create url request to asset state of device.
            String requestUrl = String.format("https://api.allthingstalk.io/asset/%s/states", deviceName);
            HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", String.format("Bearer %s", accessToken));

            // Format date string for API call.
            // The date string gives the interval "from" and "to" where the API should return data in.
            String formattedDateString;

            try {
                // Format the start and end dates into appropriate strings.
                String fromString = toCloudDateString(start);
                String toString = toCloudDateString(end);

                // Example output string: from=2019-12-01T00:01:00+0100&to=2019-12-02T23:59:00
                formattedDateString = String.format("from=%s+0100&to=%s", fromString, toString);
            } catch (NotImplementedError nie) {

                // Since we haven't implemented cloud date strings yet, we will use a sample string for testing.
                formattedDateString = "from=2019-12-01T00:01:00+0100&to=2019-12-02T23:59:00";
            }

            // Write data for request.
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write( formattedDateString );
            writer.flush();

            // Read output from server.
            StringBuilder builder = new StringBuilder();
            String line;
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(String.format("%s\n", line));
            }

            // Interpret server data into local objects.
            boolean x = false;

            // Assuming we reached this far, we have data ready to present.
            // Therefore the function succeeded.
            return true;
        } catch (Exception e) {
            // Some exception has occured so load failed.
            // Return false.
            return false;
        }
    }

    private String toCloudDateString(LocalDateTime time) {
        // TODO: Format to string of type: "2019-12-02T23:59:00" depending on the date.
        throw new NotImplementedError();
    }

    @Override
    public List<AirQualityDataModel> getData() {
        return null;
    }
}
