package com.example.cristhian.popmovies;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class PopularDetailsMovieTask extends AsyncTask<String, MovieDetail, MovieDetail> {

    private final String LOG_TAG = PopularDetailsMovieTask.class.getSimpleName();

    private static final String API_KEY = "b237a19b878581bd1bb981cd41555945";

    private IDetailMovie iDetailMovie;

    public PopularDetailsMovieTask(IDetailMovie iDetailMovie) {
        this.iDetailMovie = iDetailMovie;
    }

    private String searchDetailMovie(String id) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        String movieId = id;


        try {
            final String URL = "http://api.themoviedb.org/3/movie/".concat(movieId).concat("?");
            final String API_KEY_PARAM = "api_key";

            OkHttpClient client = new OkHttpClient();
            String urlWithParams = URL + API_KEY_PARAM + "=" + API_KEY;

            Request request = new Request.Builder()
                    .url(urlWithParams)
                    .build();


            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
//
//
//            Uri builtUri = Uri.parse(URL).buildUpon().appendQueryParameter(API_KEY_PARAM, API_KEY).build();
//            java.net.URL url = new URL(builtUri.toString());
//
//            Log.v(LOG_TAG, "Built URI " + builtUri.toString());
//
//            // Create the request to OpenWeatherMap, and open the connection
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                forecastJsonStr = null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                forecastJsonStr = null;
//            }
//            forecastJsonStr = buffer.toString();
//
//            Log.i(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);
//
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error ", e);
//            forecastJsonStr = null;
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(LOG_TAG, "Error closing stream", e);
//                }
//            }
//        }
//        return forecastJsonStr;
    }

    public String searchVideosMovie(String id) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieVideoJsonStr = null;
        String movieId = id;

        try {
            final String URL = "http://api.themoviedb.org/3/movie/".concat(movieId).concat("/videos").concat("?");
            final String API_KEY_PARAM = "api_key";

            OkHttpClient client = new OkHttpClient();
            String urlWithParams = URL + API_KEY_PARAM + "=" + API_KEY;

            Request request = new Request.Builder()
                    .url(urlWithParams)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
//
//
//            Uri builtUri = Uri.parse(URL).buildUpon().appendQueryParameter(API_KEY_PARAM, API_KEY).build();
//            java.net.URL url = new URL(builtUri.toString());
//
//            Log.v(LOG_TAG, "Built URI " + builtUri.toString());
//
//            // Create the request to OpenWeatherMap, and open the connection
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                movieVideoJsonStr = null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                movieVideoJsonStr = null;
//            }
//            movieVideoJsonStr = buffer.toString();
//
//            Log.i(LOG_TAG, "Movie videos JSON String: " + movieVideoJsonStr);
//
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error ", e);
//            movieVideoJsonStr = null;
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(LOG_TAG, "Error closing stream", e);
//                }
//            }
//        }
//
//        return movieVideoJsonStr;

    }

    private MovieDetail getMovieData(String movieData, String movieVideoData) throws JSONException {
        final String RESULTS = "results";
        MovieDetail movieDetail = new MovieDetail();
        List<MovieVideoDetail> videos = new ArrayList<>();
        JSONObject forecastJson = new JSONObject(movieData);
        JSONObject movieVideosJson = new JSONObject(movieVideoData);
        JSONArray videosArray = movieVideosJson.getJSONArray(RESULTS);

        for (int i = 0; i < videosArray.length(); i++) {
            videos.add(new MovieVideoDetail(videosArray.getJSONObject(i)));
        }
        movieDetail.setVideos(videos);

        movieDetail.setOriginal_title(forecastJson.getString("original_title"));
        movieDetail.setOverview(forecastJson.getString("overview"));
        movieDetail.setPoster_path(forecastJson.getString("poster_path"));
        movieDetail.setRelease_date(forecastJson.getString("release_date"));
        movieDetail.setRuntime(forecastJson.getInt("runtime"));
        movieDetail.setVote_average(forecastJson.getDouble("vote_average"));
        movieDetail.setBackdrop_path(forecastJson.getString("backdrop_path"));
        return movieDetail;
    }

    protected MovieDetail doInBackground(String... params) {
        MovieDetail movieDetailTemp = new MovieDetail();
        if (searchDetailMovie(params[0]) != null) {
            String movieData = searchDetailMovie(params[0]);
            String movieVideoData = searchVideosMovie(params[0]);
            try {
                if (getMovieData(movieData, movieVideoData) != null) {
                    movieDetailTemp = getMovieData(movieData, movieVideoData);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }
        return movieDetailTemp;
    }

    @Override
    protected void onPostExecute(MovieDetail result) {
        if (result != null) {
            iDetailMovie.responseDetailMovie(result);
        }
    }


}