package com.example.sundforluft.fragments.favorite;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.sundforluft.services.DataBroker.CsvDataBroker;
import com.example.sundforluft.services.FavoriteDetailedListviewAdapter;
import com.example.sundforluft.services.DataBroker.DataBroker;
import com.example.sundforluft.services.DataBroker.AirQualityDataModel;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class FavoriteDetailedFragment extends Fragment implements OnChartValueSelectedListener {

    private PieChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_detailed, container, false);

        chart = view.findViewById(R.id.chart1);
        //chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5,5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        //chart.setCenterTextTypeface(tfLight);
        //chart.setCenterText();

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);

        chart.setEntryLabelColor(Color.WHITE);
        //chart.setEntryLabelTypeface();
        chart.setEntryLabelTextSize(12f);

        DataBroker dataBroker = new CsvDataBroker( getResources() );

        List<AirQualityDataModel> modelsForSchoolA = new ArrayList<>();
        List<AirQualityDataModel> modelsForSchoolB = new ArrayList<>();

        if (dataBroker.load(LocalDateTime.parse("2019-08-19T11:37:28.264000"), LocalDateTime.parse("2019-08-20T11:30:07.899000"))) {
            modelsForSchoolA = dataBroker.getData();
        }

        if (dataBroker.load(LocalDateTime.parse("2019-08-20T13:01:11.318000"), LocalDateTime.parse("2019-08-20T13:32:35.421000"))) {
             modelsForSchoolB = dataBroker.getData();
        }


        setData(1, getAverage(modelsForSchoolA));
        setData(2, getAverage(modelsForSchoolB));

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
                    ATTDeviceInfo info = ATTCommunicator.getInstance().loadMeasurementsForDevice(classroomDevice, cal.getTime());
                    FavoriteDetailedListViewModel viewModel = new FavoriteDetailedListViewModel(self, classroomModel.name, info.getAverageQuality(), school.Id);
                    viewModel.setDeviceInfo(info);
                    classroomViewModels.add(viewModel);
                }
            }

            classroomViewModels.sort((s,o) ->  Double.compare(s.getAirQuality(), o.getAirQuality()));

            activity.runOnUiThread(() -> {
                for (FavoriteDetailedListViewModel classroomViewModel : classroomViewModels) {
                    favoriteDetailedListviewAdapter.addClassroom(classroomViewModel);
                }

                ListView schoolModelListView = view.findViewById(R.id.listView);
                schoolModelListView.setAdapter(favoriteDetailedListviewAdapter);
            });
        }).start();


        return view;
    }

    private float getAverage(List<AirQualityDataModel> models) {
        double total = 0;
        int count = 0;

        for (AirQualityDataModel model : models) {
            total += model.CO2;
            count++;
        }

        return (float)(total / count);
    }

    private void setData(int count, float range){
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        final String[] parties = new String[] {
                "Skole A", "Skole B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
                "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
                "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
                "Party Y", "Party Z"
        };

        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
                    parties[i % parties.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
