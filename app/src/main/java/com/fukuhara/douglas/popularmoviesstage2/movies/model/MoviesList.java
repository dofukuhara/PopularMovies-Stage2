package com.fukuhara.douglas.popularmoviesstage2.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 18/11/17.
 */

public class MoviesList {
    @SerializedName("results")
    private ArrayList<Movie> movies;

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}
