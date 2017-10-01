package com.android.popularmovies.Models;

import android.databinding.BaseObservable;
import com.android.popularmovies.Utils.DateUtils;

import org.parceler.Parcel;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Isingh930 on 8/20/17.
 */

@Parcel
public class Movie extends BaseObservable{

    Integer ID;
    String ImageURLPath;
    String BackgroundImageURLPath;

    String Title;

    //Change this to a DATE data type
    String ReleaseDate;
    String Overview;

    //double Rating;
    String Rating;
    Integer VoteCount;

    // MOVIE DETAILS
    String MovieRuntime;
    Integer Budget;
    ArrayList<String> Genres;
    URL Homepage;
    Integer Revenue;


    public Integer getVoteCount() {
        return VoteCount;
    }

    public void setVoteCount(Integer voteCount) {
        VoteCount = voteCount;
    }

    ArrayList<Review> Reviews;
    ArrayList<Trailer> Trailers;

    public String getMovieRuntime() {
        return MovieRuntime;
    }

    public void setMovieRuntime(String movieRuntime) {
        MovieRuntime = movieRuntime;
    }

    public Integer getBudget() {
        return Budget;
    }

    public void setBudget(Integer budget) {
        Budget = budget;
    }

    public ArrayList<String> getGenres() {
        return Genres;
    }

    public void setGenres(ArrayList<String> genres) {
        Genres = genres;
    }

    public URL getHomepage() {
        return Homepage;
    }

    public void setHomepage(URL homepage) {
        Homepage = homepage;
    }

    public Integer getRevenue() {
        return Revenue;
    }

    public void setRevenue(Integer revenue) {
        Revenue = revenue;
    }


    //MODIFY these getters and setters to include only what you need!!
    //=============================================================================

    public ArrayList<Review> getReviews() {
        return Reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        Reviews = reviews;
    }

    public ArrayList<Trailer> getTrailers() {
        return Trailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        Trailers = trailers;
    }

//=============================================================================

    public Movie(){
        Genres = new ArrayList<>();
    }

    public Integer getID() {return ID;}

    public void setID(Integer ID) {this.ID = ID;}

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getReleaseDate() {
        return DateUtils.stringToDate("yyyy", ReleaseDate);
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }


    public String getRating() {
        return Rating + "/10";
    }

    public void setRating(String rating) {
        Rating = rating;

    }

    public String getBackgroundImageURLPath() {return BackgroundImageURLPath;}

    public void setBackgroundImageURLPath(String backgroundImageURLPath) {BackgroundImageURLPath = backgroundImageURLPath;}

    public String getImageURLPath() {
        return ImageURLPath;
    }

    public void setImageURLPath(String imageURLPath) {
        ImageURLPath = imageURLPath;
    }

}

