package com.example.sundforluft.cloud;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ATTOAuthToken {
    // Singleton Implementation
    private static ATTOAuthToken instance;
    private ATTOAuthToken() {}

    // Holds access token.
    private String accessToken = null;
    private boolean loading = false;

    // Instance initizilation and retriving.
    public static ATTOAuthToken getInstance() {
        if (instance == null) {
            instance = new ATTOAuthToken();

            instance.loading = true;
            new Thread(() -> {
                if (!instance.renewToken()) {
                    System.out.println("Error during token retrieval!!!");
                } else {
                    instance.loading = false;
                }
            }).start();
        }

        return instance;
    }

    // Retrieves the current token
    String getCurrentToken() {
        return accessToken;
    }

    // Renews the token using OAuth2
    // Powershell Request:
    // Invoke-WebRequest -Uri "https://api.allthingstalk.io/login" -Method "POST" -Headers @{"method"="POST"; } -ContentType "application/x-www-form-urlencoded" -Body "grant_type=password&username=[username]&password=[password]&client_id=web"
    private boolean renewToken() {
        String contentType = "application/x-www-form-urlencoded", method = "POST", URI = "https://api.allthingstalk.io/login";

        try {
            // Request for OAuth token
            HttpURLConnection connection = (HttpURLConnection) new URL(URI).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", contentType);
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            osw.write("grant_type=password&username=TeknologiskInstitut&password=TeknologiskInstitutSF&client_id=web");
            osw.flush();
            osw.close();
            os.close();
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
            JSONObject rootOfPage =  new JSONObject( result );
            accessToken = rootOfPage.get("access_token").toString();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    boolean isLoading() {
        return loading;
    }
}
