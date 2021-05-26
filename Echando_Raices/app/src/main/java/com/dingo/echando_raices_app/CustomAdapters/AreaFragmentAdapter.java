package com.dingo.echando_raices_app.CustomAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dingo.echando_raices_app.AddSpaceFragment;
import com.dingo.echando_raices_app.MySpacesFragment;

public class AreaFragmentAdapter extends FragmentStateAdapter {
    public AreaFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new AddSpaceFragment();
        }
        return new MySpacesFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
