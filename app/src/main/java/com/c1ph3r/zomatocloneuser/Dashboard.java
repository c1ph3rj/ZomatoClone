package com.c1ph3r.zomatocloneuser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.c1ph3r.zomatocloneuser.Adapter.BottomNavAdapter;
import com.c1ph3r.zomatocloneuser.databinding.ActivityDashboardBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Dashboard extends AppCompatActivity implements LocationListener {
    Address address;
    DocumentReference user;
    String userID;
    // Declaring a variable.
    private ActivityDashboardBinding DASHBOARD;
    private FirebaseFirestore userDataBase;

    // Dashboard.
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DASHBOARD = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = DASHBOARD.getRoot();
        setContentView(view);
        userDataBase = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        user = userDataBase.collection("userDataBase").document(userID);


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
        DASHBOARD.LayoutForFragments.setUserInputEnabled(false);
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
        // To fetch the current location of the user.
        fusedLocation.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.getToken()).addOnSuccessListener(this, location -> {
            // converting the long and lat to the address.
            Geocoder fetchAddress = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            try {
                // Fetching the address from the response
                addresses = fetchAddress.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                address = addresses.get(0);
                // Setting the fetched data to the dashboard.
                setDataToTheDashboard();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).addOnFailureListener(Throwable::printStackTrace);

    }// End of getUserLocation.


    // Set the fetched location to the Dashboard.
    private void setDataToTheDashboard() {
        try {
            DASHBOARD.Area.setText(address.getSubLocality());
            String city = address.getLocality() + "," + address.getAdminArea() + ".";
            DASHBOARD.City.setText(city);
            updateLocation(address.getLatitude(), address.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // End Of the setDataToTheDashBoard.


    // Method to update users Live Location.
    public void updateLocation(double lat, double lon) {
        user.update(getString(R.string.address_Text), new GeoPoint(lat, lon)).isSuccessful();
    }

    // Overriding onBackPressed.
    @Override
    public void onBackPressed() {
        // On back pressed ask the user to exit if the viewpager was on Home screen.
        if(DASHBOARD.LayoutForFragments.getCurrentItem() == 0){
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.Zomato_Text).setMessage(R.string.Exit_Text)
                    .setPositiveButton(R.string.Yes_Text, (dialogInterface, i) -> finishAffinity())
                    .setNegativeButton(R.string.No_Text, (dialogInterface, i) -> dialogInterface.cancel());
            dialog.show();
        }else
            // If the viewpager not in Home page set the view pager current item to home page.
            DASHBOARD.LayoutForFragments.setCurrentItem(0);

    }// End of onBackPressed.

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }
}