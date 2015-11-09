package com.example.cristhian.popmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Cristhian on 11/8/2015.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    protected ImageView imageView;

    public MovieViewHolder(View v) {
        super(v);
        imageView = (ImageView) v.findViewById(R.id.image);
    }

}
