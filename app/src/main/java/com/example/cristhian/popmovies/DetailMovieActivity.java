package com.example.cristhian.popmovies;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DetailMovieActivity extends AppCompatActivity {

    DetailMovieFragment detailMovieFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_activity_layout);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }else{
            Log.e("Test", "getActionBar null");
        }


        Movie movieDetail;
        Intent intent = getIntent();
        movieDetail = intent.getParcelableExtra("movie");
        if (savedInstanceState != null) {
            detailMovieFragment = (DetailMovieFragment) getFragmentManager().getFragment(savedInstanceState, "movieDetail");
        } else {
            detailMovieFragment = (DetailMovieFragment) getFragmentManager().findFragmentById(R.id.detail_movie_frag);
        }
        detailMovieFragment.updateInfo(movieDetail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getFragmentManager().putFragment(outState, "movieDetail", detailMovieFragment);
        super.onSaveInstanceState(outState);
    }
}
