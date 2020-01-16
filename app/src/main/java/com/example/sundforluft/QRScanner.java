package com.example.sundforluft;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.DAO.ATTDevice;
import com.example.sundforluft.cloud.DAO.ATTDeviceInfo;
import com.example.sundforluft.fragments.CloudDetailedFragment;
import com.example.sundforluft.teacher.AddCloudActivity;
import com.example.sundforluft.services.Globals;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private TextView txtResult;
    Toolbar toolbar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        scannerView = findViewById(R.id.zxscan);
        txtResult = findViewById(R.id.txt_result);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String name = Globals.hasTeacherRights() ? "Lærer" : "Gæst";
        getSupportActionBar().setTitle(String.format("%s - QR Scanner", name));

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        QRScanner self = this;

        //Request permission
        Dexter.withActivity(self)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(self);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(self, "You need to accept permission", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    public void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }
    @Override
    public void handleResult(Result rawResult){
        // QR scan result.
        ZXingScannerView.ResultHandler resultSelf = this;
        //TODO check if device is valid for guest
        String deviceId = rawResult.getText();

        ATTCommunicator.getInstance().waitForLoad();
        ATTDevice foundDevice = ATTCommunicator.getInstance().getDeviceById(deviceId);

        if (foundDevice == null) {
            scannerView.resumeCameraPreview(resultSelf);
            // TODO: Invalid QR code toast!.
        } else {
            if (Globals.hasTeacherRights()) {
                // Go to cloud view.
                Intent i = new Intent(QRScanner.this, AddCloudActivity.class);
                i.putExtra("deviceId", deviceId);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else {
                Intent i = new Intent(QRScanner.this, CloudDetailedFragment.class);
                i.putExtra("deviceId", deviceId);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(QRScanner.this, StartActivity.class);
        intent.putExtra("animation", false);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}