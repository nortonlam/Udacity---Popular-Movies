package com.nortonlam.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nortonlam.popularmovies.PopularMoviesApplication;
import com.nortonlam.popularmovies.R;
import com.nortonlam.popularmovies.model.TmdbConfiguration;
import com.nortonlam.popularmovies.net.TheMovieDb;
import com.nortonlam.popularmovies.net.TheMovieDbApi;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by norton on 10/12/15.
 */
public class StartActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TheMovieDbApi theMovieDbApi = TheMovieDb.get();
        String apiKey = getResources().getString(R.string.themoviedb_key);

        // Get image configuration
        Call<TmdbConfiguration> call = theMovieDbApi.getConfiguration(apiKey);
        call.enqueue(new ConfigurationCallback());
    }

    private void nextScreen() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    class ConfigurationCallback implements Callback<TmdbConfiguration> {
        @Override
        public void onResponse(Response<TmdbConfiguration> response, Retrofit retrofit) {
            int statusCode = response.code();
            Log.d("ConfigurationCallback", "statusCode: " + statusCode);

            TmdbConfiguration config = response.body();
            Log.d("ConfigurationCallback", "image full path: " + config.getImages().getFullBaseUrl());

            ((PopularMoviesApplication)getApplication()).setConfig(config);

            nextScreen();
        }

        @Override
        public void onFailure(Throwable t) {
            // Log error here since request failed
            Log.d("MovieResultsCallback", t.toString());
        }
    }
}
