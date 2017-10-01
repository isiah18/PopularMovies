package com.android.popularmovies.Activites;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.popularmovies.Activites.Fragments.ReviewsFragment;
import com.android.popularmovies.Adapters.ViewPagerAdapters.RTPagerAdapter;
import com.android.popularmovies.Data.MovieContract;
import com.android.popularmovies.Models.Movie;
import com.android.popularmovies.Models.Review;
import com.android.popularmovies.R;
import com.android.popularmovies.Utils.DBUtils;
import com.android.popularmovies.Utils.DateUtils;
import com.android.popularmovies.Utils.JsonUtils;
import com.android.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>, DBUtils.Receiver{

    private Movie mMovie;

    @BindView(R.id.poster_image_view) ImageView mImagePosterIV;
    @BindView(R.id.background_image_view) ImageView mBackgroundPosterIV;
    @BindView(R.id.release_date_text_view) TextView mReleaseDateTextView;
    @BindView(R.id.overview_text_view) TextView mOverviewTextView;
    @BindView(R.id.rating_text_view) TextView mRatingTextView;
    @BindView(R.id.runtime_text_view) TextView mRuntimeTextView;
    @BindView(R.id.title_text_view) TextView mTitleTextView;
    @BindView(R.id.genre_text_view) TextView mGenreTextView;
    @BindView(R.id.vote_count_text_view) TextView mVoteCountTextView;
    @BindView(R.id.budget_text_view) TextView mBudgetTextView;
    @BindView(R.id.revenue_text_view) TextView mRevenueTextView;
    @BindView(R.id.favorite_button) ImageButton mFavoriteButton;
    private String previousFragment;
    private static final int LOADER_MOVIE_DEATILS_ID = 572;
    private DBUtils.MovieResultReciever mResultReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        mResultReceiver = new DBUtils.MovieResultReciever(new Handler());
        mResultReceiver.setReceiver(this);


        if (getIntent().hasExtra(getString(R.string.extra_movie_data)) && getIntent().hasExtra(getString(R.string.intent_coming_from))) {
            mMovie = Parcels.unwrap(getIntent().getParcelableExtra(getString(R.string.extra_movie_data)));
            previousFragment = getIntent().getStringExtra(getString(R.string.intent_coming_from));
        }

        setUpTabLayoutAndViewPager();

        if (previousFragment.contentEquals(getString(R.string.main_movies_fragment))) {
            setData(getString(R.string.main_movies_fragment));
            if (checkForInternetConnection()) {
                getSupportLoaderManager().initLoader(LOADER_MOVIE_DEATILS_ID, null, this);
            }
        } else {
            setData(getString(R.string.favorite_fragment));
        }
    }




    // HELPER METHODS
    //==============================================================================================
    private boolean checkForInternetConnection() {
        boolean isConnected = NetworkUtils.checkForNetworkConnection(getApplicationContext());
        return isConnected;
    }

    private void setFavoriteButtonOnClick() {
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForInternetConnection()) {
                    Bundle movieBundle = new Bundle();

                    movieBundle.putInt(MovieContract.FavoriteMovieEntry.COLUMN_MOVIEID, mMovie.getID());
                    movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_TITLE, mMovie.getTitle());
                    movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
                    movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
                    movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_RATING, mMovie.getRating());
                    movieBundle.putInt(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT, mMovie.getVoteCount());
                    movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_RUNTIME, mMovie.getMovieRuntime());
                    movieBundle.putInt(MovieContract.FavoriteMovieEntry.COLUMN_BUDGET, mMovie.getBudget());
                    movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_GENRES, convertGenreListToJSONString(mMovie.getGenres()));
                    if (mMovie.getHomepage() == null) {
                        movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_HOMEPAGE_URL, String.valueOf(mMovie.getHomepage()));
                    } else {
                        movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_HOMEPAGE_URL, null);
                    }

                    movieBundle.putInt(MovieContract.FavoriteMovieEntry.COLUMN_REVENUE, mMovie.getRevenue());
                    movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_IMAGE_URL, mMovie.getImageURLPath());
                    movieBundle.putString(MovieContract.FavoriteMovieEntry.COLUMN_BACKGROUND_URL, mMovie.getBackgroundImageURLPath());

                    Intent insertMovieIntent = new Intent(v.getContext(), DBUtils.InsertValuesIntoDBService.class);
                    insertMovieIntent.putExtra(getString(R.string.bundleContentValues), movieBundle);
                    insertMovieIntent.putExtra(getString(R.string.endpoint_uri), MovieContract.FavoriteMovieEntry.FAVORITE_MOVIE_URI);
                    insertMovieIntent.putExtra("RESULT_RECEIVER", mResultReceiver);
                    startService(insertMovieIntent);


                } else {
                    Toast.makeText(getApplicationContext(), "Sorry. Connect to internet to add to Favorites", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private String convertGenreListToJSONString(ArrayList<String> genres) {
        try {
            JSONObject json = new JSONObject();
            json.put("genreArray", new JSONArray(genres));
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void setData(String previousFragment) {
        String imageUrl = mMovie.getImageURLPath();
        String backgroundURL = mMovie.getBackgroundImageURLPath();
        Picasso.with(MovieDetailsActivity.this).load(imageUrl).fit().into(mImagePosterIV);
        Picasso.with(MovieDetailsActivity.this).load(backgroundURL).fit().into(mBackgroundPosterIV);

        mTitleTextView.setText(mMovie.getTitle());
        String formattedReleaseDate = DateUtils.stringToDate("yyyy", mMovie.getReleaseDate());
        if (formattedReleaseDate != null) {
            mReleaseDateTextView.setText(formattedReleaseDate);
        }

        mOverviewTextView.setText(mMovie.getOverview());
        mRatingTextView.setText(String.valueOf(mMovie.getRating()));
        mVoteCountTextView.setText(String.valueOf(mMovie.getVoteCount()));

        if (previousFragment == getString(R.string.favorite_fragment)) {
            String genreText = formatGenreText(mMovie.getGenres());
            mGenreTextView.setText(genreText);
            String budgetString = NumberFormat.getInstance().format(mMovie.getBudget());
            String revenueString = NumberFormat.getInstance().format(mMovie.getRevenue());
            mBudgetTextView.setText(budgetString);
            mRevenueTextView.setText(revenueString);
            mRuntimeTextView.setText(mMovie.getMovieRuntime() + getString(R.string.minutes_addition));
            mFavoriteButton.setVisibility(View.GONE);
        }
    }


    private void setUpTabLayoutAndViewPager() {
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.review_trailer_sliding_tabs);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.review_trailer_view_pager);

        mViewPager.setAdapter(new RTPagerAdapter(getSupportFragmentManager(), mMovie.getID(), getApplicationContext()));

        mTabLayout.setupWithViewPager(mViewPager);
    }


    private String formatGenreText(ArrayList<String> genreList) {
        StringBuilder concatenatedGenres = new StringBuilder();
        concatenatedGenres.append("| ");
        for (String genre : genreList) {
            concatenatedGenres.append(genre + " | ");
        }

        return concatenatedGenres.toString();
    }


    // ASYNC TASK LOADER
    //==============================================================================================

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Movie loadInBackground() {
                URL movieUrl = NetworkUtils.buildMovieURI(mMovie.getID());
                try {
                    String jsonResult = NetworkUtils.getResponseFromHTTPUrl(movieUrl);
                    Movie movie = (Movie) JsonUtils.parseJsonData(jsonResult, getString(R.string.single_movie_json_result), MovieDetailsActivity.this, mMovie);
                    return movie;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        setFavoriteButtonOnClick();
        if (data == null) {
            return;
        }
        String genreText = formatGenreText(mMovie.getGenres());
        mGenreTextView.setText(genreText);
        String budgetString = NumberFormat.getInstance().format(mMovie.getBudget());
        String revenueString = NumberFormat.getInstance().format(mMovie.getRevenue());
        mBudgetTextView.setText(budgetString);
        mRevenueTextView.setText(revenueString);
        mRuntimeTextView.setText(mMovie.getMovieRuntime() + getString(R.string.minutes_addition));
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResultReceiver.setReceiver(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResultReceiver.setReceiver(this);
    }


    // RESULT RECEIVER - onReceiveResult() IMPLEMENTATION
    //==============================================================================================

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String resultType = resultData.getString("RESULT_TYPE");
        boolean movieExists = resultData.getBoolean("DOES_MOVIE_ALREADY_EXIST");

        if(resultType.contentEquals("insert") && movieExists == false){
            if(resultCode == 1){
                Toast.makeText(getApplicationContext(), "Movie added to favorites", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Error adding movie to favorites, try again...", Toast.LENGTH_SHORT).show();
            }
        }else if(resultType.contentEquals("insert") && movieExists == true){
            Toast.makeText(getApplicationContext(), "Movie already in favorites...", Toast.LENGTH_SHORT).show();
        }
    }
}










//    private ReviewsRetriever mReviewsRetriever;


    // ADDING REVIEWS TO DB
    //==============================================================================================

//    @Parcel
//    public static class ReviewsRetriever implements ReviewsFragment.RetrieveReviewsListener{
//
//        // REVIEW FRAGMENT INTERFACE IMPLEMENTATION
//        @Override
//        public void onRetrieveReviews(ArrayList<Review> reviews, int movieID) {
//
//            addToDB(reviews, movieID);
//        }
//
//        public void addToDB(ArrayList<Review> reviews, int movieID){
//            ContentValues reviewsCV = new ContentValues();
//            reviewsCV.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, movieID);
//            //reviewsCV.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, );
//        }
//    }



//  IN ON CREATE
//  mReviewsRetriever = new ReviewsRetriever();



// IN SETUP TABLAYOUT AND VIEWPAGER METHOD
//  mViewPager.setAdapter(new RTPagerAdapter(getSupportFragmentManager(), mMovie.getID(), getApplicationContext(), mReviewsRetriever));