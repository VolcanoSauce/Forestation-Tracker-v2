package com.dingo.echando_raices_app.CustomAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dingo.echando_raices_app.AddForestationFragment;
import com.dingo.echando_raices_app.MyForestationsFragment;

public class ForestationFragmentAdapter extends FragmentStateAdapter {
    public ForestationFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new AddForestationFragment();
        }
        return new MyForestationsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
