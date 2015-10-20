package com.nortonlam.popularmovies.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nortonlam.popularmovies.R;
import com.nortonlam.popularmovies.model.Movie;
import com.nortonlam.popularmovies.model.Review;
import com.nortonlam.popularmovies.model.ReviewResults;
import com.nortonlam.popularmovies.net.TheMovieDb;
import com.nortonlam.popularmovies.net.TheMovieDbApi;

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
 * Created date: 10/20/15.
 */
public class ReviewsActivity extends AppCompatActivity {
    @Bind(R.id.titleTextView) TextView _titleTextView;
    @Bind(R.id.reviewListView) ListView _reviewListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        ButterKnife.bind(this);

        Movie movie = getIntent().getParcelableExtra(Movie.PARAM_KEY);

        String title = movie.getTitle() + " " + getResources().getString(R.string.reviews);
        setTitle(title);
        _titleTextView.setText(title);

        initReviews(movie.getId());
    }

    private void updateUi(List<Review> reviewList) {
        Log.d("DetailActivity", "review list size: " + reviewList.size());

        _reviewListView.setAdapter(new ReviewAdapter(this, reviewList));
    }

    private void initReviews(long movieId) {
        TheMovieDbApi theMovieDbApi = TheMovieDb.getApi();
        String apiKey = getResources().getString(R.string.themoviedb_key);

        Call<ReviewResults> call = theMovieDbApi.getMovieReviews(movieId, apiKey);
        call.enqueue(new ReviewResultsCallback());
    }

    class ReviewResultsCallback implements Callback<ReviewResults> {

        @Override
        public void onResponse(Response<ReviewResults> response, Retrofit retrofit) {
            int statusCode = response.code();
            Log.d("TrailerResultsCallback", "statusCode: " + statusCode);

            ReviewResults results = response.body();
            List<Review> reviewList = results.getResults();

            updateUi(reviewList);
        }

        @Override
        public void onFailure(Throwable t) {

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
}
