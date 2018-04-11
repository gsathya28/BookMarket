package com.clairvoyance.bookmarket;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class MainFragment extends Fragment {


    public MainFragment(){

    }

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        // Load your data and update GUI

        ViewPager tabViewPager = view.findViewById(R.id.sell_buy_pager);
        tabViewPager.setAdapter(new TabPagerAdapter(getFragmentManager()));
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);

        return view;
    }

}
