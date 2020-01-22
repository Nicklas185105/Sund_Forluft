package com.example.sundforluft.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.DAO.SchoolModelAverage;
import com.example.sundforluft.R;
import com.example.sundforluft.models.RanklisteListviewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RanklistFragmentListviewAdapter extends ArrayAdapter {

    LayoutInflater inflater;


    public RanklistFragmentListviewAdapter(Fragment fragment, String[] averages) {
        super(fragment.getContext(), 0, averages);
        this.inflater = LayoutInflater.from(fragment.getActivity());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = inflater.inflate(R.layout.rankliste_custom_listview, null);
        TextView text = view.findViewById(R.id.text1);
        text.setText((String) getItem(position));



        return view;
    }
}
