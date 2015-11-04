package com.nortonlam.popularmovies.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nortonlam.popularmovies.R;
import com.nortonlam.popularmovies.db.FavoritesProvider;
import com.nortonlam.popularmovies.db.FavoritesTable;
import com.nortonlam.popularmovies.model.Movie;

/**
 * Created by norton
 *
 * Created date: 10/14/15.
 */
public class DetailActivity extends AppCompatActivity {
    private Movie _movie;
    private ShareActionProvider _shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        _movie = getIntent().getParcelableExtra(Movie.PARAM_KEY);

        setTitle(_movie.getTitle());

        Fragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Movie.PARAM_KEY, _movie);
        detailFragment.setArguments(args);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.detailFrame, detailFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        _shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText("https://www.youtube.com/watch?v=2p7bgMxewxA");
        Intent shareIntent =  builder.getIntent();

        Intent.createChooser(shareIntent, "title");
        //Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //shareIntent.setType("text/plain");
        //shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=2p7bgMxewxA");
        setShareIntent(shareIntent);

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (null != _shareActionProvider) {
            _shareActionProvider.setShareIntent(shareIntent);
        }
    }
}
