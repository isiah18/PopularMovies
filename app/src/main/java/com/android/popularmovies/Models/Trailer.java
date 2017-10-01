package com.android.popularmovies.Models;

public class Trailer{
    String TrailerID;
    String Name;
    String Site;
    Integer VideoSize;
    String VideoKey;

    public String getVideoKey() {
        return VideoKey;
    }

    public void setVideoKey(String videoKey) {
        VideoKey = videoKey;
    }

    public String getTrailerID() {
        return TrailerID;
    }

    public void setTrailerID(String trailerID) {
        TrailerID = trailerID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSite() {
        return Site;
    }

    public void setSite(String site) {
        Site = site;
    }

    public Integer getVideoSize() {
        return VideoSize;
    }

    public void setVideoSize(Integer videoSize) {
        VideoSize = videoSize;
    }
}

