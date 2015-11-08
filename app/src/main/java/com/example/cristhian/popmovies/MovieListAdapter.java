package com.example.cristhian.popmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends ArrayAdapter<Movie> {

    private final Activity context;
    private final List<Movie> movies;

    public MovieListAdapter(Activity context, List<Movie> movies) {
        super(context, R.layout.movie_list_layout, movies);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.movies = movies;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.movie_list_layout, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);

        if (movies != null) {
            if (movies.size() != 0 && !movies.isEmpty()) {

                if (movies.get(position).getPoster_path() != null) {
                    String baseURL = "http://image.tmdb.org/t/p/w500/";
                    String item = baseURL.concat(movies.get(position).getPoster_path());                            ;
                    Picasso.with(context).load(item).placeholder(R.drawable.placeholder).noFade().into(imageView);
                }
            }
        }

        return rowView;

    }
}
