package com.example.sundforluft.services;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;

import java.util.ArrayList;

public class SchoolsFragmentListviewAdapter extends BaseAdapter implements Filterable {

    private ArrayList<String> schoolNames;
    private Fragment fragment;
    private LayoutInflater inflater;

    public SchoolsFragmentListviewAdapter(Fragment fragment, ArrayList<String> schoolNames) {
            this.schoolNames = schoolNames;
            this.fragment = fragment;
            inflater = LayoutInflater.from(fragment.getActivity());
    }

    @Override
    public int getCount() {
        return schoolNames.size();
    }

    @Override
    public String getItem(int position) {
        return schoolNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.custom_listview, parent, false);



        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<String> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0){
                    results.values = schoolNames;
                    results.count = schoolNames.size();
                } else {
                    for (String schoolName : schoolNames){
                        if (schoolName.toLowerCase().contains(constraint.toString())){
                            filteredList.add(schoolName);
                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
