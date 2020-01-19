package com.example.sundforluft.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.R;
import com.example.sundforluft.fragments.CloudDetailedFragment;
import com.example.sundforluft.models.FavoriteDetailedListViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public class FavoriteDetailedListviewAdapter extends BaseAdapter {

    private ArrayList<FavoriteDetailedListViewModel> items;
    private final Fragment fragment;
    private final LayoutInflater inflater;

    public FavoriteDetailedListviewAdapter(Fragment fragment) {
        items = new ArrayList<>();
        this.fragment = fragment;
        inflater = LayoutInflater.from(fragment.getActivity());
    }

    public void addClassroom(FavoriteDetailedListViewModel favoriteDetailedListviewModel) {
        items.add(favoriteDetailedListviewModel);
    }

    public int getPosition(FavoriteDetailedListViewModel inputModel) {
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
        convertView = inflater.inflate(R.layout.activity_favorite_detailed_listview, null);

        // Item to be displayed
        final FavoriteDetailedListViewModel model = items.get(position);

        // Change item properties to model values.
        Button button = convertView.findViewById(R.id.button);
        String schoolModelText = model.getName();
        button.setText(schoolModelText);
        TextView text = convertView.findViewById(R.id.text);
        String modelText = String.format(Locale.ENGLISH, "%.2f", model.getAirQuality());
        text.setText(modelText);

        button.setOnClickListener(v -> {
            String buttonText = ((AppCompatButton) v).getText().toString();
            int schoolId = model.getSchoolId();

            Optional<ClassroomModel> classroom = DataAccessLayer.getInstance().getClassroomsBySchoolId(schoolId)
                    .stream().filter(c -> c.name.equals(buttonText)).findFirst();

            if (classroom.isPresent()) {
                FragmentTransaction mFragmentTransaction = fragment.getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("deviceId", classroom.get().deviceId);

                CloudDetailedFragment cloudDetailedFragment = new CloudDetailedFragment();
                cloudDetailedFragment.setArguments(bundle);
                mFragmentTransaction
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fragment_container, cloudDetailedFragment)
                        .addToBackStack(null)
                        .commit();
                mFragmentTransaction.addToBackStack(null);
            }
        });

        // Get quality from model
        /*switch (model.getAirQualityType()) {
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
        }*/

        // return view for current row
        return convertView;
    }
}
