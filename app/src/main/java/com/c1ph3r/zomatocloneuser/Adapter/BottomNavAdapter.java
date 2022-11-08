package com.c1ph3r.zomatocloneuser.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class BottomNavAdapter extends FragmentStateAdapter {
    boolean enabled;
    private final ArrayList<Fragment> listOfFragments = new ArrayList<>();

    public BottomNavAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return listOfFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return listOfFragments.size();
    }

    public void addFragment(Fragment fragment) {
        listOfFragments.add(fragment);
    }


}
