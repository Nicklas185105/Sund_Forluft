package com.example.sundforluft.fragments.favorit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;

import com.example.sundforluft.MainActivity;
import com.example.sundforluft.R;
import com.example.sundforluft.models.FavoritListviewModel;
import com.example.sundforluft.services.FavoritListviewAdapter;
import com.example.sundforluft.services.Globals;

public class FavoritFragment extends Fragment {

    FavoritListviewAdapter favoritListviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit, container, false);

        favoritListviewAdapter = new FavoritListviewAdapter(this);
        favoritListviewAdapter.setClickListener(new FavoritListviewAdapter.ClickListener() {
            @Override
            public void onClick(FavoritListviewModel model, FavoritListviewAdapter.ClickListenerType type) {
                FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                switch (type) {
                    case School:
                        Bundle bundle = new Bundle();
                        bundle.putString("name", favoritListviewAdapter.getName(model));
                        FavoritDetailedFragment detailedFragment = new FavoritDetailedFragment();
                        detailedFragment.setArguments(bundle);
                        mFragmentTransaction
                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                .replace(R.id.fragment_container, detailedFragment)
                                .commit();
                        mFragmentTransaction.addToBackStack(null);

                        break;
                    case Close:
                        favoritListviewAdapter.deleteSchoolByModel(model);
                        break;
                }
            }
        });

        FavoritListviewModel[] favoritListviewModels = new FavoritListviewModel[] {
                new FavoritListviewModel(this, "Gentofte Skole", 12),
                new FavoritListviewModel(this, "Dyssegård Skole", 14),
                new FavoritListviewModel(this, "Ishøj Skole", 4),
                new FavoritListviewModel(this, "Hellerup Skole", 42),
        };
        for (FavoritListviewModel favoritListviewModel : favoritListviewModels) { favoritListviewAdapter.addSchool(favoritListviewModel); }

        ListView schoolModelListView = view.findViewById(R.id.listView);
        schoolModelListView.setAdapter(favoritListviewAdapter);

        // Set title of toolbar
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Favorit");

        return view;
    }
}