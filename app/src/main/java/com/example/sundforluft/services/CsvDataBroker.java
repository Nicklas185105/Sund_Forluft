package com.example.sundforluft.services;

import android.content.res.Resources;

import com.example.sundforluft.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class CsvDataBroker implements SundForluftDataBroker {
    private Resources res;

    CsvDataBroker(Resources res) {
        this.res = res; // getResources()
    }

    @Override
    public void Load() {  }

    @Override
    public SundforluftDataModel[] GetData(Date start, Date end) {
        InputStream inputStream = res.openRawResource( R.raw.data );

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        ArrayList<SundforluftDataModel> dataModelArrayList = new ArrayList<>();

        try {
            while (( line = buffreader.readLine()) != null) {
                SundforluftDataModel model = parseModel( line );

                if (model.Date.after(start)) {
                    dataModelArrayList.add(model);
                }
                if (model.Date.after(end)) { break; }//stop parsing
            }
        } catch (IOException e) {
            return null;
        }

        return (SundforluftDataModel[]) dataModelArrayList.toArray();
    }

    private SundforluftDataModel parseModel(String line) {
        String[] parts = line.split(",");

        SundforluftDataModel model = new SundforluftDataModel();

        Instant instant = Instant.parse(parts[0]);
        model.Date = Date.from(instant);
        model.CO2 = Double.parseDouble( parts[1] );

        return model;
    }

}
