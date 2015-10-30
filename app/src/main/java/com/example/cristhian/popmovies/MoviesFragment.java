package com.example.cristhian.popmovies;

import android.app.Activity;
import android.app.Fragment;
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
            return true;
        }else if (id == R.id.action_highest_rated){
            PopularMoviesTask popularMoviesTask = new PopularMoviesTask();
            popularMoviesTask.execute("vote_average.desc");
            return true;
        }else if (id == R.id.action_favorites){
            searchFavorites();
            seeFavoriteMovies();
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
        PopularMoviesTask popularMoviesTask = new PopularMoviesTask();
        popularMoviesTask.execute(valueSorts);

        myGridMovieView = (GridView) view.findViewById(R.id.grid_view_pop_movies);

        myGridMovieView.setAdapter(customListAdapter);
        myGridMovieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = customListAdapter.getItem(i);
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

    private void searchFavorites(){

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
                MovieEntity.COLUMN_VOTE_AVERAGE
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
                movie.setFavorite(true);
                favoriteMovies.add(movie);

            } while (cursor.moveToNext());
        }
    }
    
    private void seeFavoriteMovies(){
        customListAdapter.clear();
        int i = 1;
        for (Movie m:favoriteMovies) {
            Log.i("TestMovie", "Movie ".concat(String.valueOf(i)).concat(": ").concat(m.getOriginal_title()));
            i++;
            customListAdapter.add(m);
        }

    }


    private List<Movie> favoriteMovies;

}