package com.example.cristhian.popmovies.models;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.cristhian.popmovies.repository.MovieContract;

/**
 * Created by ctolosa on 06/11/2015.
 */
public class ReviewEntity implements BaseColumns {

    public static final Uri CONTENT_URI =
            MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(MovieContract.PATH_REVIEW).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MovieContract.CONTENT_AUTHORITY + "/" + MovieContract.PATH_REVIEW;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MovieContract.CONTENT_AUTHORITY + "/" + MovieContract.PATH_REVIEW;

    public static final String TABLE_NAME = "review";

    // Column with the foreign key into the location table.
    public static final String COLUMN_MOV_KEY = "movie_id";

    // Weather id as returned by API, to identify the icon to be used
    public static final String COLUMN_REVIEW_ID = "review_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_URL = "url";

    public static Uri buildReviewUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static String getReviewFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

}
