package com.dingo.echando_raices_app.CustomAdapters;

import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dingo.echando_raices_app.AddAreaFragment;
import com.dingo.echando_raices_app.MyAreasFragment;
import com.dingo.echando_raices_app.PasswordFragment;
import com.dingo.echando_raices_app.ProfileFragment;

public class ProfilePasswordFragmentAdapter extends FragmentStateAdapter {
    public ProfilePasswordFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new PasswordFragment();
        }
        return new ProfileFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
