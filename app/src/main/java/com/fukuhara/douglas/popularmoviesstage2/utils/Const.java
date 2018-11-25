package com.fukuhara.douglas.popularmoviesstage2.utils;

import com.fukuhara.douglas.popularmoviesstage2.BuildConfig;

/**
 * Created by dofukuhara on 18/11/17.
 */

public class Const {

    // API_KEY of theMovieDB read from 'gradle.properties'
    public static final String API_KEY = BuildConfig.API_KEY;

    // Const used to store the Base URL of theMoviesDB API
    public static final String THE_MOVIE_DB_BASE_URL = "http://api.themoviedb.org/";

    // Const used to store the Base URL of Image API of theMoviesDB API and the default Image Size value
    public static final String THE_MOVIE_DB_IMAGE_API_URL = "http://image.tmdb.org/t/p/";
    public static final String THE_MOVIE_DB_DEFAULT_IMAGE_SIZE = "w185";

    // Const used for theMovieDB sort type
    public static final String SORT_TYPE_POPULAR = "popular";
    public static final String SORT_TYPE_TOP_RATED = "top_rated";
}
