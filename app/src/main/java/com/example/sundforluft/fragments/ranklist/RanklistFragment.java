package com.example.sundforluft.fragments.ranklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.DAO.SchoolModelAverage;
import com.example.sundforluft.MainActivity;
import com.example.sundforluft.R;
import com.example.sundforluft.fragments.favorite.FavoriteDetailedFragment;
import com.example.sundforluft.services.RanklistFragmentListviewAdapter;
import com.example.sundforluft.services.SchoolAverageLoader;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class RanklistFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranklist, container, false);

        if (getActivity() != null) {
            ListView listView = Objects.requireNonNull(view.findViewById(R.id.listView));
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                Bundle bundle = new Bundle();
                bundle.putString("name", (parent.getAdapter().getItem(position).toString().split(" \\(")[0]));

                assert getFragmentManager() != null;
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
            RanklistFragmentListviewAdapter adapter = new RanklistFragmentListviewAdapter(this, avgList);
            listView.setAdapter(adapter);
        }

        // Set title of toolbar
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.menuRanklist);
        ((MainActivity) getActivity()).navigationView.setCheckedItem(R.id.nav_ranklist);

        MainActivity.toggle.setDrawerIndicatorEnabled(true);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        MainActivity.toggle.syncState();
        MainActivity.toolbar.setNavigationOnClickListener(v -> MainActivity.drawer.openDrawer(GravityCompat.START));

        return view;
    }
}
