package com.example.sundforluft.fragments.favorite;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.MainActivity;
import com.example.sundforluft.R;

import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.DAO.ATTDevice;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfo;
import com.example.sundforluft.models.FavoriteDetailedListViewModel;
import com.example.sundforluft.services.FavoriteDetailedListviewAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FavoriteDetailedFragment extends Fragment {

    private BarChart chart;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_detailed, container, false);

        context = getContext();

        chart = view.findViewById(R.id.chart1);
        //chart.setUsePercentValues(true);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);

        //chart.setOnChartValueSelectedListener(this);

        ClassroomAxisFormatter xAxisFormatter = new ClassroomAxisFormatter();
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextColor(R.color.textColor);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setTextColor(R.color.textColor);
        leftAxis.setAxisMinimum(380);

        chart.getAxisRight().setEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setForm(Legend.LegendForm.EMPTY);
        l.setTextColor(getResources().getColor(R.color.textColor));
        l.setEnabled(true);

        String schoolName = Objects.requireNonNull(this.getArguments()).getString("name");
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(schoolName);

        FavoriteDetailedListviewAdapter favoriteDetailedListviewAdapter = new FavoriteDetailedListviewAdapter(this);
        SchoolModel school = DataAccessLayer.getInstance().getSchoolByName(schoolName);
        ArrayList<ClassroomModel> classrooms = DataAccessLayer.getInstance().getClassroomsBySchoolId( school.Id );

        final Activity activity = getActivity();
        final Fragment self = this;

        Toast.makeText(getContext(), getResources().getText(R.string.classroom_avg_load), Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            ATTCommunicator.getInstance().waitForLoad();
            ArrayList<ATTDevice> devices = ATTCommunicator.getInstance().getDevices();

            List<FavoriteDetailedListViewModel> classroomViewModels = new ArrayList<>();
            for (ClassroomModel classroomModel : classrooms) {
                Optional<ATTDevice> classroomDeviceOptional = devices.stream().filter(c -> c.deviceId.equals(classroomModel.deviceId)).findAny();
                if (classroomDeviceOptional.isPresent()) {
                    ATTDevice classroomDevice = classroomDeviceOptional.get();
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, -1);
                    ATTCommunicator communicator = ATTCommunicator.getInstance();
                    communicator.waitForLoad();
                    ATTDeviceInfo info = communicator.loadMeasurementsForDevice(classroomDevice, cal.getTime(), ATTCommunicator.MeasurementInterval.MonthPerDay);

                    if (info != null) {
                        FavoriteDetailedListViewModel viewModel = new FavoriteDetailedListViewModel(classroomModel.name, info.getAverageQuality(), school.Id);
                        viewModel.setDeviceInfo();
                        classroomViewModels.add(viewModel);
                    }
                }
            }

            classroomViewModels.sort((s,o) ->  Double.compare(s.getAirQuality(), o.getAirQuality()));

            activity.runOnUiThread(() -> {
                ArrayList<BarEntry> values = new ArrayList<>();
                for (int i = 0; i < 5 && i < classroomViewModels.size(); i++) {
                    float val = (float)classroomViewModels.get(i).getAirQuality();
                    values.add(new BarEntry(i, val));
                }
                xAxisFormatter.setVM( classroomViewModels );
                setData(values);
                chart.notifyDataSetChanged();
                chart.invalidate();


                for (FavoriteDetailedListViewModel classroomViewModel : classroomViewModels) {
                    favoriteDetailedListviewAdapter.addClassroom(classroomViewModel);
                }

                ListView schoolModelListView = view.findViewById(R.id.listView);
                schoolModelListView.setAdapter(favoriteDetailedListviewAdapter);
            });
        }).start();


        //chart.setMinimumHeight(380);

        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        MainActivity.toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void setData(ArrayList<BarEntry> values){
        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "Co2 (PPM)");
            set1.setDrawIcons(false);

            List<GradientColor> gradientColors = new ArrayList<>();
            for (BarEntry entry : values) {
                gradientColors.add(getColorForBar(entry));
            }
            set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setHighlightEnabled(false);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            data.setValueTextColor(R.color.textColor);

            chart.setData(data);
        }
    }

    private GradientColor getColorForBar(BarEntry entry) {
        if (entry == null) {//empty
            return new GradientColor(
                    ContextCompat.getColor(context, android.R.color.holo_green_light),
                    ContextCompat.getColor(context, android.R.color.holo_green_light));
        }


        if (entry.getY() <= 450) {
            // Fremragende
            return new GradientColor(
                    ContextCompat.getColor(context, android.R.color.holo_green_light),
                    ContextCompat.getColor(context, android.R.color.holo_green_dark));
        }

        if (entry.getY() < 600) {
            // God
            return new GradientColor(
                    ContextCompat.getColor(context, android.R.color.holo_green_dark),
                    ContextCompat.getColor(context, android.R.color.holo_orange_light));

        }

        if (entry.getY() < 800) {
            // Medium
            return new GradientColor(
                    ContextCompat.getColor(context, android.R.color.holo_orange_dark),
                    ContextCompat.getColor(context, android.R.color.holo_red_light));
        }

        // Dårlig
        return new GradientColor(
                ContextCompat.getColor(context, android.R.color.holo_red_light),
                ContextCompat.getColor(context, android.R.color.holo_red_dark));
    }

    private class ClassroomAxisFormatter extends ValueFormatter {
        private List<FavoriteDetailedListViewModel> viewModels;
        void setVM(List<FavoriteDetailedListViewModel> viewModels) { this.viewModels = viewModels; }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int index = (int)value;

            if (viewModels != null && viewModels.size() > index) {
                return viewModels.get(index).getName();
            }
            return "";
        }
    }
}
