package com.example.sundforluft.services;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sundforluft.models.SchoolModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SchoolBadgeAdapter extends BaseAdapter {
    private ArrayList<SchoolModel> items;
    private final Context context;

    public SchoolBadgeAdapter(Context context) {
        items = new ArrayList<>();
        this.context = context;
    }

    public void addSchool(SchoolModel schoolModel) {
        items.add(schoolModel);
    }

    public void deleteSchool(int position) {
        items.remove(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //convertView = LayoutInflater.from(context).inflate(, parent, false);
        }

        // Item to be displayed
        SchoolModel model = items.get(position);

        // Change item properties to model values.


        // return view for current row
        return convertView;
    }

}
