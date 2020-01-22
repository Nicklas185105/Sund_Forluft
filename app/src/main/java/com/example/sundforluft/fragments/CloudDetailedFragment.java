package com.example.sundforluft.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.DAO.ATTDevice;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfo;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfoMeasurement;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CloudDetailedFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private LineChart chart;
    private LineData data;
    private String deviceId;

    private TextView textView;

    // TODO: Strings.xml
    private static final String[] paths = {
        "1 Måned [per dag]",
        "1 Uge [per time]"
    };

    private ArrayList<Long> times;
    private DateFormatter dateFormatter;

    private double lowestValue = Double.MAX_VALUE;
    private double highestValue = 0;
    private double averageValue = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cloud_detailed, container, false);
        dateFormatter = new DateFormatter();

        textView = view.findViewById(R.id.textViewInfo);
        chart = view.findViewById(R.id.chart);


        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        // TODO: Strings.xml
        chart.setNoDataText(getResources().getString(R.string.wait_data_loaded_from_cloud));
        chart.setPinchZoom(true);

        Bundle bundle = this.getArguments();
        deviceId = Objects.requireNonNull(bundle).getString("deviceId");

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.spinner_layout, paths);
        adapter.setDropDownViewResource(R.layout.spinner_layout_2);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // add some transparency to the color with "& 0x90FFFFFF"
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(dateFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float co2_max = chart.getData().getDataSetByIndex(0).getEntryForIndex((int)e.getX()).getY();
                float co2_avg = chart.getData().getDataSetByIndex(1).getEntryForIndex((int)e.getX()).getY();
                float co2_min = chart.getData().getDataSetByIndex(2).getEntryForIndex((int)e.getX()).getY();

                TextView textView = view.findViewById(R.id.textViewData);
                textView.setText(
                        String.format(Locale.ENGLISH, "Maksimum: %.2f ppm\nGennemsnit: %.2f ppm\nMinimum: %.2f ppm", co2_max, co2_avg, co2_min)
                );
            }

            @Override
            public void onNothingSelected()  { }
        });


        chart.getAxisRight().setEnabled(false);
        return view;
    }

    private void loadDeviceData(String deviceId, ATTCommunicator.MeasurementInterval interval) {
        if (chart.getData() != null) {
            chart.getData().clearValues();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        }

        Activity self = getActivity();
        dateFormatter.setMeasurementInterval(interval);

        // TODO: Strings.xml
        Toast.makeText(getContext(), getResources().getString(R.string.wait_data_loaded_from_cloud), Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            ATTCommunicator communicator = ATTCommunicator.getInstance();
            communicator.waitForLoad();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            ATTDevice device = communicator.getDeviceById(deviceId);
            ATTDeviceInfo deviceInfo = communicator.loadMeasurementsForDevice(device, cal.getTime(), interval);

            ATTDeviceInfoMeasurement[] measurements = deviceInfo.getMeasurements();
            if (measurements.length == 0) {
                return;
            }

            Arrays.sort(measurements);

            times = new ArrayList<>();

            lowestValue = Double.MAX_VALUE;
            highestValue = 0;
            averageValue = 0;

            double avg_sum = 0;

            try {
                int count = 0;
                for (ATTDeviceInfoMeasurement measurement : deviceInfo.getMeasurements()) {
                    times.add(measurement.time.getTime());

                    if (measurement.minimum < lowestValue) { lowestValue = measurement.minimum; }
                    if (measurement.maximum >= highestValue) { highestValue = measurement.maximum; }

                    avg_sum += measurement.average;

                    addEntry(count, (float) measurement.maximum, 0);
                    addEntry(count, (float) measurement.average, 1);
                    addEntry(count, (float) measurement.minimum, 2);
                    count++;
                }

                averageValue = avg_sum / count;
                chart.notifyDataSetChanged();
                //chart.setVisibleXRangeMaximum(6);
                dateFormatter.setDates(times);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO: Tilpas legend https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/LineChartActivity2.java
            //chart.getLegend().setEnabled(false);

            // Please do not move these lines around, they have to be above the ui thread

            Objects.requireNonNull(self).runOnUiThread(() -> {


                String formatStr = getResources().getString(R.string.cloudDetailedInfo);
                textView.setText( String.format(formatStr, highestValue, averageValue, lowestValue) );

                chart.animateX(1500);
                chart.setVisibleXRangeMaximum(7);
                chart.moveViewToX(data.getEntryCount());
            });

        }).start();
    }

    private void addEntry(float xValue, float yValue, int index) {
        data = chart.getData();

        if (data == null) {
            data = new LineData();
            chart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(index);
        if (set == null) {
            if (index == 0) {
                set = createSet("Max", index);
            } else if (index == 1) {
                set = createSet("Average", index);
            } else {
                set = createSet("Min", index);
            }
            data.addDataSet(set);
            chart.notifyDataSetChanged();
        }

        data.addEntry(new Entry(xValue, yValue), index);
        data.notifyDataChanged();
    }

    private LineDataSet createSet(String name, int index) {
        LineDataSet set = new LineDataSet(null, name);
        set.setLineWidth(2.5f);
        //set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCircleRadius(4.5f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        if (index == 0) {
            set.setColor(Color.RED);
            set.setCircleColor(Color.RED);
        } else if (index == 2) {
            set.setColor(Color.GREEN);
            set.setCircleColor(Color.GREEN);
        }
        return set;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                loadDeviceData(deviceId, ATTCommunicator.MeasurementInterval.MonthPerDay);
                break;
            case 1:
                loadDeviceData(deviceId, ATTCommunicator.MeasurementInterval.WeekPerHour);
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    private class DateFormatter extends ValueFormatter {
        private ArrayList<Long> dates;
        private ATTCommunicator.MeasurementInterval measurementInterval = ATTCommunicator.MeasurementInterval.MonthPerDay;

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int index = (int)value;

            if (dates != null && dates.size() > index) {
                SimpleDateFormat simpleDateFormat;
                String formatted;
                switch (measurementInterval) {
                    case WeekPerHour:
                        simpleDateFormat = new SimpleDateFormat("hh:mm EEE", Locale.ENGLISH);
                        formatted = String.format("%s", simpleDateFormat.format(new Date(dates.get(index))));
                        formatted = formatted
                                .replace("Mon", "Man")
                                .replace("Tue", "Tir")
                                .replace("Wed", "Ons")
                                .replace("Thu", "Tor")
                                .replace("Fri", "Fre")
                                .replace("Sat", "Lør")
                                .replace("Sun", "Søn");

                        break;

                    case MonthPerDay:
                    default:
                        simpleDateFormat = new SimpleDateFormat("dd/MM", Locale.GERMAN);
                        formatted = simpleDateFormat.format(new Date(dates.get(index)));
                        break;
                }

                return formatted;
            }
            return "";
        }

        void setMeasurementInterval(ATTCommunicator.MeasurementInterval measurementInterval) {
            this.measurementInterval = measurementInterval;
        }
        void setDates(ArrayList<Long> dates) {
            this.dates = dates;
        }
    }
}
