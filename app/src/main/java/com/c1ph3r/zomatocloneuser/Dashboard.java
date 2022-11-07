package com.c1ph3r.zomatocloneuser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.c1ph3r.zomatocloneuser.Adapter.BottomNavAdapter;
import com.c1ph3r.zomatocloneuser.databinding.ActivityDashboardBinding;
import com.c1ph3r.zomatocloneuser.databinding.ActivityMainBinding;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.material.tabs.TabLayoutMediator;

import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Dashboard extends AppCompatActivity implements LocationListener {
    // Declaring a variable.
    private ActivityDashboardBinding DASHBOARD;
    Address address;

    // Dashboard.
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DASHBOARD = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = DASHBOARD.getRoot();
        setContentView(view);


        // Method to check if the user turn off the permission for location.
        getLocationPermissionCheck();
        // Method to initialize bottom navigation using tab layout.
        bottomNavigation();
        // Get user Location and display above in the Appbar.
        getUserLocation();


    }// End Of OnCreate.


    // Bottom Navigation Initialization.
    private void bottomNavigation() {
        String[] listOfNames = {getString(R.string.LayoutName_One), getString(R.string.LayoutName_Two)};
        int[] listOfImages = {R.drawable.delivery_ic, R.drawable.history_ic};
        // Adapter for Viewpager which holds Fragments.
        BottomNavAdapter bottomNavAdapter = new BottomNavAdapter(this);
        // Adding fragments to the adapter.
        bottomNavAdapter.addFragment(new Home());
        bottomNavAdapter.addFragment(new History());
        // Connecting viewPager with Tab layout.
        DASHBOARD.LayoutForFragments.setAdapter(bottomNavAdapter);
        new TabLayoutMediator(DASHBOARD.BottomNav, DASHBOARD.LayoutForFragments, (tab, position) -> {
            tab.setText(listOfNames[position]);
            tab.setIcon(AppCompatResources.getDrawable(Dashboard.this, listOfImages[position]));
        }).attach();
    }// End Of BottomNavigation.


    // Method to check app has location permission if not forward to EnableLocationPermission page.
    private void getLocationPermissionCheck() {
        if (!(ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            startActivity(new Intent(Dashboard.this, EnableLocationPermission.class));
        }
    }// End of getLocationPermissionCheck.


    // Method to Fetch user Location
    @SuppressLint("MissingPermission")
    void getUserLocation() {
        CancellationTokenSource ct = new CancellationTokenSource();
        ct.getToken();
        FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(Dashboard.this);
        fusedLocation.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.getToken()).addOnSuccessListener(this, location -> {
            Geocoder fetchAddress = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses= fetchAddress.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                address  = addresses.get(0);
                setDataToTheDashboard();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).addOnFailureListener(Throwable::printStackTrace);

    }// End of getUserLocation.

    private void setDataToTheDashboard() {
        DASHBOARD.Area.setText(address.getSubLocality());
        String city = address.getLocality() + ","+ address.getAdminArea() + ".";
        DASHBOARD.City.setText(city);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }
}