package com.c1ph3r.zomatocloneuser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c1ph3r.zomatocloneuser.Adapter.FavItems;
import com.c1ph3r.zomatocloneuser.Adapter.ListOfAllItems;

public class Home extends Fragment {
    RecyclerView FavItemsList, RestaurantList;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (view != null) {
            try {
                // Declaring and assigning the data to the recycle viewer. (Feature Items).
                FavItemsList = view.findViewById(R.id.FavItems);
                FavItems favItems = new FavItems(requireActivity());
                FavItemsList.setAdapter(favItems);
                FavItemsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

                // Declaring and assigning the data to the recycle viewer. (Restaurant List).
                RestaurantList = view.findViewById(R.id.listOfAllRestaurants);
                ListOfAllItems restaurantList = new ListOfAllItems(requireActivity());
                RestaurantList.setAdapter(restaurantList);
                RestaurantList.setLayoutManager(new LinearLayoutManager(requireActivity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }// End Of the OnCreateView.
}