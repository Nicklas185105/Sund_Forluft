package com.example.sundforluft.fragments.ranklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;
import com.example.sundforluft.models.RanklisteListviewModel;
import com.example.sundforluft.services.RanklisteListviewAdapter;

public class RanklistNorthJutlandFragment extends Fragment {

    TextView textView;
    RanklisteListviewAdapter ranklisteListviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranklist_ranking, container, false);

        /*// Arrow Click
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        textView = view.findViewById(R.id.textView);
        textView.setText(R.string.northjutland);

        ranklisteListviewAdapter = new RanklisteListviewAdapter(this);

        RanklisteListviewModel[] favoritListviewModels = new RanklisteListviewModel[] {
                new RanklisteListviewModel(this, "Løgstør Skole", 12),
                new RanklisteListviewModel(this, "Farsø Skole", 14),
                new RanklisteListviewModel(this, "Vesterkærets Skole", 4),
                new RanklisteListviewModel(this, "Gammel Hasseris Skole", 42),
        };
        for (RanklisteListviewModel favoritListviewModel : favoritListviewModels) { ranklisteListviewAdapter.addSchool(favoritListviewModel); }

        ListView schoolModelListView = view.findViewById(R.id.listView);
        schoolModelListView.setAdapter(ranklisteListviewAdapter);

        return view;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, new RanklistFragment()).commit();
        }

        return super.onOptionsItemSelected(item);
    }*/
}
