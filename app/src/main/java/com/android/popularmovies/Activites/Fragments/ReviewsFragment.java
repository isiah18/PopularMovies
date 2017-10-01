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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.popularmovies.Adapters.RecyclerViewAdapters.ReviewRecycleViewAdapter;
import com.android.popularmovies.Models.Review;
import com.android.popularmovies.R;
import com.android.popularmovies.Utils.JsonUtils;
import com.android.popularmovies.Utils.NetworkUtils;
import com.borjabravo.readmoretextview.ReadMoreTextView;

import org.parceler.Parcels;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.os.Build.VERSION_CODES.M;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Review>>{

    View mView;
    @BindView(R.id.reviews_progress_bar)  ProgressBar mProgressBar;
    @BindView(R.id.reviews_recycler_view)  RecyclerView mRecyclerView;
    @BindView(R.id.reviews_no_content_text_view) TextView mErrorTextView;
    private ReviewRecycleViewAdapter mReviewRecyclerViewAdapter;
    private int mMovieID;
    private static final int LOADER_REVIEWS_ID = 118;
    private Unbinder mUnbinder;

    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_reviews, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        setRetainInstance(true);

        Bundle args = getArguments();
        mMovieID= args.getInt(getString(R.string.movie_id));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mReviewRecyclerViewAdapter = new ReviewRecycleViewAdapter();
        mRecyclerView.setAdapter(mReviewRecyclerViewAdapter);

        if(checkForInternetConnection()){
            getActivity().getSupportLoaderManager().initLoader(LOADER_REVIEWS_ID, null, this);
        }

        return mView;
    }


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

    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Review>>(getActivity()) {
            @Override
            protected void onStartLoading() {
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public ArrayList<Review> loadInBackground() {
                URL reviewsURL = NetworkUtils.buildReviewsURI(mMovieID);

                try{
                    String jsonResult = NetworkUtils.getResponseFromHTTPUrl(reviewsURL);
                    ArrayList<Review> movieReviews = (ArrayList<Review>) JsonUtils.parseJsonData(jsonResult, getString(R.string.movie_reviews_json_result), getActivity(), null);
                    return movieReviews;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(data == null){
            return;
        }
        mReviewRecyclerViewAdapter.setReviews(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}







// private RetrieveReviewsListener mReviewsListener;

// INTERFACE
//==============================================================================================
//    public interface RetrieveReviewsListener{
//        void onRetrieveReviews(ArrayList<Review> reviews, int movieID);
//    }



//    IN ON CREATE
//    mReviewsListener = Parcels.unwrap(args.getParcelable(getString(R.string.reviews_retriever_listener)));



// IN LOAD IN BACKGROUND
// URL reviewsURL = NetworkUtils.buildReviewsURI(321612);


// IN ON LOAD FINISHED
// mReviewsListener.onRetrieveReviews(data, mMovieID);
