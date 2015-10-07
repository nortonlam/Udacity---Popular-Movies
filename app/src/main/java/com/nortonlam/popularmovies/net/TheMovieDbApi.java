package com.nortonlam.popularmovies.net;

import com.nortonlam.popularmovies.model.MovieResults;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by norton on 10/7/15.
 */
public interface TheMovieDbApi {
    @GET("/3/discover/movie?sort_by=popularity.desc")
    Call<MovieResults> getPopularMovieList(@Query("api_key") String apiKey);
}
