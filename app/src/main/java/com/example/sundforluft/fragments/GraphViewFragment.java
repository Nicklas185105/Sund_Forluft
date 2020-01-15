package com.example.sundforluft.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.R;
import com.example.sundforluft.services.DataBroker.CloudDataBroker;

import java.time.LocalDateTime;
import java.util.Objects;

public class GraphViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit_detailed, container, false);

        // Get access token & device id from intent.
        try {
            // Ensure that a NullPointerException is raised if intent is null.
            Intent intent = Objects.requireNonNull(getActivity()).getIntent();

            // Get the device name and access token from previous activity
            // There might be a lot of activities reffering to this one.. Such as Guest QR scan and the user page.
            // However: We must always provide a deviceName and accessToken when reffering to this view as it is used to load
            // data from the cloud
            String deviceName = intent.getStringExtra("deviceName");
            String accessToken = intent.getStringExtra("accessToken");

            // Use our cloud provider to retrieve data from cloud.
            CloudDataBroker dataBroker = new CloudDataBroker(deviceName, accessToken);
            if (dataBroker.load(LocalDateTime.parse("2019-08-20T13:01:11.318000"), LocalDateTime.parse("2019-08-20T13:32:35.421000"))) {

                //visualizeData();

            } else {
                // An error occured in trying to load data from cloud using given deviceName and accessToken.
                Toast.makeText(getContext(), "Endpoint could not be reached using this device id and access token!", Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException npe) {
            Toast.makeText(getContext(), "No data provided from previous activity.", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void visualizeData(CloudDataBroker broker) {
        // TODO: Visualize data here using dataBroker.getData
    }
}
