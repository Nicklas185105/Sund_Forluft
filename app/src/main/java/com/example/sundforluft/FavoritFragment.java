package com.example.sundforluft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.models.FavoritListviewModel;
import com.example.sundforluft.services.FavoritListviewAdapter;

public class FavoritFragment extends Fragment {

    FavoritListviewAdapter favoritListviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit, container, false);

        favoritListviewAdapter = new FavoritListviewAdapter(this);

        FavoritListviewModel[] favoritListviewModels = new FavoritListviewModel[] {
                new FavoritListviewModel(this, "Gentofte Skole", 12),
                new FavoritListviewModel(this, "Dyssegård Skole", 14),
                new FavoritListviewModel(this, "Ishøj Skole", 4),
                new FavoritListviewModel(this, "Hellerup Skole", 42),
        };
        for (FavoritListviewModel favoritListviewModel : favoritListviewModels) { favoritListviewAdapter.addSchool(favoritListviewModel); }

        ListView schoolModelListView = view.findViewById(R.id.listView);
        schoolModelListView.setAdapter(favoritListviewAdapter);

        return view;
    }
}