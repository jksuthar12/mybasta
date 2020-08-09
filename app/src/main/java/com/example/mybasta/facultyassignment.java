package com.example.mybasta;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class facultyassignment extends Fragment {


    public facultyassignment() {
        // Required empty public constructor
    }
        TabLayout tabLayout;
    ViewPager viewPager;
    fragmentadapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_facultyassignment, container, false);
        tabLayout=view.findViewById(R.id.tablayout);
        viewPager=view.findViewById(R.id.view);
        adapter=new fragmentadapter(getFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        return view;
    }

}
