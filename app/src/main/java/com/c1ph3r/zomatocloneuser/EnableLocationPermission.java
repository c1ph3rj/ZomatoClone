package com.c1ph3r.zomatocloneuser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.c1ph3r.zomatocloneuser.databinding.ActivityEnableLocationPermissionBinding;

public class EnableLocationPermission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.c1ph3r.zomatocloneuser.databinding.ActivityEnableLocationPermissionBinding ENABLE_LOCATION = ActivityEnableLocationPermissionBinding.inflate(getLayoutInflater());
        View view = ENABLE_LOCATION.getRoot();
        setContentView(view);

        ENABLE_LOCATION.LocationPermission.setOnClickListener(LocationPermission -> {
            System.out.println("clicked");
            if(!(ContextCompat.checkSelfPermission(EnableLocationPermission.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
               dialogBoxForLocationPermission().show();
            }else {
                startActivity(new Intent(EnableLocationPermission.this, Dashboard.class));
            }
        });
    }


    AlertDialog dialogBoxForLocationPermission(){
        return new AlertDialog.Builder(this)
                .setTitle("Location Permission Required!")
                .setMessage("Please enable device location to ensure accurate address and fast delivery ")
                .setPositiveButton("Enable location permission", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialogInterface.dismiss();
                }).setCancelable(false)
                .create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if((ContextCompat.checkSelfPermission(EnableLocationPermission.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            startActivity(new Intent(EnableLocationPermission.this, Dashboard.class));
        }
    }
}