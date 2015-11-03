package com.example.cristhian.popmovies;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristhian.popmovies.models.MovieEntity;
import com.example.cristhian.popmovies.models.VideoEntity;
import com.example.cristhian.popmovies.service.MovieProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailMovieFragment extends Fragment implements IDetailMovie {

    private final String LOG_TAG = DetailMovieFragment.class.getSimpleName();
    private Movie movie;
    TextView textView;
    ImageView imageView;
    TextView textViewOverview;
    TextView yearTextView;
    TextView durationTextView;
    TextView rateTextView;

    ImageView image_header_detail;

    Button favoriteButton;

    Button readFavoriteButton;

    MovieDetail movieDetail;

    LinearLayout lm;

    MovieProvider movieProvider;

    public DetailMovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            movieDetail = savedInstanceState.getParcelable("movieDetail");
        }
        movieProvider = new MovieProvider();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_movie_fragment_layout, container, false);


//        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        getActivity().setTitle("Movie Detail");

        textView = (TextView) rootView.findViewById(R.id.detail_text);
        imageView = (ImageView) rootView.findViewById(R.id.image_detail);
        textViewOverview = (TextView) rootView.findViewById(R.id.overview_text);
        yearTextView = (TextView) rootView.findViewById(R.id.year_text);
        durationTextView = (TextView) rootView.findViewById(R.id.duration_text);
        rateTextView = (TextView) rootView.findViewById(R.id.rate_text);

        image_header_detail = (ImageView) rootView.findViewById(R.id.image_header_detail);

        favoriteButton = (Button) rootView.findViewById(R.id.favoriteButton);

        readFavoriteButton = (Button) rootView.findViewById(R.id.readFavoriteButton);

        lm = (LinearLayout) rootView.findViewById(R.id.videosLayout);

        if (movieDetail == null) {
            movieDetail = new MovieDetail();
        } else {
            responseDetailMovie(movieDetail);
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MovieEntity.COLUMN_MOVIE_ID, movieDetail.getId());
                values.put(MovieEntity.COLUMN_BACKDROP_PATH, movieDetail.getBackdrop_path());
                values.put(MovieEntity.COLUMN_ORIGINAL_TITLE, movieDetail.getOriginal_title());
                values.put(MovieEntity.COLUMN_OVERVIEW, movieDetail.getOverview());
                values.put(MovieEntity.COLUMN_POSTER_PATH, movieDetail.getPoster_path());
                values.put(MovieEntity.COLUMN_RELEASE_DATE, movieDetail.getRelease_date());
                values.put(MovieEntity.COLUMN_RUNTIME, movieDetail.getRuntime());
                values.put(MovieEntity.COLUMN_VOTE_AVERAGE, movieDetail.getVote_average());

                Uri uri = getActivity().getContentResolver().insert(
                        MovieEntity.CONTENT_URI, values);

                for (MovieVideoDetail v : movieDetail.getVideos()) {
                    ContentValues valuesVideo = new ContentValues();
                    valuesVideo.put(VideoEntity.COLUMN_MOV_KEY, movieDetail.getId());
                    valuesVideo.put(VideoEntity.COLUMN_KEY, v.getKey());
                    valuesVideo.put(VideoEntity.COLUMN_NAME, v.getName());
                    valuesVideo.put(VideoEntity.COLUMN_SITE, v.getSite());
                    valuesVideo.put(VideoEntity.COLUMN_SIZE, v.getSize());
                    valuesVideo.put(VideoEntity.COLUMN_TYPE, v.getType());
                    valuesVideo.put(VideoEntity.COLUMN_VIDEO_ID, v.getId());

                    Uri uriVideo = getActivity().getContentResolver().insert(
                            VideoEntity.CONTENT_URI, valuesVideo);
                }

                Toast.makeText(getActivity(), "Movie Registered", Toast.LENGTH_LONG).show();
            }
        });

        readFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] projection = {
                        MovieEntity._ID,
                        MovieEntity.COLUMN_MOVIE_ID,
                        MovieEntity.COLUMN_BACKDROP_PATH,
                        MovieEntity.COLUMN_ORIGINAL_TITLE,
                        MovieEntity.COLUMN_OVERVIEW,
                        MovieEntity.COLUMN_POSTER_PATH,
                        MovieEntity.COLUMN_RELEASE_DATE,
                        MovieEntity.COLUMN_RUNTIME,
                        MovieEntity.COLUMN_VOTE_AVERAGE
                };

                // Defines a string to contain the selection clause
                selectionClause = null;

                // An array to contain selection arguments
                selectionArgs = null;

                // Gets a word from the UI
                String searchString = "";
                if (movieDetail != null && movieDetail.getId() != null) {
                    searchString = movieDetail.getId().toString();
                    Log.i("TESTMovieSelected", "Movie Selected: ".concat(movieDetail.getId().toString()).concat(" - ").concat(movieDetail.getOriginal_title()));
                    Log.i("TESTsearchString", "Param busqueda searchString: ".concat(searchString));
                }

                selectionClause = MovieEntity.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{searchString};

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

                    } while (cursor.moveToNext());
                }

                Toast.makeText(getActivity(), "Movie title: ".concat(original_title), Toast.LENGTH_LONG).show();
                Log.i("TEST3", "Titulo pelicula3: ".concat(original_title));

            }
        });

        Log.e("TEST", "onCreate");

        return rootView;
    }


    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void updateInfo(Movie mv) {
        if (mv != null) {
            if (mv.isFavorite() == false) {
                PopularDetailsMovieTask popularDetailsMovieTask = new PopularDetailsMovieTask(this);
                popularDetailsMovieTask.execute(mv.getId().toString());
            } else {
                buildFavoriteMovie(mv);
            }
        }
    }

    @Override
    public void responseDetailMovie(final MovieDetail movieDetail) {
        Log.e("TEST", "responseDetailMovie");
        if (movieDetail != null) {
            lm.removeAllViews();
            this.movieDetail = movieDetail;
            if (image_header_detail != null) {

                String baseURLx = "http://image.tmdb.org/t/p/w780/";
                String itemx = baseURLx.concat(movieDetail.getBackdrop_path());
                Picasso.with(getActivity()).load(itemx).noFade().into(image_header_detail);


                textView.setText(movieDetail.getOriginal_title());
                String baseURL = "http://image.tmdb.org/t/p/w342/";
                String item = baseURL.concat(movieDetail.getPoster_path());
                Picasso.with(getActivity()).load(item).noFade().into(imageView);
                String[] yearVector = movieDetail.getRelease_date().split("-");
                yearTextView.setText("Year: " + yearVector[0]);
                durationTextView.setText("Duration: " + movieDetail.getRuntime().toString().concat(" min"));
                rateTextView.setText("Average Rating: " + movieDetail.getVote_average().toString().concat("/10"));
                textViewOverview.setText(movieDetail.getOverview());

//                final LinearLayout lm = (LinearLayout) getActivity().findViewById(R.id.videosLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT);


                for (int i = 0; i < movieDetail.getVideos().size(); i++) {
                    MovieVideoDetail movieVideoDetail = movieDetail.getVideos().get(i);
                    final String videoKey = movieVideoDetail.getKey();
                    String urlBaseVideo = "http://img.youtube.com/vi/".concat(videoKey).concat("/hqdefault.jpg");

                    // Create LinearLayout
                    LinearLayout ll = new LinearLayout(getActivity());
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    ImageView trailerImageView = new ImageView(getActivity());

                    trailerImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent videoClient = new Intent(Intent.ACTION_VIEW);
                            videoClient.setData(Uri.parse("https://www.youtube.com/watch?v=" + videoKey));
                            startActivity(videoClient);
                        }
                    });


                    Picasso.with(getActivity()).load(urlBaseVideo).noFade().into(trailerImageView);
                    trailerImageView.setLayoutParams(params);
                    ll.addView(trailerImageView);

                    // Create TextView
                    TextView trailerTextView = new TextView(getActivity());
                    trailerTextView.setText("Trailer " + (i + 1));
                    ll.addView(trailerTextView);

                    lm.addView(ll);
                }

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movieDetail", movieDetail);
        super.onSaveInstanceState(outState);
    }

    private void buildFavoriteMovie(Movie mv) {
        if (mv != null) {
            MovieDetail movieDetail = new MovieDetail();
            movieDetail.setId(mv.getId());
            movieDetail.setOriginal_title(mv.getOriginal_title());
            movieDetail.setPoster_path(mv.getPoster_path());
            movieDetail.setBackdrop_path(mv.getBackdrop_path());
            movieDetail.setOverview(mv.getOverview());
            movieDetail.setRelease_date(mv.getRelease_date());
            movieDetail.setRuntime(mv.getRuntime());
            movieDetail.setVote_average(mv.getVote_average());

            if (mv.getVideos().size() > 0 && !mv.getVideos().isEmpty()) {
                movieDetail.setVideos(mv.getVideos());
            } else {
                movieDetail.setVideos(new ArrayList<MovieVideoDetail>());
            }

            responseDetailMovie(movieDetail);
        }
    }

    // Defines a string to contain the selection clause
    String selectionClause;

    // An array to contain selection arguments
    String[] selectionArgs;
}
