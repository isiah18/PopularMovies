package com.android.popularmovies.Adapters.ViewPagerAdapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.popularmovies.Activites.Fragments.ReviewsFragment;
import com.android.popularmovies.Activites.Fragments.TrailersFragment;
import com.android.popularmovies.R;

import org.parceler.Parcels;

/**
 * Created by Isingh930 on 9/18/17.
 */

public class RTPagerAdapter extends FragmentStatePagerAdapter {

    String[] fragmentTitles = new String[]{"Reviews", "Trailers"};
    private int mMovieID;
    private Context mContext;
//    private ReviewsFragment.RetrieveReviewsListener mReviewsListener;

//    public RTPagerAdapter(FragmentManager fm, int movieID, Context context, ReviewsFragment.RetrieveReviewsListener listener) {
//        super(fm);
//        mMovieID = movieID;
//        mContext = context;
//        mReviewsListener = listener;
//    }

    public RTPagerAdapter(FragmentManager fm, int movieID, Context context) {
        super(fm);
        mMovieID = movieID;
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles[position];
    }


    @Override
    public Fragment getItem(int position) {
        Bundle movieArgs = new Bundle();
        movieArgs.putInt(mContext.getString(R.string.movie_id), mMovieID);

        if(position == 0){
//            movieArgs.putParcelable(mContext.getString(R.string.reviews_retriever_listener), Parcels.wrap(mReviewsListener));
            ReviewsFragment reviewsFragment = new ReviewsFragment();
            reviewsFragment.setArguments(movieArgs);
            return reviewsFragment;
        }else{
            TrailersFragment trailersFragment = new TrailersFragment();
            trailersFragment.setArguments(movieArgs);
            return trailersFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
