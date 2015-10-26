package com.nortonlam.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nortonlam.popularmovies.PopularMoviesApplication;
import com.nortonlam.popularmovies.R;
import com.nortonlam.popularmovies.db.FavoritesProvider;
import com.nortonlam.popularmovies.db.FavoritesTable;
import com.nortonlam.popularmovies.model.Movie;
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
    @Bind(R.id.favoritesButton) Button _favoritesButton;
    @Bind(R.id.overviewTextView) TextView _overviewTextView;
    @Bind(R.id.trailerListView) ListView _trailerListView;
    @Bind(R.id.ratingTextView) TextView _ratingTextView;
    @Bind(R.id.releaseDateTextView) TextView _releaseDateTextView;

    private Movie _movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

        _movie = getIntent().getParcelableExtra(Movie.PARAM_KEY);

        setTitle(_movie.getTitle());

        _titleTextView.setText(_movie.getTitle());
        initFavoritesButton(_movie.getId());
        _overviewTextView.setText(_movie.getOverview());
        initTrailers(_movie.getId());
        _ratingTextView.setText(_movie.getVoteAverage());
        _releaseDateTextView.setText(sdf.format(_movie.getReleaseDate()));

        String imageFullPath = ((PopularMoviesApplication)getApplication()).getImageBaseUrl() + _movie.getPosterPath();
        Picasso.with(this).load(imageFullPath)
                .placeholder(R.drawable.posternotavailable)
                .into(_posterImageView);
    }

    private void updateUi(List<Video> trailerList) {
        Log.d("DetailActivity", "trailer list size: " + trailerList.size());

        _trailerListView.setAdapter(new TrailerAdapter(this, trailerList));
        _trailerListView.setOnItemClickListener(new PlayTrailer(trailerList));
    }

    private void initFavoritesButton(long movieId) {
        if (!FavoritesTable.exists(this, movieId)) {
            _favoritesButton.setText(getResources().getString(R.string.add_to_favorites));
        }
        else {
            _favoritesButton.setText(getResources().getString(R.string.remove_from_favorites));
        }
    }

    private void initTrailers(long movieId) {
        TheMovieDbApi theMovieDbApi = TheMovieDb.getApi();
        String apiKey = getResources().getString(R.string.themoviedb_key);

        Call<VideoResults> call = theMovieDbApi.getMovieTrailers(movieId, apiKey);
        call.enqueue(new TrailerResultsCallback());
    }

    public void addRemoveFavorites(View view) {
        if (!FavoritesTable.exists(this, _movie.getId())) {
            getContentResolver().insert(FavoritesProvider.BASE_PATH, FavoritesTable.getContentValues(_movie.getId()));
        }
        else {
            String[] movieIdArray = new String[1];
            movieIdArray[0] = "" + _movie.getId();
            getContentResolver().delete(FavoritesProvider.BASE_PATH, "movie_id = ?", movieIdArray);
        }

        initFavoritesButton(_movie.getId());
    }

    public void readReviews(View view) {
        Intent reviewsIntent = new Intent(this, ReviewsActivity.class);
        reviewsIntent.putExtra(Movie.PARAM_KEY, _movie);
        startActivity(reviewsIntent);
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

            updateUi(trailerList);
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

    class PlayTrailer implements AdapterView.OnItemClickListener {
        private List<Video> _trailerList;

        PlayTrailer(List<Video> trailerList) {
            _trailerList = trailerList;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Play video
            Intent intent = new  Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + _trailerList.get(position).getKey()));

            startActivity(intent);
        }
    }
}
