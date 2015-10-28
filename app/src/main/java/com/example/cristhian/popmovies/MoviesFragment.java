package com.example.cristhian.popmovies;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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

}
