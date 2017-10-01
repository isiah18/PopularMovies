package com.android.popularmovies.Utils;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.android.popularmovies.Data.MovieContract;
import com.android.popularmovies.R;

import java.util.Set;

/**
 * Created by Isingh930 on 9/21/17.
 */

public final class DBUtils {

    public static class InsertValuesIntoDBService extends IntentService{

        public InsertValuesIntoDBService() {
            super("DatabaseService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            ResultReceiver resultReciever = intent.getParcelableExtra("RESULT_RECEIVER");

            ContentValues cv = new ContentValues();

            Bundle contentValueBundle = intent.getBundleExtra(getString(R.string.bundleContentValues));
            Uri endpointURI = intent.getParcelableExtra(getString(R.string.endpoint_uri));

            //Check if movie exists before inserting
            int movieID = contentValueBundle.getInt(MovieContract.FavoriteMovieEntry.COLUMN_MOVIEID);
            boolean doesMovieExist = checkIfMovieAlreadyExists(movieID);

            Uri uri = null;

            if(!doesMovieExist){
                Set<String> contentValueBundleKeys = contentValueBundle.keySet();

                for(String key : contentValueBundleKeys){
                    Object o = contentValueBundle.get(key);
                    if(o != null){
                        if(o.getClass().getSimpleName().contentEquals("int")){
                            cv.put(key, (int)contentValueBundle.get(key));
                        }
                        else if(o.getClass().getSimpleName().contentEquals("Integer")){
                            cv.put(key, (Integer) contentValueBundle.get(key));
                        }
                        else if(o.getClass().getSimpleName().contentEquals("String")){
                            cv.put(key, (String)contentValueBundle.get(key));
                        }
                    }

                }

                uri = getContentResolver().insert(endpointURI, cv);
            }

            Bundle args = new Bundle();
            args.putString("RESULT_TYPE", "insert");
            if(!doesMovieExist){
                args.putBoolean("DOES_MOVIE_ALREADY_EXIST", false);
                if(uri != null){
                    resultReciever.send(1, args);
                }else{
                    resultReciever.send(0, args);
                }
            }else{
                args.putBoolean("DOES_MOVIE_ALREADY_EXIST", true);
                resultReciever.send(0, args);
            }
        }


        private boolean checkIfMovieAlreadyExists(int movieID){
            String[] selectionArgs = {String.valueOf(movieID)};
            String selection = MovieContract.FavoriteMovieEntry.COLUMN_MOVIEID + "=?";
            Cursor retQuery = getContentResolver().query(ContentUris.withAppendedId(MovieContract.FavoriteMovieEntry.FAVORITE_MOVIE_URI, movieID), null, selection, selectionArgs, null);
            if(retQuery.getCount() == 0){
                return false;
            }else{
                return true;
            }
        }
    }




    // RESULT RECEIVER INNER CLASS
    //==============================================================================================
    public static class MovieResultReciever extends android.os.ResultReceiver{

        private Receiver mReceiver;

        public MovieResultReciever(Handler handler) {
            super(handler);
        }


        public void setReceiver(Receiver receiver) {
            mReceiver = receiver;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (mReceiver != null) {
                mReceiver.onReceiveResult(resultCode, resultData);
            }
        }
    }



    // RESULT RECEIVER INTERFACE
    //==============================================================================================
    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

}
