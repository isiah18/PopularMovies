package com.android.popularmovies.Models;

import java.net.URL;

public class Review {
    String ReviewID;
    String Author;
    String Content;
    URL ReviewUrl;

    public String getReviewID() {
        return ReviewID;
    }

    public void setReviewID(String reviewID) {
        ReviewID = reviewID;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public URL getReviewUrl() {
        return ReviewUrl;
    }

    public void setReviewUrl(URL reviewUrl) {
        ReviewUrl = reviewUrl;
    }
}
