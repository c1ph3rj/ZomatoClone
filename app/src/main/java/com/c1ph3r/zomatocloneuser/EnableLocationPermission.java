package com.c1ph3r.zomatocloneuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.c1ph3r.zomatocloneuser.databinding.ActivityEnableLocationPermissionBinding;

public class EnableLocationPermission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding the view with a variable to access the elements inside the layout without declaring.
        com.c1ph3r.zomatocloneuser.databinding.ActivityEnableLocationPermissionBinding ENABLE_LOCATION = ActivityEnableLocationPermissionBinding.inflate(getLayoutInflater());
        View view = ENABLE_LOCATION.getRoot();
        setContentView(view);

        try {
            // when the user click the enable location permission button it shows the detail need of location and ask for permission again.
            ENABLE_LOCATION.LocationPermission.setOnClickListener(LocationPermission -> {
                // Checks if the user allows the permission or Not.
                if (!(ContextCompat.checkSelfPermission(EnableLocationPermission.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                    dialogBoxForLocationPermission().show();
                } else {
                    startActivity(new Intent(EnableLocationPermission.this, Dashboard.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Dialog box for asking the location permission with the detailed information's.
    AlertDialog dialogBoxForLocationPermission() {
        try {
            return new AlertDialog.Builder(this)
                    .setTitle(R.string.LocationPermissionDialogTitle)
                    .setMessage(R.string.ReasonForLocationPermission)
                    .setPositiveButton(R.string.EnableLocationBtn_Text, (dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts(getString(R.string.package_Text), getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialogInterface.dismiss();
                    }).setCancelable(false)
                    .create();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }// End Of the dialogBoxForLocationPermission.

    // Check the permission is allowed or Not if allowed forward to Dashboard Activity.
    @Override
    protected void onResume() {
        super.onResume();
        if ((ContextCompat.checkSelfPermission(EnableLocationPermission.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            startActivity(new Intent(EnableLocationPermission.this, Dashboard.class));
        }
    }
}