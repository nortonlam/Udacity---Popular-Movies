package com.nortonlam.popularmovies.net;

import com.nortonlam.popularmovies.model.MovieResults;
import com.nortonlam.popularmovies.model.TmdbConfiguration;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by norton on 10/7/15.
 */
public interface TheMovieDbApi {
    String VERSION = "/3";

    @GET(VERSION + "/configuration")
    Call<TmdbConfiguration> getConfiguration(@Query("api_key") String apiKey);

    @GET(VERSION + "/discover/movie?sort_by=popularity.desc")
    Call<MovieResults> getMovieListByPopularity(@Query("api_key") String apiKey);
}
