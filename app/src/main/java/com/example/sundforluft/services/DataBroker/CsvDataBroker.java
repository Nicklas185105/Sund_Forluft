package com.example.sundforluft.services.DataBroker;

import android.content.res.Resources;

import com.example.sundforluft.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvDataBroker implements DataBroker {
    private Resources res;

    private LocalDateTime start;
    private LocalDateTime end;

    private ArrayList<AirQualityDataModel> data;

    public CsvDataBroker(Resources res) {
        this.res = res; // getResources()
    }

    @Override
    public boolean load(LocalDateTime start, LocalDateTime end) {
        data = new ArrayList<>();

        InputStream inputStream = res.openRawResource( R.raw.data );

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        try {
            while (( line = buffreader.readLine()) != null) {
                AirQualityDataModel model = parseModel( line );

                if (model.Date.isAfter(start)) {
                    data.add(model);
                }
                if (model.Date.isAfter(end)) {
                    break; //stop parsing
                }
            }
        } catch (IOException e) {
            return false; // Error parsing..
        }

        this.start = start;
        this.end = end;
        return true;
    }

    @Override
    public List<AirQualityDataModel> getData() {
        return data;
    }

    private AirQualityDataModel parseModel(String line) {
        String[] parts = line.split(",");

        AirQualityDataModel model = new AirQualityDataModel();

        model.Date = LocalDateTime.parse(parts[0]);
        model.CO2 = Double.parseDouble( parts[1] );

        return model;
    }

}
