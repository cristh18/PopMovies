package com.example.cristhian.popmovies;

import org.json.JSONObject;

/**
 * Created by Cristhian on 01/08/2015.
 */
public class MovieVideoDetail {

    private String id;

    private String iso_639_1;

    private String key;

    private String name;

    private String site;

    private Integer size;

    private String type;

    public MovieVideoDetail(JSONObject jsonObject){
        this.id=jsonObject.optString("id");
        this.iso_639_1=jsonObject.optString("iso_639_1");
        this.key=jsonObject.optString("key");
        this.name=jsonObject.optString("name");
        this.site=jsonObject.optString("site");
        this.size=jsonObject.optInt("size");
        this.type=jsonObject.optString("type");
    }

    public  MovieVideoDetail(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
