package com.android.popularmovies.Data;

import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Isingh930 on 9/19/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME  = "movieDB.db";

    private static final int VERSION = 2;

    private static final String DATABASE_ALTER_FAVORITE_MOVIE_ADD_MOVIEID = "ALTER TABLE "
            + MovieContract.FavoriteMovieEntry.TABLE_NAME + " ADD COLUMN " + MovieContract.FavoriteMovieEntry.COLUMN_MOVIEID + " INTEGER NOT NULL;";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_MOVIE_TABLE = "CREATE TABLE "  + MovieContract.FavoriteMovieEntry.TABLE_NAME+ " (" +
                MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIEID + " INTEGER NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_RUNTIME + " INTEGER, " +
                MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MovieContract.FavoriteMovieEntry.COLUMN_GENRES + " TEXT, " +
                MovieContract.FavoriteMovieEntry.COLUMN_RATING + " TEXT, " +
                MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MovieContract.FavoriteMovieEntry.COLUMN_HOMEPAGE_URL + " TEXT, " +
                MovieContract.FavoriteMovieEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_BACKGROUND_URL + " TEXT, " +
                MovieContract.FavoriteMovieEntry.COLUMN_REVENUE + " INTEGER, " +
                MovieContract.FavoriteMovieEntry.COLUMN_BUDGET + " INTEGER NOT NULL);";

//        final String CREATE_REVIEW_TABLE = "CREATE TABLE "  + MovieContract.ReviewEntry.TABLE_NAME+ " (" +
//                MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY, " +
//                MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " REFERENCES " + MovieContract.FavoriteMovieEntry.TABLE_NAME + "(" + MovieContract.FavoriteMovieEntry._ID + "), " +
//                MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
//                MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
//                MovieContract.ReviewEntry.COLUMN_REVIEW_URL + " TEXT);";
//
//        final String CREATE_TRAILER_TABLE = "CREATE TABLE "  + MovieContract.TrailerEntry.TABLE_NAME+ " (" +
//                MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY, " +
//                MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " REFERENCES " + MovieContract.FavoriteMovieEntry.TABLE_NAME + "(" + MovieContract.FavoriteMovieEntry._ID + "), " +
//                MovieContract.TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
//                MovieContract.TrailerEntry.COLUMN_VIDEO_KEY + " TEXT NOT NULL, " +
//                MovieContract.TrailerEntry.COLUMN_VIDEO_SIZE + " INTEGER, " +
//                MovieContract.TrailerEntry.COLUMN_SITE + " TEXT);";
//

        db.execSQL(CREATE_MOVIE_TABLE);
//        db.execSQL(CREATE_REVIEW_TABLE);
//        db.execSQL(CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(DATABASE_ALTER_FAVORITE_MOVIE_ADD_MOVIEID);
        }

        //DON'T DO THIS
//        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteMovieEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
//        onCreate(db);
    }
}
