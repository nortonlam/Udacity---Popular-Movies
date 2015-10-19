package com.nortonlam.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.nortonlam.popularmovies.PopularMoviesApplication;
import com.nortonlam.popularmovies.R;
import com.nortonlam.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by norton
 *
 * Created date: 10/14/15.
 */
public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.titleTextView) TextView _titleTextView;
    @Bind(R.id.posterImageView) ImageView _posterImageView;
    @Bind(R.id.overviewTextView) TextView _overviewTextView;
    @Bind(R.id.ratingTextView) TextView _ratingTextView;
    @Bind(R.id.releaseDateTextView) TextView _releaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

        Movie movie = getIntent().getParcelableExtra(Movie.PARAM_KEY);

        setTitle(movie.getTitle());

        _titleTextView.setText(movie.getTitle());
        _overviewTextView.setText(movie.getOverview());
        _ratingTextView.setText(movie.getVoteAverage());
        _releaseDateTextView.setText(sdf.format(movie.getReleaseDate()));

        String imageFullPath = ((PopularMoviesApplication)getApplication()).getImageBaseUrl() + movie.getPosterPath();
        Log.d("MainActivity", "imageFullPath: " + imageFullPath);

        Picasso.with(this).load(imageFullPath)
                .placeholder(R.drawable.posternotavailable)
                .into(_posterImageView);
    }
}
