package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingo.echando_raices_app.CustomAdapters.AreaFragmentAdapter;
import com.dingo.echando_raices_app.CustomAdapters.ForestationFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class ForestationContainerFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 pager2;
    ForestationFragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forestation_container, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.forestation_TabLayout);
        pager2 = (ViewPager2)view.findViewById(R.id.forestation_viewPager2);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        adapter = new ForestationFragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tree_solid).setText("Mis Forestaciones"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_add_tree).setText("Forestación"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return view;
    }
}