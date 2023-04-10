package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter2 extends FragmentStateAdapter {

    private final Bundle bundle;
    public ViewPagerAdapter2(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,  Bundle bundle) {
        super(fragmentManager, lifecycle);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(bundle);
                return detailsFragment;
            case 1:
                ArtistFragment artistFragment = new ArtistFragment();
                artistFragment.setArguments(bundle);
                return artistFragment;
            case 2:
                VenueFragment venueFragment = new VenueFragment();
                venueFragment.setArguments(bundle);
                return venueFragment;
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
