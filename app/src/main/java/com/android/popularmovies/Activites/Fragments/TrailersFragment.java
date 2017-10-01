package com.android.popularmovies.Activites.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.popularmovies.Adapters.RecyclerViewAdapters.TrailerRecycleViewAdapter;
import com.android.popularmovies.Models.Trailer;
import com.android.popularmovies.R;
import com.android.popularmovies.Utils.JsonUtils;
import com.android.popularmovies.Utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static java.lang.reflect.Array.getInt;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrailersFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>>{

    View mView;
    @BindView(R.id.trailers_progress_bar)  ProgressBar mProgressBar;
    @BindView(R.id.trailers_recycler_view)  RecyclerView mRecyclerView;
    @BindView(R.id.trailers_no_content_text_view)  TextView mErrorTextView;
    private TrailerRecycleViewAdapter mTrailerRecyclerViewAdapter;
    int mMovieID;
    private static final int LOADER_TRAILERS_ID = 503;
    private Unbinder mUnbinder;


    public TrailersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        Bundle args = getArguments();
        mMovieID= args.getInt(getString(R.string.movie_id));
        mView = inflater.inflate(R.layout.fragment_trailers, container, false);

        mUnbinder = ButterKnife.bind(this, mView);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mTrailerRecyclerViewAdapter = new TrailerRecycleViewAdapter();

        mRecyclerView.setAdapter(mTrailerRecyclerViewAdapter);

        if(savedInstanceState != null){
            int focusID = savedInstanceState.getInt("focusID", View.NO_ID);

            View focusedChild = mView.findViewById(focusID);
            if (focusedChild != null)
            {
                focusedChild.requestFocus();
            }
        }

        if(checkForInternetConnection()){
            getActivity().getSupportLoaderManager().initLoader(LOADER_TRAILERS_ID, null, this);
        }

        return mView;
    }


    // HELPER METHODS
    //==============================================================================================

    private boolean checkForInternetConnection(){
        boolean isConnected = NetworkUtils.checkForNetworkConnection(getContext());
        if(!isConnected){
            showErrorMessage();
        }
        return isConnected;
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }




    // IMPLEMENTING ASYNCTASKLOADER
    //==============================================================================================
    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Trailer>>(getActivity()) {
            @Override
            protected void onStartLoading() {
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public ArrayList<Trailer> loadInBackground() {
                URL trailersURL = NetworkUtils.buildTrailersURI(mMovieID);
                try{
                    String jsonResult = NetworkUtils.getResponseFromHTTPUrl(trailersURL);
                    ArrayList<Trailer> movieTrailers = (ArrayList<Trailer>) JsonUtils.parseJsonData(jsonResult, getString(R.string.movie_trailers_json_result), getActivity(), null);
                    return movieTrailers;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(data == null){
            return;
        }
        mTrailerRecyclerViewAdapter.setTrailers(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

    }



    @Override
    public void onStop() {
        super.onStop();
        mTrailerRecyclerViewAdapter.releaseLoaders();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mTrailerRecyclerViewAdapter.releaseLoaders();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View focusedChild = getActivity().getCurrentFocus();

        if (focusedChild != null) {
            int focusID = focusedChild.getId();
            int cursorLoc = 0;

            if (focusedChild instanceof EditText) {
                cursorLoc = ((EditText) focusedChild).getSelectionStart();
            }

            outState.putInt("focusID", focusID);
            outState.putInt("cursorLoc", cursorLoc);
        }

    }
}
