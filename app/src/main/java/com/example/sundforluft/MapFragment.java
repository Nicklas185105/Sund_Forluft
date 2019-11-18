package com.example.sundforluft;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    MapView mMapView;
    private GoogleMap mMap;
    private Geocoder mGeo;
    private HashMap<String, LatLng> addresses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());

        } catch (   Exception e){
            e.printStackTrace();
        }

        mGeo = new Geocoder(this.getContext());
        addresses = new HashMap<>();
        addresses.put("DTU Lyngby", new LatLng(55.7855742,12.521381));


        mMapView.getMapAsync(googleMap -> {
            mMap = googleMap;

            addresses.forEach((k,v)-> {

                googleMap.addMarker(new MarkerOptions().position(v).title(k).snippet("Description"));
            });




            /* Should request user's location as a LatLng, DTU Lyngby temp value */
            //LatLng currentLoc = new LatLng(addresses.get("DTU Lyngby").getLatitude(), addresses.get("DTU Lyngby").getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(addresses.values().iterator().next()).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });
        return rootView;

    }
}
