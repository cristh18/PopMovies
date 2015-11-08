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
        setContentView(R.layout.activity_main);
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


        FragmentManager fragmentManager = getFragmentManager();
        DetailMovieFragment detailMovieFragment = (DetailMovieFragment) fragmentManager.findFragmentById(R.id.detail_movie_frag);
        if (detailMovieFragment == null) {

            Intent intent = new Intent(this, DetailMovieActivity.class);

            Bundle mBundle = new Bundle();
            mBundle.putParcelable("movie", movie);
            intent.putExtras(mBundle);

            startActivity(intent);
        } else {
            detailMovieFragment.updateInfo(movie);
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
