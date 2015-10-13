package com.nortonlam.popularmovies.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nortonlam.popularmovies.R;
import com.nortonlam.popularmovies.model.Movie;
import com.nortonlam.popularmovies.model.MovieResults;
import com.nortonlam.popularmovies.model.TmdbConfiguration;
import com.nortonlam.popularmovies.net.TheMovieDb;
import com.nortonlam.popularmovies.net.TheMovieDbApi;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    TmdbConfiguration _config;

    @Bind(R.id.gridview)
    GridView gridView;

    private List<Movie> _movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        TheMovieDbApi theMovieDbApi = TheMovieDb.get();
        String apiKey = getResources().getString(R.string.themoviedb_key);

        // Get image configuration
        Call<TmdbConfiguration> call = theMovieDbApi.getConfiguration(apiKey);
        call.enqueue(new ConfigurationCallback());
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get popular movies list
        TheMovieDbApi theMovieDbApi = TheMovieDb.get();
        String apiKey = getResources().getString(R.string.themoviedb_key);
        Call<MovieResults> call = theMovieDbApi.getPopularMovieList(apiKey);
        call.enqueue(new MovieResultsCallback());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUi(List<Movie> movieList) {
        gridView.setAdapter(new ImageAdapter(this, movieList));
    }

     class ConfigurationCallback implements Callback<TmdbConfiguration> {
        @Override
        public void onResponse(Response<TmdbConfiguration> response, Retrofit retrofit) {
            int statusCode = response.code();
            Log.d("ConfigurationCallback", "statusCode: " + statusCode);

            _config = response.body();
            Log.d("ConfigurationCallback", "image full path: " + _config.getImages().getFullBaseUrl());
        }

        @Override
        public void onFailure(Throwable t) {
            // Log error here since request failed
            Log.d("MovieResultsCallback", t.toString());
        }
    }

    class MovieResultsCallback implements Callback<MovieResults> {
        @Override
        public void onResponse(Response<MovieResults> response, Retrofit retrofit) {
            int statusCode = response.code();
            Log.d("MovieResultsCallback", "statusCode: " + statusCode);

            MovieResults results = response.body();
            List<Movie> movieList = results.getResults();

            updateUi(movieList);
        }

        @Override
        public void onFailure(Throwable t) {
            // Log error here since request failed
            Log.d("MovieResultsCallback", t.toString());
        }
    }

    class ImageAdapter extends BaseAdapter {
        private Context _context;
        private List<Movie> _movieList;

        public ImageAdapter(Context context, List<Movie> movieList) {
            _context = context;
            _movieList = movieList;
        }

        public int getCount() {
            return _movieList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            Movie movie = _movieList.get(position);

            ImageView posterImageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                posterImageView = new ImageView(_context);
                posterImageView.setLayoutParams(new GridView.LayoutParams(400, 400));
                posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                posterImageView.setPadding(8, 8, 8, 8);

            } else {
                posterImageView = (ImageView) convertView;
            }

            String imageFullPath = _config.getImages().getFullBaseUrl() + movie.getPosterPath();
            Log.d("MainActivity", "imageFullPath: " + imageFullPath);

            Picasso.with(_context).load(imageFullPath).into(posterImageView);

            return posterImageView;
        }
    }
}
