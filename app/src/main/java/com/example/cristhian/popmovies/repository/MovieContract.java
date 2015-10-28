package com.example.cristhian.popmovies.repository;

import android.net.Uri;

/**
 * Created by Cristhian on 10/27/2015.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.movies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_VIDEO = "video";
}
