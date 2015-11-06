package com.example.cristhian.popmovies;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ctolosa on 06/11/2015.
 */
public class MovieReviewDetail implements Serializable {

    private String id;

    private String author;

    private String content;

    private String url;

    public MovieReviewDetail(JSONObject jsonObject){
        this.id=jsonObject.optString("id");
        this.author=jsonObject.optString("author");
        this.content=jsonObject.optString("content");
        this.url=jsonObject.optString("url");
    }

    public MovieReviewDetail(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
