package com.example.cristhian.popmovies.models;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.cristhian.popmovies.repository.MovieContract;

/**
 * Created by Cristhian on 10/27/2015.
 */
public class VideoEntity implements BaseColumns {

    public static final Uri CONTENT_URI =
            MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(MovieContract.PATH_VIDEO).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MovieContract.CONTENT_AUTHORITY + "/" + MovieContract.PATH_VIDEO;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MovieContract.CONTENT_AUTHORITY + "/" + MovieContract.PATH_VIDEO;

    public static final String TABLE_NAME = "video";

    // Column with the foreign key into the location table.
    public static final String COLUMN_MOV_KEY = "movie_id";

    // Weather id as returned by API, to identify the icon to be used
    public static final String COLUMN_VIDEO_ID = "video_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_SITE = "site";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_TYPE = "type";

    public static Uri buildVideoUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static String getVideoFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    /*
           Student: This is the buildWeatherLocation function you filled in.
        */
    public static Uri buildVideoMovie(String movieSetting) {
        return CONTENT_URI.buildUpon().appendPath(movieSetting).build();
    }
}
