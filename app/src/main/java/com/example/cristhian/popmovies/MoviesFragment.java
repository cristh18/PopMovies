package com.example.cristhian.popmovies;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.cristhian.popmovies.models.MovieEntity;
import com.example.cristhian.popmovies.models.ReviewEntity;
import com.example.cristhian.popmovies.models.VideoEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristhian on 9/29/2015.
 */
public class MoviesFragment extends Fragment {

    private GridView myGridMovieView;

    protected static MovieListAdapter customListAdapter;

    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private String valueSorts;

    Communicator comm;

    private List<Movie> favoriteMovies;

    int option_selected;

    public MoviesFragment() {
        valueSorts = "popularity.desc";
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            comm = (Communicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Communicator");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            int option = savedInstanceState.getInt("optionSelected");
            Log.e(LOG_TAG, "Option selected: " + option);
            if (option == R.id.action_most_popular) {
                valueSorts = "popularity.desc";
                option_selected = R.id.action_most_popular;
            } else if (option == R.id.action_highest_rated) {
                valueSorts = "vote_average.desc";
                option_selected = R.id.action_highest_rated;
            } else if (option == R.id.action_favorites) {
                valueSorts = "searchFavorites";
                option_selected = R.id.action_favorites;
            }
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_pop_movies, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_most_popular) {
            PopularMoviesTask popularMoviesTask = new PopularMoviesTask();
            popularMoviesTask.execute("popularity.desc");
            option_selected = R.id.action_most_popular;
            return true;
        } else if (id == R.id.action_highest_rated) {
            PopularMoviesTask popularMoviesTask = new PopularMoviesTask();
            popularMoviesTask.execute("vote_average.desc");
            option_selected = R.id.action_highest_rated;
            return true;
        } else if (id == R.id.action_favorites) {
            searchFavorites();
            seeFavoriteMovies();
            option_selected = R.id.action_favorites;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies_fragment_layout, container, false);

        getActivity().setTitle("Pop Movies");
        comm = (Communicator) getActivity();
        final List<Movie> movies = new ArrayList<>();
        customListAdapter = new MovieListAdapter(this.getActivity(), movies);
        if (!valueSorts.equalsIgnoreCase("searchFavorites")) {
            PopularMoviesTask popularMoviesTask = new PopularMoviesTask();
            popularMoviesTask.execute(valueSorts);
        } else {
            searchFavorites();
            seeFavoriteMovies();
        }

        myGridMovieView = (GridView) view.findViewById(R.id.grid_view_pop_movies);

        myGridMovieView.setAdapter(customListAdapter);
        myGridMovieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = customListAdapter.getItem(i);
                if (movie.getRuntime() == null) {
                    movie.setRuntime(0);
                }
                comm.response(movie);

            }
        });

        return view;
    }

    public void setValueSorts(String valueSorts) {
        this.valueSorts = valueSorts;
    }

    public String getValueSorts() {
        return valueSorts;
    }

    private void searchFavorites() {

        favoriteMovies = new ArrayList<>();

        // A "projection" defines the columns that will be returned for each row
        final String[] projection = {
                MovieEntity._ID,    // Contract class constant for the _ID column name
                MovieEntity.COLUMN_MOVIE_ID,   // Contract class constant for the word column name
                MovieEntity.COLUMN_BACKDROP_PATH,
                MovieEntity.COLUMN_ORIGINAL_TITLE,
                MovieEntity.COLUMN_OVERVIEW,
                MovieEntity.COLUMN_POSTER_PATH,
                MovieEntity.COLUMN_RELEASE_DATE,
                MovieEntity.COLUMN_RUNTIME,
                MovieEntity.COLUMN_VOTE_AVERAGE,
                MovieEntity.COLUMN_POPULARITY,
                MovieEntity.COLUMN_VOTE_COUNT
        };

        // Defines a string to contain the selection clause
        String selectionClause = null;
        selectionClause = null;

        // An array to contain selection arguments
        String[] selectionArgs = null;


        // An ORDER BY clause, or null to get results in the default sort order
        final String sortOrder = null;

        String original_title = "";
        Cursor cursor = getActivity().getContentResolver().query(
                MovieEntity.CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                sortOrder);

        if (cursor.moveToFirst()) {
            do {
                String movie_title = cursor.getString(cursor.getColumnIndex("original_title"));
                original_title = movie_title;
                Log.i("TEST1", "Titulo pelicula1: ".concat(movie_title));
                Log.i("TEST2", "Titulo pelicula2: ".concat(original_title));

                Movie movie = new Movie();
                movie.setId(cursor.getLong(cursor.getColumnIndex("movie_id")));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex("original_title")));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndex("poster_path")));
                movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex("backdrop_path")));
                movie.setOverview(cursor.getString(cursor.getColumnIndex("overview")));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex("release_date")));
                movie.setRuntime(cursor.getInt(cursor.getColumnIndex("runtime")));
                movie.setVote_average(cursor.getDouble(cursor.getColumnIndex("vote_average")));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndex("popularity")));
                movie.setVote_count(cursor.getInt(cursor.getColumnIndex("vote_count")));
                movie.setFavorite(true);

                movie.setVideos(searchVideoMovies(movie.getId()));
                movie.setReviews(searchReviewMovies(movie.getId()));

                favoriteMovies.add(movie);

            } while (cursor.moveToNext());
        }
    }

    private void seeFavoriteMovies() {
        customListAdapter.clear();
        int i = 1;
        for (Movie m : favoriteMovies) {
            Log.i("TestMovie", "Movie ".concat(String.valueOf(i)).concat(": ").concat(m.getOriginal_title()));
            seeVideoMovies(m);
            seeReviewMovies(m);
            i++;
            customListAdapter.add(m);
        }

    }

    private List<MovieVideoDetail> searchVideoMovies(Long movieId) {
        List<MovieVideoDetail> videos = new ArrayList<>();

        String[] projection = {
                VideoEntity.COLUMN_VIDEO_ID,
                VideoEntity.COLUMN_NAME,
                VideoEntity.COLUMN_KEY,
                VideoEntity.COLUMN_SITE,
                VideoEntity.COLUMN_SIZE,
                VideoEntity.COLUMN_TYPE
        };

        Cursor cursor = getActivity().getContentResolver().query(VideoEntity.buildVideoUri(movieId),
                projection,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                MovieVideoDetail movieVideoDetail = new MovieVideoDetail();
                movieVideoDetail.setId(cursor.getString(cursor.getColumnIndex("video_id")));
                movieVideoDetail.setName(cursor.getString(cursor.getColumnIndex("name")));
                movieVideoDetail.setKey(cursor.getString(cursor.getColumnIndex("key")));
                movieVideoDetail.setSite(cursor.getString(cursor.getColumnIndex("site")));
                movieVideoDetail.setSize(cursor.getInt(cursor.getColumnIndex("size")));
                movieVideoDetail.setType(cursor.getString(cursor.getColumnIndex("type")));
                videos.add(movieVideoDetail);

            } while (cursor.moveToNext());
        }

        return videos;
    }


    private void seeVideoMovies(Movie movie) {
        if (movie.getVideos().size() > 0) {
            int j = 0;
            for (MovieVideoDetail v : movie.getVideos()) {
                Log.i("TestMovieVideo", "Video ".concat(String.valueOf(j)).concat(": ").concat(v.getId()));
                j++;
            }
        }
    }


    private List<MovieReviewDetail> searchReviewMovies(Long movieId) {
        List<MovieReviewDetail> reviews = new ArrayList<>();

        String[] projection = {
                ReviewEntity.COLUMN_REVIEW_ID,
                ReviewEntity.COLUMN_AUTHOR,
                ReviewEntity.COLUMN_CONTENT,
                ReviewEntity.COLUMN_URL
        };

        Cursor cursor = getActivity().getContentResolver().query(ReviewEntity.buildReviewUri(movieId),
                projection,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                MovieReviewDetail movieReviewDetail = new MovieReviewDetail();
                movieReviewDetail.setId(cursor.getString(cursor.getColumnIndex("review_id")));
                movieReviewDetail.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                movieReviewDetail.setContent(cursor.getString(cursor.getColumnIndex("content")));
                movieReviewDetail.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                reviews.add(movieReviewDetail);

            } while (cursor.moveToNext());
        }

        return reviews;
    }

    private void seeReviewMovies(Movie movie) {
        if (movie.getReviews().size() > 0) {
            int j = 0;
            for (MovieReviewDetail r : movie.getReviews()) {
                Log.i("TestMovieReview", "Review ".concat(String.valueOf(j)).concat(": ").concat(r.getId()));
                j++;
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("optionSelected", option_selected);
        super.onSaveInstanceState(outState);
    }
}
