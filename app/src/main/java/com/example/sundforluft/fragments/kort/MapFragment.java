package com.example.sundforluft.fragments.kort;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.DAL.SchoolsLocator;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.R;
import com.example.sundforluft.services.CacheSchoolMananger;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_map, container, false);

        ListView listView = rootView.findViewById(R.id.listView);
        ArrayList<String> schools = new ArrayList<>();
        for (SchoolModel model : SchoolsLocator.getInstance().getSchools()) {
            if (model.Id != 0) {
                schools.add(model.Name);
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.custom_listview, schools);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //here you can use the position to determine what checkbox to check
                //this assumes that you have an array of your checkboxes as well. called checkbox
                String name = (String)adapter.getItem(position);
                CacheSchoolMananger.getInstance().addFavoriteSchool(
                    SchoolsLocator.getInstance().getSchoolByName(name).Id
                );

            }
        });

        return rootView;

    }
}
