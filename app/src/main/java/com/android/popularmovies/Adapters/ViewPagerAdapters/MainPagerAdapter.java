package com.android.popularmovies.Adapters.ViewPagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.popularmovies.Activites.Fragments.FavoritesFragment;
import com.android.popularmovies.Activites.Fragments.MainMoviesFragment;

/**
 * Created by Isingh930 on 9/20/17.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    String[] titles = new String[]{"Movies", "Favorites"};

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            MainMoviesFragment mainMoviesFragment = new MainMoviesFragment();
            return mainMoviesFragment;
        }else {
            FavoritesFragment favoritesFragment = new FavoritesFragment();
            return favoritesFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


    @Override
    public int getCount() {
        return 2;
    }

}
