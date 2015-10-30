package com.example.cristhian.popmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristhian on 9/29/2015.
 */
public class PopularMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    private final String LOG_TAG = PopularMoviesTask.class.getSimpleName();

    private String searchMovies(String valueSort) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        String apiKey = "b237a19b878581bd1bb981cd41555945";

        try {
            final String URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(URL).buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, valueSort)
                    .appendQueryParameter(API_KEY_PARAM, apiKey).build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();

            Log.i(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            forecastJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return forecastJsonStr;

    }

    private List<Movie> getMoviesData(String moviesData) throws JSONException {

        List<Movie> list_movie = new ArrayList<>();
        final String RESULTS = "results";


        JSONObject forecastJson = new JSONObject(moviesData);
        JSONArray moviesArray = forecastJson.getJSONArray(RESULTS);

        for (int i = 0; i < moviesArray.length(); i++) {
            list_movie.add(new Movie(moviesArray.getJSONObject(i)));
        }

        return list_movie;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        List<Movie> movies = new ArrayList<>();
        if (searchMovies(params[0]) != null) {
            String movieData = searchMovies(params[0]);
            try {
                if (getMoviesData(movieData).size() > 0 && !getMoviesData(movieData).isEmpty()) {
                    movies.addAll(getMoviesData(movieData));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> result) {
        if (result != null) {
            MoviesFragment.customListAdapter.clear();
            for (Movie a : result) {
                a.setFavorite(false);
                MoviesFragment.customListAdapter.add(a);
            }
        }
    }
}
