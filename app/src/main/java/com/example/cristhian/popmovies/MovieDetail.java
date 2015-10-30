package com.example.cristhian.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Cristhian on 29/07/2015.
 */
public class MovieDetail implements Parcelable {

    private Long id;
    private String original_title;
    private String overview;
    private String poster_path;
    private String release_date;
    private Integer runtime;
    private Double vote_average;
    private String backdrop_path;
    private List<MovieVideoDetail> videos;

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }


    public List<MovieVideoDetail> getVideos() {
        return videos;
    }

    public void setVideos(List<MovieVideoDetail> videos) {
        this.videos = videos;
    }


    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static final Parcelable.Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {

        public MovieDetail createFromParcel(Parcel source) {

            MovieDetail movieDetail = new MovieDetail();

            movieDetail.id = source.readLong();
            movieDetail.original_title = source.readString();
            movieDetail.overview = source.readString();
            movieDetail.poster_path = source.readString();
            movieDetail.release_date = source.readString();
            movieDetail.runtime = source.readInt();
            movieDetail.vote_average = source.readDouble();
            movieDetail.backdrop_path = source.readString();
            movieDetail.videos = source.readArrayList(MovieDetail.class.getClassLoader());

            return movieDetail;

        }

        public MovieDetail[] newArray(int size) {

            return new MovieDetail[size];

        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(original_title);
        parcel.writeString(overview);
        parcel.writeString(poster_path);
        parcel.writeString(release_date);
        parcel.writeInt(runtime);
        parcel.writeDouble(vote_average);
        parcel.writeString(backdrop_path);
        parcel.writeList(videos);
    }
}
