package com.example.sundforluft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.models.SchoolModel;
import com.example.sundforluft.services.SchoolBadgeAdapter;

public class FavoritFragment extends Fragment {

    SchoolBadgeAdapter schoolBadgeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit, container, false);

        schoolBadgeAdapter = new SchoolBadgeAdapter(this);

        SchoolModel[] schoolModels = new SchoolModel[] {
                new SchoolModel(this, "Gentofte Skole", 12),
                new SchoolModel(this, "Dyssegård Skole", 14),
                new SchoolModel(this, "Ishøj Skole", 4),
                new SchoolModel(this, "Hellerup Skole", 42),
        };
        for (SchoolModel schoolModel : schoolModels) { schoolBadgeAdapter.addSchool(schoolModel); }

        ListView schoolModelListView = view.findViewById(R.id.listView);
        schoolModelListView.setAdapter(schoolBadgeAdapter);

        return view;
    }
}