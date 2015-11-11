package com.example.cristhian.popmovies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristhian.popmovies.models.MovieEntity;
import com.example.cristhian.popmovies.models.ReviewEntity;
import com.example.cristhian.popmovies.models.VideoEntity;
import com.example.cristhian.popmovies.service.MovieProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

    Button removefavoriteButton;

    TextView trailers;

    TextView reviews;

    MovieDetail movieDetail;

    LinearLayout lm;

    LinearLayout lr;

    MovieProvider movieProvider;

    Movie favoriteMovie;

    Menu mMenu;

    // Defines a string to contain the selection clause
    String selectionClause;

    // An array to contain selection arguments
    String[] selectionArgs;

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
        favoriteButton.setVisibility(View.INVISIBLE);

        removefavoriteButton = (Button) rootView.findViewById(R.id.removeFavoriteButton);
        removefavoriteButton.setVisibility(View.INVISIBLE);

        trailers = (TextView) rootView.findViewById(R.id.trailers);

        reviews = (TextView) rootView.findViewById(R.id.reviews);

        lm = (LinearLayout) rootView.findViewById(R.id.videosLayout);

        lr = (LinearLayout) rootView.findViewById(R.id.reviewsLayout);

        if (movieDetail == null) {
            movieDetail = new MovieDetail();
        } else {
            responseDetailMovie(movieDetail);
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFavoriteMovie(movieDetail.getId()) == true) {
                    Toast.makeText(getActivity(), "This film is already selected as favorite", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(MovieEntity.COLUMN_MOVIE_ID, movieDetail.getId());
                    values.put(MovieEntity.COLUMN_BACKDROP_PATH, movieDetail.getBackdrop_path());
                    values.put(MovieEntity.COLUMN_ORIGINAL_TITLE, movieDetail.getOriginal_title());
                    values.put(MovieEntity.COLUMN_OVERVIEW, movieDetail.getOverview());
                    values.put(MovieEntity.COLUMN_POSTER_PATH, movieDetail.getPoster_path());
                    values.put(MovieEntity.COLUMN_RELEASE_DATE, movieDetail.getRelease_date());
                    values.put(MovieEntity.COLUMN_RUNTIME, movieDetail.getRuntime());
                    values.put(MovieEntity.COLUMN_VOTE_AVERAGE, movieDetail.getVote_average());

                    if (movieDetail.getPopularity() != null) {
                        values.put(MovieEntity.COLUMN_POPULARITY, movieDetail.getPopularity());
                    } else {
                        values.put(MovieEntity.COLUMN_POPULARITY, 0.0);
                    }

                    if (movieDetail.getVote_count() != null) {
                        values.put(MovieEntity.COLUMN_VOTE_COUNT, movieDetail.getVote_count());
                    } else {
                        values.put(MovieEntity.COLUMN_VOTE_COUNT, 0);
                    }

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

                    for (MovieReviewDetail v : movieDetail.getReviews()) {
                        ContentValues valuesReview = new ContentValues();
                        valuesReview.put(ReviewEntity.COLUMN_MOV_KEY, movieDetail.getId());
                        valuesReview.put(ReviewEntity.COLUMN_REVIEW_ID, v.getId());
                        valuesReview.put(ReviewEntity.COLUMN_AUTHOR, v.getAuthor());
                        valuesReview.put(ReviewEntity.COLUMN_CONTENT, v.getContent());
                        valuesReview.put(ReviewEntity.COLUMN_URL, v.getUrl());

                        Uri uriReview = getActivity().getContentResolver().insert(
                                ReviewEntity.CONTENT_URI, valuesReview);
                    }

                    Toast.makeText(getActivity(), "Movie Registered", Toast.LENGTH_LONG).show();
                }
            }
        });

        removefavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFavoriteMovieReviews(movieDetail.getId());
                removeFavoriteMovieVideos(movieDetail.getId());
                removeFavoriteMovie(movieDetail.getId());

                FragmentManager fragmentManager = getFragmentManager();
                MoviesFragment moviesFragment = (MoviesFragment) fragmentManager.findFragmentById(R.id.movie_frag);
                if (moviesFragment == null) {
                    getActivity().onBackPressed();
                } else {
                    clearScreen();
                    moviesFragment.updateInfo("searchFavorites");
                }
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
                favoriteButton.setVisibility(View.VISIBLE);
                removefavoriteButton.setVisibility(View.INVISIBLE);
                PopularDetailsMovieTask popularDetailsMovieTask = new PopularDetailsMovieTask(this);
                popularDetailsMovieTask.execute(mv.getId().toString());
            } else {
                favoriteButton.setVisibility(View.INVISIBLE);
                removefavoriteButton.setVisibility(View.VISIBLE);
                buildFavoriteMovie(mv);
            }
        }
    }

    @Override
    public void responseDetailMovie(final MovieDetail movieDetail) {
        Log.e("TEST", "responseDetailMovie");
        if (movieDetail != null) {
            lm.removeAllViews();
            lr.removeAllViews();
            this.movieDetail = movieDetail;
            if (image_header_detail != null) {

                String baseURLx = "http://image.tmdb.org/t/p/w780/";
                String itemx = baseURLx.concat(movieDetail.getBackdrop_path());
                Picasso.with(getActivity()).load(itemx).placeholder(R.drawable.placeholder).noFade().into(image_header_detail);


                textView.setText(movieDetail.getOriginal_title());
                String baseURL = "http://image.tmdb.org/t/p/w342/";
                String item = baseURL.concat(movieDetail.getPoster_path());
                Picasso.with(getActivity()).load(item).placeholder(R.drawable.placeholder).noFade().into(imageView);
                String[] yearVector = movieDetail.getRelease_date().split("-");
                yearTextView.setText("Year: " + yearVector[0]);
                durationTextView.setText("Duration: " + movieDetail.getRuntime().toString().concat(" min"));
                rateTextView.setText("Average Rating: " + movieDetail.getVote_average().toString().concat("/10"));
                textViewOverview.setText(movieDetail.getOverview());

                if (movieDetail.getPopularity() == null) {
                    movieDetail.setPopularity(0.0);
                }

                if (movieDetail.getVote_count() == null) {
                    movieDetail.setVote_count(0);
                }

//                final LinearLayout lm = (LinearLayout) getActivity().findViewById(R.id.videosLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT);

                trailers.setText("Trailers:");

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


                    Picasso.with(getActivity()).load(urlBaseVideo).placeholder(R.drawable.placeholder).noFade().into(trailerImageView);
                    trailerImageView.setLayoutParams(params);
                    ll.addView(trailerImageView);

                    // Create TextView
                    TextView trailerTextView = new TextView(getActivity());
                    trailerTextView.setText("Trailer " + (i + 1));
                    ll.addView(trailerTextView);

                    lm.addView(ll);
                }

                reviews.setText("Reviews:");

                for (int i = 0; i < movieDetail.getReviews().size(); i++) {
                    MovieReviewDetail movieReviewDetail = movieDetail.getReviews().get(i);

                    TextView reviewTitle = new TextView(getActivity());
                    reviewTitle.setText("Review " + (i + 1));
                    lr.addView(reviewTitle);

                    TextView reviewText = new TextView(getActivity());
                    reviewText.setText(movieReviewDetail.getContent());
                    lr.addView(reviewText);
                }

            }
        }

        restartMenu();
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

            if (mv.getReviews().size() > 0 && !mv.getReviews().isEmpty()) {
                movieDetail.setReviews(mv.getReviews());
            } else {
                movieDetail.setReviews(new ArrayList<MovieReviewDetail>());
            }

            responseDetailMovie(movieDetail);
        }
    }

    private Movie isFavoriteMovie(Long movieId) {
        favoriteMovie = new Movie();

        String[] projection = {
                MovieEntity._ID,
                MovieEntity.COLUMN_MOVIE_ID,
                MovieEntity.COLUMN_ORIGINAL_TITLE
        };

        Cursor cursor = getActivity().getContentResolver().query(MovieEntity.buildMovieUri(movieId),
                projection,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getLong(cursor.getColumnIndex("movie_id")));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex("original_title")));
                favoriteMovie = movie;

            } while (cursor.moveToNext());
        }

        return favoriteMovie;
    }

    private boolean validateFavoriteMovie(Long movieId) {
        boolean isFavorite = false;
        if (isFavoriteMovie(movieId).getId() != null) {
            isFavorite = true;
        } else {
            isFavorite = false;
        }
        return isFavorite;
    }

    private boolean removeFavoriteMovieReviews(Long movie_id) {
        boolean result = false;
        String selection = "movie_id = \"" + movie_id + "\"";
        int rowsDeleted = getActivity().getContentResolver().delete(
                ReviewEntity.buildReviewUri(movie_id), selection, null);

        if (rowsDeleted > 0) {
            result = true;
        }

        return result;
    }

    private boolean removeFavoriteMovieVideos(Long movie_id) {
        boolean result = false;
        String selection = "movie_id = \"" + movie_id + "\"";
        int rowsDeleted = getActivity().getContentResolver().delete(
                VideoEntity.buildVideoUri(movie_id), selection, null);

        if (rowsDeleted > 0) {
            result = true;
        }

        return result;
    }

    private boolean removeFavoriteMovie(Long movie_id) {
        boolean result = false;
        String selection = "movie_id = \"" + movie_id + "\"";
        int rowsDeleted = getActivity().getContentResolver().delete(
                MovieEntity.buildMovieUri(movie_id), selection, null);

        if (rowsDeleted > 0) {
            result = true;
        }

        return result;
    }

    private void clearScreen() {
        ImageView image_header_detail = (ImageView) getActivity().findViewById(R.id.image_header_detail);
        image_header_detail.setImageBitmap(null);

        TextView detail_text = (TextView) getActivity().findViewById(R.id.detail_text);
        detail_text.setText("");

        ImageView image_detail = (ImageView) getActivity().findViewById(R.id.image_detail);
        image_detail.setImageBitmap(null);

        TextView year_text = (TextView) getActivity().findViewById(R.id.year_text);
        year_text.setText("");

        TextView duration_text = (TextView) getActivity().findViewById(R.id.duration_text);
        duration_text.setText("");

        TextView rate_text = (TextView) getActivity().findViewById(R.id.rate_text);
        rate_text.setText("");

        Button favoriteButton = (Button) getActivity().findViewById(R.id.favoriteButton);
        favoriteButton.setVisibility(View.INVISIBLE);

        Button removeFavoriteButton = (Button) getActivity().findViewById(R.id.removeFavoriteButton);
        removeFavoriteButton.setVisibility(View.INVISIBLE);

        TextView overview_text = (TextView) getActivity().findViewById(R.id.overview_text);
        overview_text.setText("");

        TextView separator = (TextView) getActivity().findViewById(R.id.separator);
        separator.setText("");

        TextView trailers = (TextView) getActivity().findViewById(R.id.trailers);
        trailers.setText("");

        lm.removeAllViews();

        TextView separator2 = (TextView) getActivity().findViewById(R.id.separator2);
        separator2.setText("");

        TextView reviews = (TextView) getActivity().findViewById(R.id.reviews);
        reviews.setText("");

        lr.removeAllViews();
    }

    private Intent createShareTrailerMovieIntent() {
        String videoKey = "";
        if (movieDetail != null) {
            if (movieDetail.getVideos() != null && movieDetail.getVideos().size() > 0)
                videoKey = movieDetail.getVideos().get(0).getKey();
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=".concat(videoKey));
        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        mMenu = menu;
        menuInflater.inflate(R.menu.menu_detail_movie, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareTrailerMovieIntent());
        } else {
            Log.i(LOG_TAG, "is null");
        }
    }

    private void restartMenu() {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.action_share);
            if (item != null) {
                item.setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this.getActivity());
            }
        }
    }
}
