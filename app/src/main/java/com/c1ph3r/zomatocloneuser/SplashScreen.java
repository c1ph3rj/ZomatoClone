package com.c1ph3r.zomatocloneuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;

import com.c1ph3r.zomatocloneuser.FireBaseFireStore.DataBase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    private static final int LOADING_TIME = 6000;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            intent = new Intent(this, MainActivity.class);
        else {
            getLocationPermission();
        }

        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }, LOADING_TIME);



    }


    private void getLocationPermission() {
        if(!(ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            intent = new Intent(SplashScreen.this, EnableLocationPermission.class);
        }else{
            FirebaseFirestore userDB = FirebaseFirestore.getInstance();
            DocumentReference user = userDB.collection("userDataBase")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            updateLocation(user);

            intent = new Intent(this, Dashboard.class);
        }
    }

    @SuppressLint("MissingPermission")
    private void updateLocation(DocumentReference user) {
        CancellationTokenSource ct = new CancellationTokenSource();
        ct.getToken();
        FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(SplashScreen.this);
        fusedLocation.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.getToken()).addOnSuccessListener(this, location -> {
            user.update("address", new GeoPoint(location.getLatitude(), location.getLongitude()));
        }).addOnFailureListener(Throwable::printStackTrace);
    }

}