package com.fukuhara.douglas.popularmoviesstage2.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 18/11/17.
 */

public class ReviewsList {
    @SerializedName("results")
    private ArrayList<Review> reviews;

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
