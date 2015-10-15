package com.nortonlam.popularmovies;

import android.app.Application;

import com.nortonlam.popularmovies.model.TmdbConfiguration;

/**
 * Created by norton
 *
 * Created date: 10/12/15.
 */
public class PopularMoviesApplication extends Application {
    private TmdbConfiguration _config;

    public void setConfig(TmdbConfiguration config) {
        _config = config;
    }

    // Return the base image url as defined by the TMDB configuration
    public String getImageBaseUrl() {
        return _config.getImages().getFullBaseUrl();
    }
}
