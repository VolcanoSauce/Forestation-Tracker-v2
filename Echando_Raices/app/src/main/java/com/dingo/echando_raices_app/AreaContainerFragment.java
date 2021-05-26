package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingo.echando_raices_app.CustomAdapters.AreaFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class AreaContainerFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 pager2;
    AreaFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_area_container, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.area_TabLayout);
        pager2 = (ViewPager2)view.findViewById(R.id.area_viewPager2);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        adapter = new AreaFragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_my_spaces).setText("Mis Espacios"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_add_space).setText("Agregar Espacio"));

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