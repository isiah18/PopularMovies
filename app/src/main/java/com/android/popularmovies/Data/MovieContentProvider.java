package com.android.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static android.R.attr.id;

/**
 * Created by Isingh930 on 9/19/17.
 */

public class MovieContentProvider extends ContentProvider {

    private static final int FAV_MOVIES = 100;
    private static final int FAV_MOVIES_WITH_ID = 101;

    //private static final int TRAILERS = 200;
    //private static final int TRAILERS_WITH_ID = 202;

    //private static final int REVIEWS = 300;
    //private static final int REVIEWS_WITH_ID = 302;

    private static final UriMatcher sUriMatcher = buildURIMatcher();
    private MovieDBHelper mMovieDBHelper;

    private static UriMatcher buildURIMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITE_MOVIES, FAV_MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITE_MOVIES + "/#", FAV_MOVIES_WITH_ID);

        //uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_REVIEW, REVIEWS);
        //uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_REVIEW + "/#", REVIEWS_WITH_ID);

        //uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TRAILER, TRAILERS);
        ////uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TRAILER + "/#", TRAILERS_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDBHelper = new MovieDBHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mMovieDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match) {
            case FAV_MOVIES:
                returnCursor =  db.query(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAV_MOVIES_WITH_ID:
                returnCursor = db.query(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
//            case REVIEWS:
//                returnCursor =  db.query(MovieContract.ReviewEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder);
//                break;
//            case TRAILERS:
//                returnCursor =  db.query(MovieContract.TrailerEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder);
//                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnURI;
        long returnID;

        switch (match){
            case FAV_MOVIES:
                returnID = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if(returnID > 0){
                    returnURI = ContentUris.withAppendedId(MovieContract.FavoriteMovieEntry.FAVORITE_MOVIE_URI, id);
                }else{
                    //throw new SQLException("Failed to insert row into " + uri);
                    returnURI = null;
                }
                break;
//            case REVIEWS:
//                returnID = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
//                if(returnID > 0){
//                    returnURI = ContentUris.withAppendedId(MovieContract.ReviewEntry.REVIEW_URI, returnID);
//                }else{
//                    throw new SQLException("Failed to insert row into " + uri);
//                }
//                break;
//            case TRAILERS:
//                returnID = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
//                if(returnID > 0){
//                    returnURI = ContentUris.withAppendedId(MovieContract.TrailerEntry.TRAILER_URI, returnID);
//                }else{
//                    throw new SQLException("Failed to insert row into " + uri);
//                }
//                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnURI;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int favMoviesDeleted = 0;

        switch(match){
            case FAV_MOVIES:
                favMoviesDeleted = db.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, null);
                break;
            case FAV_MOVIES_WITH_ID:
                String movieID =uri.getPathSegments().get(1);
                favMoviesDeleted = db.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, "_id=?", new String[]{movieID});
                break;
        }

        if(favMoviesDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return favMoviesDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
