package com.nortonlam.popularmovies.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nortonlam.popularmovies.PopularMoviesApplication;
import com.nortonlam.popularmovies.R;
import com.nortonlam.popularmovies.model.Movie;
import com.nortonlam.popularmovies.model.Review;
import com.nortonlam.popularmovies.model.ReviewResults;
import com.nortonlam.popularmovies.model.Video;
import com.nortonlam.popularmovies.model.VideoResults;
import com.nortonlam.popularmovies.net.TheMovieDb;
import com.nortonlam.popularmovies.net.TheMovieDbApi;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by norton
 *
 * Created date: 10/14/15.
 */
public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.titleTextView) TextView _titleTextView;
    @Bind(R.id.posterImageView) ImageView _posterImageView;
    @Bind(R.id.overviewTextView) TextView _overviewTextView;
    @Bind(R.id.trailerListView) ListView _trailerListView;
    @Bind(R.id.ratingTextView) TextView _ratingTextView;
    @Bind(R.id.reviewListView) ListView _reviewListView;
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
        initTrailers(movie.getId());
        _ratingTextView.setText(movie.getVoteAverage());
        initReviews(movie.getId());
        _releaseDateTextView.setText(sdf.format(movie.getReleaseDate()));

        String imageFullPath = ((PopularMoviesApplication)getApplication()).getImageBaseUrl() + movie.getPosterPath();

        Picasso.with(this).load(imageFullPath)
                .placeholder(R.drawable.posternotavailable)
                .into(_posterImageView);
    }

    private void updateUi(List<Video> trailerList, List<Review> reviewList) {
        if (null != trailerList) {
            Log.d("DetailActivity", "trailer list size: " + trailerList.size());

            _trailerListView.setAdapter(new TrailerAdapter(this, trailerList));
            _trailerListView.setOnItemClickListener(new PlayTrailer());
        }
        else if (null != reviewList) {
            Log.d("DetailActivity", "review list size: " + reviewList.size());

            _reviewListView.setAdapter(new ReviewAdapter(this, reviewList));
            _reviewListView.setOnItemClickListener(new PlayTrailer());
        }
    }

    private void initTrailers(long movieId) {
        TheMovieDbApi theMovieDbApi = TheMovieDb.getApi();
        String apiKey = getResources().getString(R.string.themoviedb_key);

        Call<VideoResults> call = theMovieDbApi.getMovieTrailers(movieId, apiKey);
        call.enqueue(new TrailerResultsCallback());
    }

    private void initReviews(long movieId) {
        TheMovieDbApi theMovieDbApi = TheMovieDb.getApi();
        String apiKey = getResources().getString(R.string.themoviedb_key);

        Call<ReviewResults> call = theMovieDbApi.getMovieReviews(movieId, apiKey);
        call.enqueue(new ReviewResultsCallback());
    }

    class TrailerResultsCallback implements Callback<VideoResults> {

        @Override
        public void onResponse(Response<VideoResults> response, Retrofit retrofit) {
            int statusCode = response.code();
            Log.d("TrailerResultsCallback", "statusCode: " + statusCode);

            VideoResults results = response.body();
            List<Video> videoList = results.getResults();

            // Filter out trailers
            List<Video> trailerList = new ArrayList<>();
            for (Video video : videoList) {
                if (video.isTrailer()) {
                    trailerList.add(video);
                }
            }

            updateUi(trailerList, null);
        }

        @Override
        public void onFailure(Throwable t) {

        }
    }

    class ReviewResultsCallback implements Callback<ReviewResults> {

        @Override
        public void onResponse(Response<ReviewResults> response, Retrofit retrofit) {
            int statusCode = response.code();
            Log.d("TrailerResultsCallback", "statusCode: " + statusCode);

            ReviewResults results = response.body();
            List<Review> reviewList = results.getResults();

            updateUi(null, reviewList);
        }

        @Override
        public void onFailure(Throwable t) {

        }
    }

    class TrailerAdapter extends BaseAdapter {
        private Activity _context;
        private List<Video> _trailerList;

        TrailerAdapter(Activity context, List<Video> trailerList) {
            _context = context;
            _trailerList = trailerList;
        }

        @Override
        public int getCount() {
            return _trailerList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View layoutView, ViewGroup parent) {
            Video trailer = _trailerList.get(position);

            ViewHolder holder;
            if (null == layoutView) {
                // if it's not recycled, initialize some attributes
                LayoutInflater inflater = _context.getLayoutInflater();
                layoutView = inflater.inflate(R.layout.listitem_trailer, null);
                holder = new ViewHolder(layoutView);
                layoutView.setTag(holder);
            } else {
                holder = (ViewHolder) layoutView.getTag();
            }

            holder.trailerNameTextView.setText(trailer.getName());

            return layoutView;
        }

        class ViewHolder {
            @Bind(R.id.trailerNameTextView) TextView trailerNameTextView;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    class ReviewAdapter extends BaseAdapter {
        private Activity _context;
        private List<Review> _reviewList;

        ReviewAdapter(Activity context, List<Review> reviewList) {
            _context = context;
            _reviewList = reviewList;
        }

        @Override
        public int getCount() {
            return _reviewList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View layoutView, ViewGroup parent) {
            Review review = _reviewList.get(position);

            ViewHolder holder;
            if (null == layoutView) {
                // if it's not recycled, initialize some attributes
                LayoutInflater inflater = _context.getLayoutInflater();
                layoutView = inflater.inflate(R.layout.listitem_review, null);
                holder = new ViewHolder(layoutView);
                layoutView.setTag(holder);
            } else {
                holder = (ViewHolder) layoutView.getTag();
            }

            holder.titleTextView.setText(review.getContent());
            holder.authorTextView.setText(review.getAuthor());

            return layoutView;
        }

        class ViewHolder {
            @Bind(R.id.titleTextView) TextView titleTextView;
            @Bind(R.id.authorTextView) TextView authorTextView;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    class PlayTrailer implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Play video
        }
    }
}
