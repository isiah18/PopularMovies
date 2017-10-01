package com.android.popularmovies.Activites.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.popularmovies.Activites.MovieDetailsActivity;
import com.android.popularmovies.Adapters.RecyclerViewAdapters.MovieRecycleViewAdapter;
import com.android.popularmovies.Models.Interfaces.RecyclerViewOnClickHandler;
import com.android.popularmovies.Models.Movie;
import com.android.popularmovies.R;
import com.android.popularmovies.Utils.EndlessRecycleViewScrollListener;
import com.android.popularmovies.Utils.JsonUtils;
import com.android.popularmovies.Utils.NetworkUtils;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.R.attr.orientation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainMoviesFragment extends Fragment implements RecyclerViewOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Movie>>, SharedPreferences.OnSharedPreferenceChangeListener{

    private View mView;
    private EndlessRecycleViewScrollListener mEndlessScrollListener;
    @BindView(R.id.popular_movies_recycler_view)  RecyclerView mRecyclerView;
    private MovieRecycleViewAdapter mMovieRecycleViewAdapter;
    @BindView(R.id.no_content_text_view)  TextView mErrorTextView;
    @BindView(R.id.progress_bar)  ProgressBar mProgressBar;
    private GridLayoutManager mGridLayoutManager;
    private static final int LOADER_ID = 769;
    private boolean firstPage = true;

    private static final String SORT_BY_CONST = "sort_by";
    private static final String POPULAR_CONST = "popular";
    private static final String PAGING_ID_CONST = "PAGING_ID";
    private static final String ALL_MOVIES_CONST ="all_movies";


    private Unbinder mUnbinder;

    public MainMoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);

        mView = inflater.inflate(R.layout.fragment_main_movies, container, false);
        mUnbinder = ButterKnife.bind(this, mView);

        mMovieRecycleViewAdapter = new MovieRecycleViewAdapter(this, getContext());
        mGridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayout.VERTICAL, false);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mMovieRecycleViewAdapter);
        mRecyclerView.setHasFixedSize(true);

        setUpEndlessScrollListener();

        // Register the listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if(savedInstanceState != null){
            ArrayList<Movie> movieList = Parcels.unwrap(savedInstanceState.getParcelable(getString(R.string.movie_list)));
            mMovieRecycleViewAdapter.setMovieData(movieList);
        }else{
            if(checkForInternetConnection()){
                getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            }
        }

        return mView;
    }


    //HELPER METHODS
    //==============================================================================================
    private void setUpEndlessScrollListener(){
        mEndlessScrollListener = new EndlessRecycleViewScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Bundle argsScrollListener = new Bundle();
                        argsScrollListener.putInt(getString(R.string.paging_name), page);
                        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, argsScrollListener, MainMoviesFragment.this);
                    }
                });
            }
        };

        mRecyclerView.addOnScrollListener(mEndlessScrollListener);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);

    }

    private void showRecyclerView(){
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    private boolean checkForInternetConnection(){
        boolean isConnected = NetworkUtils.checkForNetworkConnection(getContext());
        if(!isConnected){
            showErrorMessage();
        }
        return isConnected;
    }


    // ASYNC TASK INNER CLASS - To Fetch Movie Data From API
    //==============================================================================================

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(getContext()) {

            @Override
            protected void onStartLoading() {
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                ArrayList<Movie> movieList = null;

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String endPoint = sharedPreferences.getString(SORT_BY_CONST, POPULAR_CONST);

                Integer page;
                URL url;
                if(args != null){
                    page = args.getInt(PAGING_ID_CONST);
                    firstPage = false;
                    url = NetworkUtils.buildAllMoviesURI(endPoint, page + 1);
                }else{
                    firstPage = true;
                    url = NetworkUtils.buildAllMoviesURI(endPoint);
                }

                try{
                    String jsonString = NetworkUtils.getResponseFromHTTPUrl(url);
                    movieList = (ArrayList<Movie>) JsonUtils.parseJsonData(jsonString, ALL_MOVIES_CONST, getContext(), null);

                }catch (Exception e){
                    Log.e("Main Activity Async", e.toString());
                }
                return movieList;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if(firstPage){
            if(data != null){
                showRecyclerView();
                mMovieRecycleViewAdapter.setMovieData(data);
            }else{
                showErrorMessage();
            }
        }
        else{
            if(data!=null){
                mMovieRecycleViewAdapter.addToMovieDataSet(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }





    // IMPLEMENTING INTERFACE - Movie Adapter On Click Handler Interface
    //==============================================================================================
    @Override
    public void onClick(Object movie) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(getString(R.string.extra_movie_data), Parcels.wrap(movie));
        intent.putExtra(getString(R.string.intent_coming_from), getString(R.string.main_movies_fragment));
        startActivity(intent);
    }


    //IMPLEMENTING INTERFACE OnSharedPreferenceChangeListener Interface
    //==============================================================================================
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key == getString(R.string.sort_by_key)){
            mMovieRecycleViewAdapter.setMovieData(null);
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.movie_list),  Parcels.wrap(mMovieRecycleViewAdapter.getMovieData()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        mEndlessScrollListener.resetState();
        mUnbinder.unbind();
    }
}
