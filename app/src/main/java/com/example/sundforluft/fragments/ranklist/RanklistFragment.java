package com.example.sundforluft.fragments.ranklist;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.DAO.SchoolModelAverage;
import com.example.sundforluft.MainActivity;
import com.example.sundforluft.R;
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.DAO.ATTDevice;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfo;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfoMeasurement;
import com.example.sundforluft.fragments.favorite.FavoriteDetailedFragment;
import com.example.sundforluft.models.FavoriteDetailedListViewModel;
import com.example.sundforluft.services.AdminCloudsOverviewAdapter;
import com.example.sundforluft.services.FavoriteDetailedListviewAdapter;
import com.example.sundforluft.services.RanklisteListviewAdapter;
import com.example.sundforluft.services.SchoolAverageLoader;
import com.github.mikephil.charting.data.BarEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class RanklistFragment extends Fragment{
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranklist, container, false);

        if (getActivity() != null) {
            listView = Objects.requireNonNull(view.findViewById(R.id.listView));
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                Bundle bundle = new Bundle();
                bundle.putString("name", (parent.getAdapter().getItem(position).toString().split(" \\(")[0]));

                FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                FavoriteDetailedFragment detailedFragment = new FavoriteDetailedFragment();
                detailedFragment.setArguments(bundle);
                mFragmentTransaction
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fragment_container, detailedFragment)
                        .addToBackStack(null)
                        .commit();
                mFragmentTransaction.addToBackStack(null);
            });

            SchoolAverageLoader schoolAverages = SchoolAverageLoader.getInstance();
            SchoolModel[] schools = DataAccessLayer.getInstance().getSchools();
            SchoolModelAverage[] averages = new SchoolModelAverage[schools.length];

            for (int i = 0; i < averages.length; i++) {
                SchoolModelAverage average = new SchoolModelAverage(schools[i].Id, schools[i].Name);
                average.setAverage(schoolAverages.getCachedAverageBySchoolId(schools[i].Id));
                averages[i] = average;
            }

            Arrays.sort(averages);

            String[] avgList = Arrays.stream(averages)
                    .filter(e -> e.Id != 0)
                    .map(e -> e.Name + String.format(Locale.ENGLISH, " (%.2f)", e.getAverage())).toArray(String[]::new);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, avgList);
            listView.setAdapter(adapter);
        }

        return view;
    }
}
