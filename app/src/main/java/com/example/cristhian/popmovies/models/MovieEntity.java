package com.example.cristhian.popmovies.models;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.cristhian.popmovies.repository.MovieContract;

/**
 * Created by Cristhian on 10/27/2015.
 */
public class MovieEntity implements BaseColumns{

    public static final Uri CONTENT_URI =
            MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(MovieContract.PATH_MOVIE).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MovieContract.CONTENT_AUTHORITY + "/" + MovieContract.PATH_MOVIE;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MovieContract.CONTENT_AUTHORITY + "/" + MovieContract.PATH_MOVIE;

    public static final String TABLE_NAME = "movie";

    public static final String COLUMN_ORIGINAL_TITLE = "original_title";

    public static final String COLUMN_OVERVIEW = "overview";

    public static final String COLUMN_POSTER_PATH = "poster_path";

    public static final String COLUMN_RELEASE_DATE = "release_date";

    public static final String COLUMN_RUNTIME = "runtime";

    public static final String COLUMN_VOTE_AVERAGE = "vote_average";

    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

    public static Uri buildLocationUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }


}
