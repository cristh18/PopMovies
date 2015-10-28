package com.example.cristhian.popmovies;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    MovieDetail movieDetail;

    LinearLayout lm;

    public DetailMovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            movieDetail = savedInstanceState.getParcelable("movieDetail");
        }
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

        lm = (LinearLayout) rootView.findViewById(R.id.videosLayout);

        if (movieDetail == null) {
            movieDetail = new MovieDetail();
        } else {
            responseDetailMovie(movieDetail);
        }

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
            PopularDetailsMovieTask popularDetailsMovieTask = new PopularDetailsMovieTask(this);
            popularDetailsMovieTask.execute(mv.getId().toString());
        }
    }

    @Override
    public void responseDetailMovie(final MovieDetail movieDetail) {
        Log.e("TEST", "responseDetailMovie");
        if (movieDetail != null) {
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
}
