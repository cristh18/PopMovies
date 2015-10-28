package com.example.cristhian.popmovies;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Cristhian on 9/30/2015.
 */
public class MainActivity extends AppCompatActivity implements Communicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (savedInstanceState != null) {
//            setContentView(R.layout.detail_movie_fragment_layout);
//        } else {
            setContentView(R.layout.activity_main);
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_pop_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void response(Movie movie) {
//        FragmentManager fragmentManager = getFragmentManager();
//        DetailMovieFragment detailMovieFragment = (DetailMovieFragment) fragmentManager.findFragmentById(R.id.fragment2);
//        if (detailMovieFragment != null) {
//            detailMovieFragment.setMovie(movie);
//        } else {
//            DetailMovieFragment fragment = new DetailMovieFragment();
//            fragment.setMovie(movie);
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.pop_movies_container, fragment)
//                    .addToBackStack(null).commit();
//        }

        FragmentManager fragmentManager = getFragmentManager();
        DetailMovieFragment detailMovieFragment = (DetailMovieFragment) fragmentManager.findFragmentById(R.id.detail_movie_frag);
        if (detailMovieFragment == null) {
//            detailMovieFragment.setMovie(movie);
            Intent intent = new Intent(this, DetailMovieActivity.class);

            Bundle mBundle = new Bundle();
            mBundle.putParcelable("movie", movie);
            intent.putExtras(mBundle);

//            intent.putExtra("movie", (Parcelable)movie);

//            detailMovieFragment.setMovie(movie);
            startActivity(intent);
        } else {
//            DetailMovieFragment fragment = new DetailMovieFragment();
//            fragment.setMovie(movie);
            detailMovieFragment.updateInfo(movie);
        }
    }

//    @Override
//    public void responseDetail(MovieDetail movieDetail) {
//        DetailMovieFragment detailMovieFragment = (DetailMovieFragment)getFragmentManager().findFragmentById(R.id.detail_movie_frag);
//        if (detailMovieFragment == null){
//            Intent intent = new Intent(this, DetailMovieActivity.class);
//            intent.putExtra("movieDetail", (Parcelable)movieDetail);
//            startActivity(intent);
//        }else {
//            detailMovieFragment.updateInfo(movieDetail);
//        }
//    }


    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }


//    @Override
//    public void headlineSelected(String headline) {
//        MoviesFragment moviesFragment = (MoviesFragment)getFragmentManager().findFragmentById(R.id.article_frag);
//        if (moviesFragment == null){
//            Intent intent = new Intent(this, DetailMovieActivity.class);
//            intent.putExtra("headline", headline);
//            startActivity(intent);
//        }else {
//            moviesFragment.updateInfo(headline);
//        }
//
//    }
}
