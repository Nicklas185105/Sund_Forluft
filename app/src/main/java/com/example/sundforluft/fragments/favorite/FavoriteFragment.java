package com.example.sundforluft.fragments.favorite;

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
import com.example.sundforluft.MainActivity;
import com.example.sundforluft.R;
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.models.FavoritListviewModel;
import com.example.sundforluft.services.CacheSchoolMananger;
import com.example.sundforluft.services.FavoritListviewAdapter;
import com.example.sundforluft.services.SchoolAverageLoader;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    FavoritListviewAdapter favoriteListviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);


        favoriteListviewAdapter = new FavoritListviewAdapter(this);
        favoriteListviewAdapter.setClickListener(new FavoritListviewAdapter.ClickListener() {
            @Override
            public void onClick(FavoritListviewModel model, FavoritListviewAdapter.ClickListenerType type) {
                FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                switch (type) {
                    case School:
                        Bundle bundle = new Bundle();
                        bundle.putString("name", favoriteListviewAdapter.getName(model));

                        FavoriteDetailedFragment detailedFragment = new FavoriteDetailedFragment();
                        detailedFragment.setArguments(bundle);
                        mFragmentTransaction
                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.fragment_container, detailedFragment)
                                .addToBackStack(null)
                                .commit();
                        mFragmentTransaction.addToBackStack(null);

                        break;
                    case Close:
                        favoriteListviewAdapter.deleteSchoolByModel(model);
                        CacheSchoolMananger.getInstance().removeFavoriteSchool(
                            DataAccessLayer.getInstance().getSchoolByName(model.getName()).Id
                        );
                        break;
                }
            }
        });

        ArrayList<FavoritListviewModel> viewModelList = new ArrayList<>();
        SchoolModel[] favoriteSchools = CacheSchoolMananger.getInstance().getFavoriteSchools();
        SchoolAverageLoader averageLoader = SchoolAverageLoader.getInstance();

        for (SchoolModel favoriteSchool : favoriteSchools) {
            viewModelList.add(new FavoritListviewModel(this, favoriteSchool.Name, (int)averageLoader.getCachedAverageBySchoolId(favoriteSchool.Id)));
        }
        FavoritListviewModel[] favoritListviewModels = new FavoritListviewModel[favoriteSchools.length];
        viewModelList.toArray(favoritListviewModels);


        for (FavoritListviewModel favoritListviewModel : favoritListviewModels) { favoriteListviewAdapter.addSchool(favoritListviewModel); }

        ListView schoolModelListView = view.findViewById(R.id.listView);
        schoolModelListView.setAdapter(favoriteListviewAdapter);

        // Set title of toolbar
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.menuFavorit);

        MainActivity.toggle.setDrawerIndicatorEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MainActivity.toggle.syncState();
        MainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.drawer.openDrawer(GravityCompat.START);
            }
        });

        return view;
    }
}