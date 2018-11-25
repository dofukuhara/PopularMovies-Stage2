package com.fukuhara.douglas.popularmoviesstage2.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 18/11/17.
 */

public class VideosList {
    @SerializedName("results")
    private ArrayList<Video> videos;

    public ArrayList<Video> getVideos() {
        return videos;
    }
}
