package com.example.cristhian.popmovies.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cristhian.popmovies.models.MovieEntity;
import com.example.cristhian.popmovies.models.VideoEntity;

/**
 * Created by Cristhian on 10/27/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "pop_movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntity.TABLE_NAME + " (" +
                MovieEntity._ID + " INTEGER PRIMARY KEY," +
                MovieEntity.COLUMN_ORIGINAL_TITLE + " TEXT UNIQUE NOT NULL, " +
                MovieEntity.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntity.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntity.COLUMN_RELEASE_DATE + " TEXT NOT NULL " +
                MovieEntity.COLUMN_RUNTIME + " INTEGER NOT NULL " +
                MovieEntity.COLUMN_VOTE_AVERAGE + " REAL NOT NULL " +
                MovieEntity.COLUMN_BACKDROP_PATH + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + VideoEntity.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                VideoEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                VideoEntity.COLUMN_MOV_KEY + " INTEGER NOT NULL, " +
                VideoEntity.COLUMN_NAME + " TEXT NOT NULL, " +
                VideoEntity.COLUMN_KEY + " TEXT NOT NULL," +

                VideoEntity.COLUMN_NAME + " TEXT NOT NULL, " +
                VideoEntity.COLUMN_SITE + " TEXT NOT NULL, " +

                VideoEntity.COLUMN_SIZE + " INTEGER NOT NULL, " +
                VideoEntity.COLUMN_TYPE + " TEXT NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + VideoEntity.COLUMN_MOV_KEY + ") REFERENCES " +
                MovieEntity.TABLE_NAME + " (" + MovieEntity._ID + "), " +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntity.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideoEntity.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
