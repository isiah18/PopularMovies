package com.android.popularmovies.Utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.android.popularmovies.Models.Movie;
import com.android.popularmovies.Models.Review;
import com.android.popularmovies.Models.Trailer;
import com.android.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.popularmovies.Utils.NetworkUtils.buildRandomURL;


/**
 * Created by Isingh930 on 8/21/17.
 */

public final class JsonUtils {

    public static Object parseJsonData(String jsonString, String apiResultType, Context context, Movie movieObject) throws JSONException{

        final String OWM_MESSAGE_CODE = "cod";

        JSONObject jsonObject = new JSONObject(jsonString);

        if(jsonObject.has(OWM_MESSAGE_CODE)){
            int code = jsonObject.getInt(OWM_MESSAGE_CODE);
            switch (code){
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        if(apiResultType.contentEquals(context.getString(R.string.all_movies_json_result))){
            return parseAllMovieJSON(jsonObject);
        }
        else if(apiResultType == context.getString(R.string.single_movie_json_result) && movieObject != null){
            return parseSingleMovieJSON(jsonObject, movieObject);
        }
        else if(apiResultType == context.getString(R.string.movie_reviews_json_result)){
            return parseMovieReviewsJSON(jsonObject);
        }
        else if(apiResultType == context.getString(R.string.movie_trailers_json_result)){
            return parseMovieTrailersJSON(jsonObject);
        }
        else{
            return null;
        }

    }


    //PARSE JSON FOR ALL MOVIES RESULT
    //==============================================================================================
    private static ArrayList<Movie> parseAllMovieJSON(JSONObject jsonObject) throws JSONException{

        ArrayList<Movie> movieList = new ArrayList<>();
        JSONArray results = jsonObject.getJSONArray("results");

        if(results.length() == 0){
            return null;
        }

        for(int i = 0; i < results.length(); i++){
            JSONObject movieDetails = results.getJSONObject(i);
            Movie movieToAdd = new Movie();
            movieToAdd.setID(movieDetails.getInt("id"));
            movieToAdd.setTitle(movieDetails.getString("title"));
            movieToAdd.setImageURLPath("http://image.tmdb.org/t/p/w500/" + movieDetails.getString("poster_path"));
            movieToAdd.setBackgroundImageURLPath("http://image.tmdb.org/t/p/w500/" + movieDetails.getString("backdrop_path"));
            movieToAdd.setRating(Double.toString(movieDetails.getDouble("vote_average")));
            movieToAdd.setVoteCount(movieDetails.getInt("vote_count"));
            movieToAdd.setOverview(movieDetails.getString("overview"));
            movieToAdd.setReleaseDate(movieDetails.getString("release_date"));
            movieList.add(movieToAdd);
        }

        return movieList;
    }


    //PARSE JSON FOR SINGLE MOVIE RESULT
    //==============================================================================================
    private static Movie parseSingleMovieJSON(JSONObject jsonObject, Movie movieObject) throws JSONException{

        movieObject.setBudget(jsonObject.getInt("budget"));
        String homepageURL = jsonObject.getString("homepage");
        URL movieHomepageUrl = NetworkUtils.buildRandomURL(homepageURL);
        movieObject.setHomepage(movieHomepageUrl);
        movieObject.setMovieRuntime(Integer.toString(jsonObject.getInt("runtime")));
        movieObject.setRevenue(jsonObject.getInt("revenue"));

        JSONArray genreArray = jsonObject.getJSONArray("genres");
        ArrayList<String> genres = new ArrayList<>();;
        for(int i =0; i < genreArray.length(); i++){
            JSONObject genreObj = genreArray.getJSONObject(i);
            genres.add(genreObj.getString("name"));
        }
        movieObject.setGenres(genres);

        return movieObject;
    }



    //PARSE JSON FOR TRAILERS RESULT
    //==============================================================================================
    private static ArrayList<Trailer> parseMovieTrailersJSON(JSONObject jsonObject) throws JSONException{

        ArrayList<Trailer> movieTrailers = new ArrayList<>();
        JSONArray results = jsonObject.getJSONArray("results");
        for(int i = 0; i < results.length(); i++){
            JSONObject item = results.getJSONObject(i);
            Trailer trailer = new Trailer();
            trailer.setName(item.getString("name"));
            trailer.setSite(item.getString("site"));
            trailer.setTrailerID(item.getString("id"));
            trailer.setVideoSize(item.getInt("size"));
            trailer.setVideoKey(item.getString("key"));
            movieTrailers.add(trailer);
        }

        return movieTrailers;
    }




    //PARSE JSON FOR REVIEWS RESULT
    //==============================================================================================
    private static ArrayList<Review> parseMovieReviewsJSON(JSONObject jsonObject) throws JSONException{

        ArrayList<Review> movieReviews = new ArrayList<>();

        JSONArray results = jsonObject.getJSONArray("results");
        for(int i = 0; i < results.length(); i++){
            JSONObject item = results.getJSONObject(i);
            Review review = new Review();
            review.setReviewID(item.getString("id"));
            review.setAuthor(item.getString("author"));
            review.setContent(item.getString("content"));
            String reviewURL = item.getString("url");
            URL movieReviewURL = NetworkUtils.buildRandomURL(reviewURL);
            review.setReviewUrl(movieReviewURL);
            movieReviews.add(review);
        }

        return movieReviews;
    }



    // CREATE YOUTUBE URL GIVEN KEY
    //==============================================================================================
    public static String getFullYoutubeURL(String key){
        String BASE_URL = "https://www.youtube.com/watch";
        String YOUTUBE_KEY_PARAM = "v";
        Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(YOUTUBE_KEY_PARAM, key).build();
        URL url = null;
        try{
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url.toString();

    }

}