package com.c1ph3r.zomatocloneuser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c1ph3r.zomatocloneuser.Adapter.FavItems;

public class Home extends Fragment {

    public Home() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if(view != null){
            RecyclerView FavItemsList = view.findViewById(R.id.FavItems);
            FavItems favItems = new FavItems(requireActivity());
            FavItemsList.setAdapter(favItems);
            FavItemsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        }

        return view;
    }
}