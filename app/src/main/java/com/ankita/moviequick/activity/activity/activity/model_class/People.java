package com.ankita.moviequick.activity.activity.activity.model_class;

public class People {

    public String name;
    String profilePath;
    private String profileUrl;
    public int id;
    private final static String TMDB_IMG_BASE_URL = "http://image.tmdb.org/t/p/";

    People() {
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    void setProfileUrl(String profilePath) {
        profileUrl = TMDB_IMG_BASE_URL + "w300" + profilePath;
    }

    void setId(int id){
        this.id = id;
    }
}

