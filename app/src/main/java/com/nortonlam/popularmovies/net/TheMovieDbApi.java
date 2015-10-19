package com.nortonlam.popularmovies.net;

import com.nortonlam.popularmovies.model.MovieResults;
import com.nortonlam.popularmovies.model.TmdbConfiguration;
import com.nortonlam.popularmovies.model.Video;
import com.nortonlam.popularmovies.model.VideoResults;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by norton
 *
 * Created date: 10/7/15.
 */
public interface TheMovieDbApi {
    String VERSION = "/3";

    @GET(VERSION + "/configuration")
    Call<TmdbConfiguration> getConfiguration(@Query("api_key") String apiKey);

    @GET(VERSION + "/discover/movie")
    Call<MovieResults> getMovieList(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);

    @GET(VERSION + "/movie/{id}/videos")
    Call<VideoResults> getMovieTrailers(@Path("id") long id, @Query("api_key") String apiKey);
}
