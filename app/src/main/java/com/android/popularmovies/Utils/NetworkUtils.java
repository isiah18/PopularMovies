package com.android.popularmovies.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.compat.BuildConfig;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Isingh930 on 8/21/17.
 */

public final class NetworkUtils {

    // ADD API KEY HERE
    private static final String API_KEY = com.android.popularmovies.BuildConfig.API_KEY;

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private final static String API_KEY_QUERY_PARAM = "api_key";

    private final static String PAGE_QUERY_PARAM = "page";

    private final static String VIDEOS_URL = "/videos";

    private final static String REVIEWS_URL = "/reviews";


    // BUILD URI FOR ALL MOVIES
    //==============================================================================================
    public static URL buildAllMoviesURI(String endPointInput){
        Uri uri = Uri.parse(BASE_URL + endPointInput).buildUpon()
                    .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY).build();

       return buildURL(uri);
    }

    // OVERLOADING FOR PAGING
    public static URL buildAllMoviesURI(String endPointInput, Integer page){
        Uri uri = Uri.parse(BASE_URL + endPointInput).buildUpon()
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .appendQueryParameter(PAGE_QUERY_PARAM, Integer.toString(page)).build();

        return buildURL(uri);
    }



    //BUILD URI FOR 1 SPECIFIC MOVIE
    //==============================================================================================
    public static URL buildMovieURI(int movieID){
        Uri uri = Uri.parse(BASE_URL + Integer.toString(movieID)).buildUpon()
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY).build();

        return buildURL(uri);
    }

    //BUILD URI FOR REVIEWS FOR A SPECIFIC MOVIE
    //==============================================================================================
    public static URL buildReviewsURI(int movieID){
        Uri uri = Uri.parse(BASE_URL + Integer.toString(movieID) + REVIEWS_URL).buildUpon()
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY).build();

        return buildURL(uri);
    }

    //BUILD URI FOR TRAILERS FOR A SPECIFIC MOVIE
    //==============================================================================================
    public static URL buildTrailersURI(int movieID){
        Uri uri = Uri.parse(BASE_URL + Integer.toString(movieID) + VIDEOS_URL).buildUpon()
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY).build();

        return buildURL(uri);
    }


    //HELPER METHODS
    //==============================================================================================
    private static URL buildURL(Uri uri){
        URL url = null;
        try{
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            Log.e("Network UTILS", e.toString());
        }

        return url;
    }

    public static URL buildRandomURL(String destinationUrl){
        Uri uri = Uri.parse(destinationUrl);
        return buildURL(uri);
    }



    //MAKE NETWORK CALL
    //==============================================================================================
    public static String getResponseFromHTTPUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);

            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }



    // CHECK FOR NETWORK CONNECTION
    //==============================================================================================
    public static boolean checkForNetworkConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }



}
