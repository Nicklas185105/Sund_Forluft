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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.MainActivity;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CloudDetailedFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    LineChart chart;
    LineData data;
    String deviceId;

    private Spinner spinner;

    // TODO: Strings.xml
    private static final String[] paths = {
            "1 Måned [per dag]",
            "1 Uge [per time]"
    };

    ArrayList<Long> times;
    DateFormatter dateFormatter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cloud_detailed, container, false);
        dateFormatter = new DateFormatter();

        chart = view.findViewById(R.id.chart);

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        // TODO: Strings.xml
        chart.setNoDataText("Data is being loaded from cloud.. Please wait");
        chart.setPinchZoom(true);

        Bundle bundle = this.getArguments();
        deviceId = bundle.getString("deviceId");

        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_layout, paths);
        adapter.setDropDownViewResource(R.layout.spinner_layout_2);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        loadDeviceData(deviceId, ATTCommunicator.MeasurementInterval.MonthPerDay);

        // add some transparency to the color with "& 0x90FFFFFF"
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(dateFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

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
        Toast.makeText(getContext(), "Loading data from cloud", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            ATTCommunicator communicator = ATTCommunicator.getInstance();
            communicator.waitForLoad();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            ATTDevice device = communicator.getDeviceById(deviceId);
            ATTDeviceInfo deviceInfo = communicator.loadMeasurementsForDevice(device, cal.getTime(), interval);

            ATTDeviceInfoMeasurement[] measurements = deviceInfo.getMeasurements();
            Arrays.sort(measurements);

            times = new ArrayList<>();

            try {
                int count = 0;
                for (ATTDeviceInfoMeasurement measurement : deviceInfo.getMeasurements()) {
                    times.add(measurement.time.getTime());

                    addEntry(count, (float) measurement.maximum, 0);
                    addEntry(count, (float) measurement.average, 1);
                    addEntry(count, (float) measurement.minimum, 2);
                    count++;
                }

                chart.notifyDataSetChanged();
                chart.setVisibleXRangeMaximum(6);
                dateFormatter.setDates(times);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO: Tilpas legend https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/LineChartActivity2.java
            //chart.getLegend().setEnabled(false);

            self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chart.animateX(1500);
                    chart.setVisibleXRangeMaximum(7);
                    chart.moveViewToX(data.getEntryCount());
                }
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

        public void setMeasurementInterval(ATTCommunicator.MeasurementInterval measurementInterval) {
            this.measurementInterval = measurementInterval;
        }
        public void setDates(ArrayList<Long> dates) {
            this.dates = dates;
        }
    }
}
