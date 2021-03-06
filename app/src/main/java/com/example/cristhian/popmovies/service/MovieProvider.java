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
import android.text.TextUtils;

import com.example.cristhian.popmovies.models.MovieEntity;
import com.example.cristhian.popmovies.models.ReviewEntity;
import com.example.cristhian.popmovies.models.VideoEntity;
import com.example.cristhian.popmovies.repository.MovieContract;
import com.example.cristhian.popmovies.repository.MovieDbHelper;

/**
 * Created by Cristhian on 10/27/2015.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final String MOVIE_ID = "movie_id";

    static final int MOVIE = 100;
    static final int VIDEO = 200;
    static final int REVIEW = 300;
    static final int MOVIE_WITH_VIDEOS = 400;
    static final int MOVIE_WITH_REVIEWS = 500;
    static final int MOVIE_BY_MOVIE_ID = 600;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/*", MOVIE_WITH_VIDEOS);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*", MOVIE_WITH_REVIEWS);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_BY_MOVIE_ID);

        return matcher;
    }

    private static final SQLiteQueryBuilder sVideosByMovieQueryBuilder;

    private static final SQLiteQueryBuilder sReviewsByMovieQueryBuilder;

    private static final SQLiteQueryBuilder sMovieByIdQueryBuilder;

    static {
        sVideosByMovieQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //video INNER JOIN movie ON video.movie_id = movie.movie_id
        sVideosByMovieQueryBuilder.setTables(
                VideoEntity.TABLE_NAME + " INNER JOIN " +
                        MovieEntity.TABLE_NAME +
                        " ON " + VideoEntity.TABLE_NAME +
                        "." + VideoEntity.COLUMN_MOV_KEY +
                        " = " + MovieEntity.TABLE_NAME +
                        "." + MovieEntity.COLUMN_MOVIE_ID);
    }

    static {
        sReviewsByMovieQueryBuilder = new SQLiteQueryBuilder();

        sReviewsByMovieQueryBuilder.setTables(
                MovieEntity.TABLE_NAME + " INNER JOIN " +
                        ReviewEntity.TABLE_NAME +
                        " ON " + MovieEntity.TABLE_NAME +
                        "." + MovieEntity.COLUMN_MOVIE_ID +
                        " = " + ReviewEntity.TABLE_NAME +
                        "." + ReviewEntity.COLUMN_MOV_KEY);
    }

    static {
        sMovieByIdQueryBuilder = new SQLiteQueryBuilder();

        sMovieByIdQueryBuilder.setTables(
                MovieEntity.TABLE_NAME);
    }

    //movie.movie_id = ?
    private static final String sMovieSettingSelection =
            MovieEntity.TABLE_NAME +
                    "." + MovieEntity.COLUMN_MOVIE_ID + " = ? ";

    private Cursor getVideosByMovie(Uri uri, String[] projection, String sortOrder) {
        String movie = VideoEntity.getVideoFromUri(uri);
        String[] selectionArgs;
        String selection;

        selection = sMovieSettingSelection;
        selectionArgs = new String[]{movie};

        return sVideosByMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviewsByMovie(Uri uri, String[] projection, String sortOrder) {
        String movie = ReviewEntity.getReviewFromUri(uri);
        String[] selectionArgs;
        String selection;

        selection = sMovieSettingSelection;
        selectionArgs = new String[]{movie};

        return sReviewsByMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMoviesById(Uri uri, String[] projection, String sortOrder) {
        String movie = MovieEntity.getMovieFromUri(uri);
        String[] selectionArgs;
        String selection;

        selection = sMovieSettingSelection;
        selectionArgs = new String[]{movie};

        return sMovieByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case VIDEO: {
                retCursor = mOpenHelper.getReadableDatabase().query(VideoEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(MovieEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case REVIEW: {
                retCursor = mOpenHelper.getReadableDatabase().query(ReviewEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case MOVIE_WITH_VIDEOS: {
                retCursor = getVideosByMovie(uri, projection, sortOrder);
                break;
            }

            case MOVIE_WITH_REVIEWS: {
                retCursor = getReviewsByMovie(uri, projection, sortOrder);
                break;
            }

            case MOVIE_BY_MOVIE_ID: {
                retCursor = getMoviesById(uri, projection, sortOrder);
                break;
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
            case REVIEW:
                return ReviewEntity.CONTENT_TYPE;
            case MOVIE_WITH_VIDEOS:
                return VideoEntity.CONTENT_TYPE;
            case MOVIE_WITH_REVIEWS:
                return ReviewEntity.CONTENT_TYPE;
            case MOVIE_BY_MOVIE_ID:
                return MovieEntity.CONTENT_TYPE;
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
                    returnUri = VideoEntity.buildVideoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE: {
                long _id = db.insert(MovieEntity.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieEntity.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {
                long _id = db.insert(ReviewEntity.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ReviewEntity.buildReviewUri(_id);
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
            case REVIEW:
                rowsDeleted = db.delete(ReviewEntity.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_BY_MOVIE_ID: {
                String id = uri.getLastPathSegment();
                rowsDeleted = db.delete(MovieEntity.TABLE_NAME, MOVIE_ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            }
            case MOVIE_WITH_VIDEOS: {
                String id = uri.getLastPathSegment();
                rowsDeleted = db.delete(VideoEntity.TABLE_NAME, MOVIE_ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            }
            case MOVIE_WITH_REVIEWS: {
                String id = uri.getLastPathSegment();
                rowsDeleted = db.delete(ReviewEntity.TABLE_NAME, MOVIE_ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            }
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
            case REVIEW:
                rowsUpdated = db.update(ReviewEntity.TABLE_NAME, values, selection, selectionArgs);
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
