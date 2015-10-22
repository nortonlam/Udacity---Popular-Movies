package com.nortonlam.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by norton
 * <p/>
 * Created date: 10/21/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PopularMovies";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FavoritesTable favoritesTable = new FavoritesTable(db);
        favoritesTable.create();

        MoviesTable moviesTable = new MoviesTable(db);
        moviesTable.create();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }
}

