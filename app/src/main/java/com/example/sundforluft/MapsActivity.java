package com.example.sundforluft;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder mGeocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mGeocoder = new Geocoder(this);



        //List of address strings to be populated by a database call
        //  Could possibly become a HashMap<String,Address>
        List<String> addressStringList = new LinkedList<>();
        addressStringList.add("DTU Ballerup");
        addressStringList.add("DTU Lyngby");
        addressStringList.add("Virum Skole");

        //List of Address objects after Geocoder has processed an address string
        //  Could possibly become a HashMap<String,Address>
        List<Address> addressList = new LinkedList<>();

        for (String addressString : addressStringList){
            try {
                Address tempAddress = mGeocoder.getFromLocationName(addressString,1).get(0);
                addressList.add(tempAddress);
            } catch (IOException e) {
                continue;
            }
        }

        int addressIndex = 0;
        for (Address address : addressList){
            LatLng location = new LatLng(address.getLatitude(),address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(addressStringList.get(addressIndex)));
            addressIndex++;
        }

        LatLng initLatLng = new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initLatLng,15));

    }
}
