package com.example.lastestlistings;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Philip on 16-01-04.
 */
public class MovieListing implements Serializable {

    public static final String resultsKEY = "results";
    public static final String titleKEY = "title";
    public static final String posterPathKEY = "poster_path";
    public static final String plotSynopsisKEY = "overview";
    public static final String userRatingKEY = "vote_average";
    public static final String releaseDateKEY = "release_date";

    private String title;
    private String posterPath;
    private String plotSynopsis;
    private double userRating;
    private String releaseDate;


    public MovieListing(String title, String posterPath, String plotSynopsis, double userRating, String releaseDate) {
        this.title = title;
        this.posterPath = posterPath;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }


    public String getTitle() {
        return title;
    }


    public String getPosterPath() {

        return posterPath;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


    public Uri getPosterUri() {
        return getPosterUri(false);
    }

    public Uri getPosterUri(boolean fullSize) {
        return new Uri.Builder()
                .scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath(fullSize ? "w500" : "w185")
                .appendEncodedPath(getPosterPath())
                .build();
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
