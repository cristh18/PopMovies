package com.example.cristhian.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieViewHolder> {

//    ArrayAdapter<Movie>

    //private final Activity context;
    private final List<Movie> movies;
    private Context mContext;

    private OnItemClick mListener;

    public void setOnItemClickListener(OnItemClick listener) {
        mListener = listener;
    }

    public MovieListAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.movie_list_layout, viewGroup, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder movieViewHolder, final int position) {
        final Movie movie;
        if (movies != null && movies.size() != 0 && !movies.isEmpty()) {
            movie = movies.get(position);

            movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(v, position, movie);
                }
            });

            if (movie.getPoster_path() != null) {
                String baseURL = "http://image.tmdb.org/t/p/w500/";
                String item = baseURL.concat(movie.getPoster_path());
                Context context = movieViewHolder.imageView.getContext();
                Picasso.with(context).load(item).placeholder(R.drawable.placeholder).noFade().into(movieViewHolder.imageView);
            }
        }
    }

    private Movie getItem(int position) {
        return movies.get(position);
    }

    private Context getContext() {
        return mContext;
    }

    public void clear() {
        int size = this.movies.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.movies.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void add(Movie item) {
        movies.add(item);
        this.notifyDataSetChanged();
    }
}
