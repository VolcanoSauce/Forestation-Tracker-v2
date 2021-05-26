package com.dingo.echando_raices_app.CustomAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dingo.echando_raices_app.AddAreaFragment;
import com.dingo.echando_raices_app.MyAreasFragment;

public class AreaFragmentAdapter extends FragmentStateAdapter {
    public AreaFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new AddAreaFragment();
        }
        return new MyAreasFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
