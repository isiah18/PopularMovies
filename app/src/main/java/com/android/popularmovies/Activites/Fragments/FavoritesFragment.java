package com.android.popularmovies.Activites.Fragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.popularmovies.Activites.MainActivity;
import com.android.popularmovies.Activites.MovieDetailsActivity;
import com.android.popularmovies.Adapters.RecyclerViewAdapters.FavoriteRecycleViewAdapter;
import com.android.popularmovies.Data.MovieContract;
import com.android.popularmovies.Models.Interfaces.RecyclerViewOnClickHandler;
import com.android.popularmovies.R;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RecyclerViewOnClickHandler{

    private View mView;

    @BindView(R.id.favorite_movieprogress_bar)  ProgressBar mProgressBar;
    @BindView(R.id.favorite_movie_no_content_text_view)  TextView mErrorTextView;
    @BindView(R.id.favorite_movie_recycler_view)  RecyclerView mRecyclerView;
    private FavoriteRecycleViewAdapter mRecycleViewAdapter;
    private GridLayoutManager mGridLayoutManager;
    private static final int LOADER_ID = 189;
    private Unbinder mUnbinder;

    public FavoritesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        mView = inflater.inflate(R.layout.fragment_favorites, container, false);
        mUnbinder = ButterKnife.bind(this, mView);

        mRecycleViewAdapter = new FavoriteRecycleViewAdapter(getContext(), this);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mGridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayout.VERTICAL, false);
        }
        else{
            mGridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayout.VERTICAL, false);
        }


        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mRecycleViewAdapter);

        mRecyclerView.setHasFixedSize(true);

        setUpSwipeToDelete();

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return mView;
    }

    private void setUpSwipeToDelete(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                int id = (int)viewHolder.itemView.getTag();

                Uri uri = MovieContract.FavoriteMovieEntry.FAVORITE_MOVIE_URI.buildUpon().appendPath(String.valueOf(id)).build();

                int numRowsDeleted = getContext().getContentResolver().delete(uri, null, null);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, FavoritesFragment.this);

            }
        }).attachToRecyclerView(mRecyclerView);
    }



    // IMPLEMENTING LOADER
    //==============================================================================================
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getContext()) {

            Cursor mFavMovieCursor = null;

            @Override
            protected void onStartLoading() {
                mErrorTextView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                if (mFavMovieCursor != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mFavMovieCursor);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getActivity().getContentResolver().query(MovieContract.FavoriteMovieEntry.FAVORITE_MOVIE_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e("FavoritesFragment", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mFavMovieCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(data != null){
            mRecycleViewAdapter.swapCursor(data);
        }else{
            mErrorTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecycleViewAdapter.swapCursor(null);
    }




    // IMPLEMENTING INTERFACE - Movie Adapter On Click Handler Interface
    //==============================================================================================
    @Override
    public void onClick(Object movie) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(getString(R.string.extra_movie_data), Parcels.wrap(movie));
        intent.putExtra(getString(R.string.intent_coming_from), getString(R.string.favorite_fragment));
        startActivity(intent);
    }


    // ON RESUME
    //==============================================================================================
    @Override
    public void onResume() {
        super.onResume();
        // re-queries for all tasks
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
