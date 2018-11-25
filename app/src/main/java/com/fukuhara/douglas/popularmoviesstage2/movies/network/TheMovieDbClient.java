package com.fukuhara.douglas.popularmoviesstage2.movies.network;

import com.fukuhara.douglas.popularmoviesstage2.movies.model.MoviesList;
import com.fukuhara.douglas.popularmoviesstage2.movies.model.ReviewsList;
import com.fukuhara.douglas.popularmoviesstage2.movies.model.VideosList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dofukuhara on 18/11/17.
 */

public interface TheMovieDbClient {

    @GET("/3/movie/{sort}")
    Call<MoviesList> listOfMovies(@Path("sort") String sort, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<ReviewsList> listOfReviews(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<VideosList> listOfVideos(@Path("id") String id, @Query("api_key") String apiKey);
}
