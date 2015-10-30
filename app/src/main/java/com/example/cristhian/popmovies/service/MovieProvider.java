package com.example.cristhian.popmovies.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.cristhian.popmovies.models.MovieEntity;
import com.example.cristhian.popmovies.models.VideoEntity;
import com.example.cristhian.popmovies.repository.MovieContract;
import com.example.cristhian.popmovies.repository.MovieDbHelper;

/**
 * Created by Cristhian on 10/27/2015.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int VIDEO = 200;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.


        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.


        // 3) Return the new matcher!

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);

        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;

        SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case VIDEO: {
                //retCursor = mOpenHelper.getReadableDatabase().query(VideoEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                //break;


                SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                String[] args = {String.valueOf(uri.getLastPathSegment())};
                Cursor cursor = db.rawQuery(
                        "SELECT p1.first_name, p1.last_name " +
                                "FROM Movie m, Video v " +
                                "WHERE m.movie_id = v.movie_id AND " +
                                "v.movie_id = ?", args);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                retCursor = cursor;
                break;
            }
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(MovieEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

                    //_QB.setTables(MovieEntity.TABLE_NAME +
                      //      " INNER JOIN " + VideoEntity.TABLE_NAME + " ON " +
                        //    MovieEntity.COLUMN_MOVIE_ID + " = " + VideoEntity.COLUMN_MOV_KEY);
                    //_QB.setProjectionMap();
                    //_TableType = BOOKS;
                    //break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIE:
                return MovieEntity.CONTENT_TYPE;
            case VIDEO:
                return VideoEntity.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case VIDEO: {
                long _id = db.insert(VideoEntity.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = VideoEntity.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE: {
                long _id = db.insert(MovieEntity.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieEntity.buildLocationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";
        switch (match) {
            case VIDEO:
                rowsDeleted = db.delete(VideoEntity.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE:
                rowsDeleted = db.delete(MovieEntity.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case VIDEO:
                rowsUpdated = db.update(VideoEntity.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE:
                rowsUpdated = db.update(MovieEntity.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
