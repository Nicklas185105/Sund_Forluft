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

        ClassroomAxisFormatter xAxisFormatter = new ClassroomAxisFormatter();
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setEnabled(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);


        String schoolName = this.getArguments().getString("name");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(schoolName);

        FavoriteDetailedListviewAdapter favoriteDetailedListviewAdapter = new FavoriteDetailedListviewAdapter(this);
        SchoolModel school = DataAccessLayer.getInstance().getSchoolByName(schoolName);
        ArrayList<ClassroomModel> classrooms = DataAccessLayer.getInstance().getClassroomsBySchoolId( school.Id );

        final Activity activity = getActivity();
        final Fragment self = this;

        // TODO: Strings.xml
        Toast.makeText(getContext(), "Loading classroom averages from cloud", Toast.LENGTH_SHORT).show();
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
                    ATTDeviceInfo info = ATTCommunicator.getInstance().loadMeasurementsForDevice(classroomDevice, cal.getTime(), ATTCommunicator.MeasurementInterval.MonthPerDay);
                    FavoriteDetailedListViewModel viewModel = new FavoriteDetailedListViewModel(self, classroomModel.name, info.getAverageQuality(), school.Id);
                    viewModel.setDeviceInfo(info);
                    classroomViewModels.add(viewModel);
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

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*if (v.getId() == R.id.rankliste)
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.fragment_container, new RanklistFragment()).commit();
                    else{
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.fragment_container, new HelpFragment()).commit();
                    }*/
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
            set1 = new BarDataSet(values, "The year 2017");
            set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(context, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(context, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(context, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(context, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(context, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(context, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(context, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(context, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(context, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(context, android.R.color.holo_orange_dark);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new GradientColor(startColor1, endColor1));
            gradientColors.add(new GradientColor(startColor2, endColor2));
            gradientColors.add(new GradientColor(startColor3, endColor3));
            gradientColors.add(new GradientColor(startColor4, endColor4));
            gradientColors.add(new GradientColor(startColor5, endColor5));

            set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setHighlightEnabled(false);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

    private class ClassroomAxisFormatter extends ValueFormatter {
        private List<FavoriteDetailedListViewModel> viewModels;
        public void setVM(List<FavoriteDetailedListViewModel> viewModels) { this.viewModels = viewModels; }

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
