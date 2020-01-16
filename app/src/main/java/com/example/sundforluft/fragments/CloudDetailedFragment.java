package com.example.sundforluft.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.R;
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfo;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfoMeasurement;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CloudDetailedFragment extends Fragment {

    LineChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cloud_detailed, container, false);
        chart = view.findViewById(R.id.chart);

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        // TODO: Strings.xml
        chart.setNoDataText("Data is being loaded from cloud.. Please wait");
        chart.invalidate();


        final String deviceId =  "NpWCNaQC5ULxNkTaJ7FnRYKK"; //getActivity().getIntent().getStringExtra("deviceId");

        // TODO: Strings.xml
        Toast.makeText(getContext(), "Loading data from cloud", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            ATTCommunicator communicator = ATTCommunicator.getInstance();
            communicator.waitForLoad();
            ATTDeviceInfo deviceInfo = communicator.loadMeasurementsForDevice(communicator.getDeviceById(deviceId));
                float biggestTime = 0;
                for (ATTDeviceInfoMeasurement measurement : deviceInfo.getMeasurements()) {
                    float time = (float) measurement.time.getTime();
                    if (time >= biggestTime) {
                        biggestTime = time;
                    }

                    addEntry((float)measurement.average);
                }
            chart.getLegend().setEnabled(false);
        }).start();

        // add some transparency to the color with "& 0x90FFFFFF"
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisRight().setEnabled(false);
        return view;
    }

    private void addEntry(float yValue) {
        LineData data = chart.getData();

        if (data == null) {
            data = new LineData();
            chart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        data.addEntry(new Entry(set.getEntryCount(), yValue),0);
        data.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.setVisibleXRangeMaximum(6);

        chart.moveViewTo(data.getEntryCount() - 7, 400f, YAxis.AxisDependency.LEFT);
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dataset 1");
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        return set;
    }
}
