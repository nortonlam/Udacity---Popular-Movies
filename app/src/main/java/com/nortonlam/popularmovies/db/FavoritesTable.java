package com.nortonlam.popularmovies.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by norton
 *
 * Created date: 10/20/15.
 */
public class FavoritesTable {
    public static final String TABLE_NAME = "favorites";

    public final static String ID = "movie_id";

    private final static String CREATE_SQL =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " text not null)";

    private final static String DELETE_ALL_SQL =
            "DELETE FROM TABLE " + TABLE_NAME;

    private SQLiteDatabase _db;

    public FavoritesTable(SQLiteDatabase db) {
        _db = db;
    }

    public void create() {
        _db.execSQL(CREATE_SQL);
    }

    public static ContentValues getContentValues(long movieId) {
        ContentValues values = new ContentValues();

        values.put(ID, movieId);

        return values;
    }
}
