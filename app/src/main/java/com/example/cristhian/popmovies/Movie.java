package com.example.cristhian.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {

    private Long id;

    private boolean adult;

    private String backdrop_path;

    private List<Integer> genre_ids;

    private String original_language;

    private String original_title;

    private String overview;

    private String release_date;

    private String poster_path;

    private Double popularity;

    private String title;

    private boolean video;

    private Double vote_average;

    private Integer vote_count;

    private boolean favorite;

    private Integer runtime;

    public Movie(JSONObject jsonObject) {
        //this.id = Long.parseLong(jsonObject.optString("id").toString());

        try {
            this.id = jsonObject.optLong("id");
            this.adult = jsonObject.optBoolean("adult");
            this.backdrop_path = jsonObject.optString("backdrop_path");
            JSONArray jsonTempArray = jsonObject.optJSONArray("genre_ids");
            this.genre_ids = getGenreIds(jsonTempArray);
            this.original_language = jsonObject.optString("original_language");
            this.original_title = jsonObject.optString("original_title");
            this.overview = jsonObject.optString("overview");
            this.release_date = jsonObject.optString("release_date");
            this.poster_path = jsonObject.optString("poster_path");
            this.popularity = jsonObject.optDouble("popularity");
            this.title = jsonObject.optString("title");
            this.video = jsonObject.optBoolean("video");
            this.vote_average = jsonObject.optDouble("vote_average");
            this.vote_count = jsonObject.optInt("vote_count");
//            this.favorite = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Movie() {
    }

    private List<Integer> getGenreIds(JSONArray jsonTempArray) throws JSONException {
        List<Integer> genre_ids_temp = new ArrayList<>();
        for (int j = 0; j < jsonTempArray.length(); j++) {
            genre_ids_temp.add(jsonTempArray.getInt(j));
        }
        return genre_ids_temp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public boolean isVideo() {
        return video;
    }

    public String getTitle() {
        return title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public Long getId() {
        return id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {

        public Movie createFromParcel(Parcel source) {

            Movie movie = new Movie();

            movie.id = source.readLong();
            movie.backdrop_path = source.readString();
//            movie.favorite = source.readByte() != 0;

            return movie;

        }

        public Movie[] newArray(int size) {

            return new Movie[size];

        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(backdrop_path);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(poster_path);
        parcel.writeDouble(popularity);
        parcel.writeString(title);
        parcel.writeDouble(vote_average);
        parcel.writeInt(vote_count);
//        parcel.writeByte((byte)(favorite ? 1 : 0));
    }
}
