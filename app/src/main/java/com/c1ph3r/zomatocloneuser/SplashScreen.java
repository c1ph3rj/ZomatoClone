package com.c1ph3r.zomatocloneuser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    private static final int LOADING_TIME = 6000;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        // Verifying if the user is logged in or not.
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            // If no forward to login page.
            intent = new Intent(this, MainActivity.class);
        else {
            // If yes forward to the Dashboard and fetch the user Location.
            getLocationPermission();
        }

        // Handler for splash screen.
        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }, LOADING_TIME);

    }// End Of the OnCreate Method.


    // Method to check if the user of the location permission and store the location if available.
    private void getLocationPermission() {
        try {
            // checks the permission is allowed or not.
            if (!(ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                // If no forward to Enable Location activity.
                intent = new Intent(SplashScreen.this, EnableLocationPermission.class);
            } else {
                // If yes store the location data to the user Db.
                FirebaseFirestore userDB = FirebaseFirestore.getInstance();
                DocumentReference user = userDB.collection(getString(R.string.userDataBase_Text))
                        .document(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber()));
                updateLocation(user);
                // and forwards to the dashboard.
                intent = new Intent(this, Dashboard.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// End of the getLocationPermission.

    // Method to update user Location to the fireStore.
    @SuppressLint("MissingPermission")
    private void updateLocation(DocumentReference user) {
        try {
            CancellationTokenSource ct = new CancellationTokenSource();
            ct.getToken();
            // Fetching the location using fusedLocation.getCurrentLocation Method.
            FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(SplashScreen.this);
            fusedLocation.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.getToken()).addOnSuccessListener(this, location -> {
                // updating the data to the fireStore if the values occurred.
                user.update(getString(R.string.address_Text), new GeoPoint(location.getLatitude(), location.getLongitude()));
            }).addOnFailureListener(Throwable::printStackTrace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// End of the updateLocation.

}