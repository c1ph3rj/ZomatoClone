package com.c1ph3r.zomatocloneuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.c1ph3r.zomatocloneuser.Adapter.BottomNavAdapter;
import com.c1ph3r.zomatocloneuser.databinding.ActivityDashboardBinding;
import com.c1ph3r.zomatocloneuser.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class Dashboard extends AppCompatActivity {
    // Declaring a variable.
    private ActivityDashboardBinding DASHBOARD;


    // Dashboard.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DASHBOARD = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = DASHBOARD.getRoot();
        setContentView(view);

        getLocationPermission();
        // Method to initialize bottom navigation using tab layout.
        bottomNavigation();




    }

    // Bottom Navigation Initialization.
    private void bottomNavigation() {
        String [] listOfNames = {getString(R.string.LayoutName_One), getString(R.string.LayoutName_Two)};
        int [] listOfImages = {R.drawable.delivery_ic, R.drawable.history_ic};
        // Adapter for Viewpager which holds Fragments.
        BottomNavAdapter bottomNavAdapter = new BottomNavAdapter(this);
        // Adding fragments to the adapter.
        bottomNavAdapter.addFragment(new Home());
        bottomNavAdapter.addFragment(new History());
        // Connecting viewPager with Tab layout.
        DASHBOARD.LayoutForFragments.setAdapter(bottomNavAdapter);
        new TabLayoutMediator(DASHBOARD.BottomNav,DASHBOARD.LayoutForFragments,(tab, position) -> {
            tab.setText(listOfNames[position]);
            tab.setIcon(AppCompatResources.getDrawable(Dashboard.this, listOfImages[position]));
        }).attach();
    }

    private void getLocationPermission() {
        if(!(ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}