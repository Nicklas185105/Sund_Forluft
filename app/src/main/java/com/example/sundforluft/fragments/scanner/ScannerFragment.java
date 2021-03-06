package com.example.sundforluft.fragments.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.MainActivity;
import com.example.sundforluft.R;
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.DAO.ATTDevice;
import com.example.sundforluft.fragments.CloudDetailedFragment;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;

    private String lastScannedQR  = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);


        scannerView = view.findViewById(R.id.zxscan);

        ScannerFragment self = this;

        //Request permission
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(self);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getActivity(), R.string.acceptPermission, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        // Set title of toolbar
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.menuScanner);
        ((MainActivity) getActivity()).navigationView.setCheckedItem(R.id.nav_scanner);

        return view;
    }

    @Override
    public void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }
    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void handleResult(Result rawResult){
        // QR scan result.
        String deviceId = rawResult.getText();
        ZXingScannerView.ResultHandler resultSelf = this;

        Vibrator v = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Objects.requireNonNull(v).vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            Objects.requireNonNull(v).vibrate(500);
        }
        if (lastScannedQR.equals(deviceId)) {
            scannerView.resumeCameraPreview(resultSelf);
            return;
        }
        lastScannedQR = deviceId;

        ATTCommunicator.getInstance().waitForLoad();
        ATTDevice foundDevice = ATTCommunicator.getInstance().getDeviceById(deviceId);

        if (foundDevice == null) {
            scannerView.resumeCameraPreview(resultSelf);

            Toast.makeText(getContext(), getResources().getText(R.string.invalid_qr_code), Toast.LENGTH_SHORT).show();

        } else {

            Fragment fragment = new CloudDetailedFragment();
            Bundle bundle = new Bundle();
            bundle.putString("deviceId", deviceId);
            fragment.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container, fragment).commit();
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

}