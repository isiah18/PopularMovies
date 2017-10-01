package com.android.popularmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Isingh930 on 9/19/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.android.popularmovies";

    public static final Uri BASE_CONTEXT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";
//    public static final String PATH_REVIEW = "reviews";
//    public static final String PATH_TRAILER = "trailers";

    public static final class FavoriteMovieEntry implements BaseColumns{

        public static final Uri FAVORITE_MOVIE_URI = BASE_CONTEXT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_MOVIEID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_HOMEPAGE_URL = "homepage_url";
        public static final String COLUMN_REVENUE = "revenue";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_BACKGROUND_URL = "background_url";

    }

//    public static final class ReviewEntry implements BaseColumns{
//
//        public static final Uri REVIEW_URI = BASE_CONTEXT_URI.buildUpon().appendPath(PATH_REVIEW).build();
//        public static final String TABLE_NAME = "reviews";
//
//        public static final String COLUMN_MOVIE_ID = "movie_id";
//        public static final String COLUMN_AUTHOR = "author";
//        public static final String COLUMN_CONTENT = "content";
//        public static final String COLUMN_REVIEW_URL = "review_url";
//
//    }
//
//    public static final class TrailerEntry implements BaseColumns{
//
//        public static final Uri TRAILER_URI = BASE_CONTEXT_URI.buildUpon().appendPath(PATH_TRAILER).build();
//        public static final String TABLE_NAME = "trailers";
//
//        public static final String COLUMN_MOVIE_ID = "movie_id";
//        public static final String COLUMN_NAME = "name";
//        public static final String COLUMN_SITE = "site";
//        public static final String COLUMN_VIDEO_SIZE = "size";
//        public static final String COLUMN_VIDEO_KEY = "key";
//    }

}
