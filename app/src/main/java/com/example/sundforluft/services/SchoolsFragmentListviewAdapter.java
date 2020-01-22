package com.example.sundforluft.services;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;

import java.util.ArrayList;

public class SchoolsFragmentListviewAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;

    public SchoolsFragmentListviewAdapter(Fragment fragment, ArrayList<String> schoolNames) {
        super(fragment.getContext(), 0, schoolNames);
        inflater = LayoutInflater.from(fragment.getActivity());
    }



    @SuppressLint({"ViewHolder", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = inflater.inflate(R.layout.schools_custom_listview, null);
        TextView text = view.findViewById(R.id.text1);
        text.setText( getItem(position));

        return view;
    }
}
