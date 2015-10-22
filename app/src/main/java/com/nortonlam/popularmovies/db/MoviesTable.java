package com.nortonlam.popularmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.nortonlam.popularmovies.model.Movie;

/**
 * Created by norton
 * <p/>
 * Created date: 10/21/15.
 */
public class MoviesTable {
    public static final String TABLE_NAME = "movies";

    public final static String ID = "movie_id";
    public final static String TITLE = "title";
    public final static String ORIGINAL_TITILE = "original_title";
    public final static String OVERVIEW = "overview";
    public final static String POPULARITY = "popularity";
    public final static String VIDEO = "video";
    public final static String ADULT = "adult";
    public final static String BACKDROP_PATH = "backdrop_path";
    public final static String ORIGINAL_LANGUAGE = "original_language";
    public final static String RELEASE_DATE = "release_date";
    public final static String POSTER_PATH = "poster_path";
    public final static String VOTE_AVERAGE = "vote_average";
    public final static String VOTE_COUNT = "vote_count";

    private final static String CREATE_SQL =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " long not null," +
                    TITLE + " text not null," +
                    ORIGINAL_TITILE + " text not null," +
                    OVERVIEW + " text not null," +
                    POPULARITY + " text not null," +
                    VIDEO + " char(1) not null," +
                    ADULT + " char(1) not null," +
                    BACKDROP_PATH + " text not null," +
                    ORIGINAL_LANGUAGE + " text not null," +
                    RELEASE_DATE + " text not null," +
                    POSTER_PATH + " text not null," +
                    VOTE_AVERAGE + " text not null," +
                    VOTE_COUNT + " text not null)";

    private final static String DELETE_ALL_SQL =
            "DELETE FROM TABLE " + TABLE_NAME;

    private SQLiteDatabase _db;

    public MoviesTable(SQLiteDatabase db) {
        _db = db;
    }

    public void create() {
        _db.execSQL(CREATE_SQL);
    }

    public static ContentValues getContentValues(Movie movie) {
        ContentValues values = new ContentValues();

        values.put(ID, movie.getId());
        values.put(TITLE, movie.getTitle());
        values.put(ORIGINAL_TITILE, movie.getOriginalTitle());
        values.put(OVERVIEW, movie.getOverview());
        values.put(POPULARITY, movie.getPopularity());
        values.put(VIDEO, movie.isVideo());
        values.put(ADULT, movie.isAdult());
        values.put(BACKDROP_PATH, movie.getBackdropPath());
        values.put(ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        values.put(RELEASE_DATE, movie.getReleaseDateString());
        values.put(POSTER_PATH, movie.getPosterPath());
        values.put(VOTE_AVERAGE, movie.getVoteAverage());
        values.put(VOTE_COUNT, movie.getVoteCount());

        return values;
    }

    public static Movie getMovie(Context context, long movieId) {
        Cursor c = context.getContentResolver().query(Uri.parse(MoviesProvider.URL + "/" + movieId), null, null, null, null);
        if (c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndex(ID));
            String title = c.getString(c.getColumnIndex(TITLE));
            Movie movie = new Movie(id, title); //, originalTitle, overview, popularity, video, adult, backdropPath, originalLanguage, releaseDate, posterPath, voteAverage, voteCount);

            return movie;
        }

        throw new IllegalArgumentException("No movie with id '" + movieId + "'.");
    }
}
