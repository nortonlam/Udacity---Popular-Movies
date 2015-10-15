package com.nortonlam.popularmovies.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nortonlam.popularmovies.util.LoggingInterceptor;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by norton
 *
 * Created date: 10/7/15.
 */
public class TheMovieDb {
    public final static String BASE_URL = "http://api.themoviedb.org";

    private static TheMovieDbApi _theMovieApi;

    static {
        setupRestClient();
    }

    private TheMovieDb() {}

    public static TheMovieDbApi getApi() {
        return _theMovieApi;
    }

    private static void setupRestClient() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor("TheMovieDb"));

        Gson gsonDateParser = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonDateParser))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client);

        Retrofit retrofit = builder.build();
        _theMovieApi = retrofit.create(TheMovieDbApi.class);
    }
}
