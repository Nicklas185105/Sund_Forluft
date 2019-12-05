package com.example.sundforluft.services;

import android.content.res.Resources;

import com.example.sundforluft.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsvDataBroker implements SundForluftDataBroker {
    private Resources res;

    public CsvDataBroker(Resources res) {
        this.res = res; // getResources()
    }

    @Override
    public void Load() {  }

    @Override
    public List<SundforluftDataModel> GetData(LocalDateTime start, LocalDateTime end) {
        InputStream inputStream = res.openRawResource( R.raw.data );

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        ArrayList<SundforluftDataModel> dataModelArrayList = new ArrayList<>();

        try {
            while (( line = buffreader.readLine()) != null) {
                SundforluftDataModel model = parseModel( line );

                if (model.Date.isAfter(start)) {
                    dataModelArrayList.add(model);
                }
                if (model.Date.isAfter(end)) {
                    break; //stop parsing
                }
            }
        } catch (IOException e) {
            return null;
        }

        return dataModelArrayList;
    }

    private SundforluftDataModel parseModel(String line) {
        String[] parts = line.split(",");

        SundforluftDataModel model = new SundforluftDataModel();

        model.Date = LocalDateTime.parse(parts[0]);
        model.CO2 = Double.parseDouble( parts[1] );

        return model;
    }

}
