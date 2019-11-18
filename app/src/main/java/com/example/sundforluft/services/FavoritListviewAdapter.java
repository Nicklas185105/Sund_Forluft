package com.example.sundforluft.services;


import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.FavoritFragment;
import com.example.sundforluft.R;
import com.example.sundforluft.models.FavoritListviewModel;

import java.util.ArrayList;

public class FavoritListviewAdapter extends BaseAdapter {
    private ArrayList<FavoritListviewModel> items;
    private final Fragment fragment;
    private final LayoutInflater inflater;

    public FavoritListviewAdapter(Fragment fragment) {
        items = new ArrayList<>();
        this.fragment = fragment;
        inflater = LayoutInflater.from(fragment.getActivity());
    }

    public void addSchool(FavoritListviewModel favoritListviewModel) {
        items.add(favoritListviewModel);
    }

    public void deleteSchool(int position) {
        //TODO: Delete from favorites (locally saved)
        items.remove(position);
    }

    public int getPosition(FavoritListviewModel inputModel) {
        for (int i = 0; i < items.size(); i++) {
            if (inputModel.getName().equals(items.get(i).getName())) { return i; }
        }
        return -1;
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
        // Create new object for activity listener
        convertView = inflater.inflate(R.layout.activity_favorit_listview, null);

        // Item to be displayed
        final FavoritListviewModel model = items.get(position);

        // Change item properties to model values.
        Button button = convertView.findViewById(R.id.button);
        String schoolModelText = model.getName() + "\n" + model.getAirQualityString();
        button.setText(schoolModelText);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*convertView.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new FavoritFragment()).commit();*/
            }
        });

        // Trigger delete on click
        Button closeButton = convertView.findViewById(R.id.removeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int position = getPosition(model);

                if (position >= 0) {
                    deleteSchool(position);
                    notifyDataSetChanged();
                }
            }
        });


        // Get quality from model
        switch (model.getAirQuality()) {
            case 1:
                Drawable redCircle = ResourcesCompat.getDrawable(fragment.getResources(), R.drawable.ic_listview_circle_red, null);
                convertView.findViewById(R.id.cicle).setBackground(redCircle);
                break;

            case 2:
                Drawable orangeCircle = ResourcesCompat.getDrawable(fragment.getResources(), R.drawable.ic_listview_circle_orange, null);
                convertView.findViewById(R.id.cicle).setBackground(orangeCircle);
                break;

            case 3:
            case 4:
                Drawable greenCircle = ResourcesCompat.getDrawable(fragment.getResources(), R.drawable.ic_listview_circle_green, null);
                convertView.findViewById(R.id.cicle).setBackground(greenCircle);
                break;
        }

        // return view for current row
        return convertView;
    }

}