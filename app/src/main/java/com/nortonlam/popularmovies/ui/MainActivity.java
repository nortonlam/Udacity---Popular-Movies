package com.nortonlam.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.nortonlam.popularmovies.PopularMoviesApplication;
import com.nortonlam.popularmovies.R;
import com.nortonlam.popularmovies.model.Movie;
import com.nortonlam.popularmovies.model.MovieResults;
import com.nortonlam.popularmovies.net.TheMovieDb;
import com.nortonlam.popularmovies.net.TheMovieDbApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by norton
 *
 * Created date: 10/7/15.
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.sortBySpinner) Spinner _sortBySpinner;
    @Bind(R.id.gridview)      GridView _gridView;

    private ImageAdapter  _imageAdapter;
    private MovieSelected _clickListener = new MovieSelected();
    String[]              _sortByValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        _clickListener = new MovieSelected();
        _gridView.setOnItemClickListener(_clickListener);

        _imageAdapter = new ImageAdapter(this);
        _gridView.setAdapter(_imageAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_by, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _sortBySpinner.setAdapter(adapter);
        _sortBySpinner.setOnItemSelectedListener(new SortBySelected());

        _sortByValues = getResources().getStringArray(R.array.sort_by_values);
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshMovieList(_sortByValues[_sortBySpinner.getSelectedItemPosition()]);
    }

    private void refreshMovieList(String sortBy) {
        TheMovieDbApi theMovieDbApi = TheMovieDb.getApi();
        String apiKey = getResources().getString(R.string.themoviedb_key);

        Call<MovieResults> call = theMovieDbApi.getMovieList(apiKey, sortBy);
        call.enqueue(new MovieResultsCallback());
    }

    private void updateUi(List<Movie> movieList) {
        _imageAdapter.setMovieList(movieList);
        _imageAdapter.notifyDataSetChanged();

        _clickListener.setMovieList(movieList);
    }

    private void nextScreen(Movie movie) {
        Intent detailntent = new Intent(this, DetailActivity.class);
        detailntent.putExtra(Movie.PARAM_KEY, movie);
        startActivity(detailntent);
    }

    class SortBySelected implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            refreshMovieList(_sortByValues[position]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    }

    // Callback for getting movie list
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

    // Adapter to show movie list in gridview
    class ImageAdapter extends BaseAdapter {
        private static final int PADDING = 8;
        private static final int POSTER_WIDTH = 300;
        private static final int POSTER_HEIGHT = 450;

        private Context _context;
        private List<Movie> _movieList;

        public ImageAdapter(Context context) {
            _context = context;
            _movieList = new ArrayList<>();
        }

        public void setMovieList(List<Movie> movieList) {
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
                posterImageView.setLayoutParams(new GridView.LayoutParams(POSTER_WIDTH, POSTER_HEIGHT));
                posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                posterImageView.setPadding(PADDING, PADDING, PADDING, PADDING);

            } else {
                posterImageView = (ImageView) convertView;
            }

            String imageFullPath = ((PopularMoviesApplication)getApplication()).getImageBaseUrl() + movie.getPosterPath();
            Log.d("MainActivity", "imageFullPath: " + imageFullPath);

            Picasso.with(_context).load(imageFullPath)
                    .placeholder(R.drawable.posternotavailable)
                    .into(posterImageView);

            return posterImageView;
        }
    }

    // What to do when a movie is selected
    class MovieSelected implements AdapterView.OnItemClickListener {
        private List<Movie> _movieList;

        public void setMovieList(List<Movie> movieList) {
            _movieList = movieList;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            nextScreen(_movieList.get(position));
        }
    }
}
