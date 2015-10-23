package com.nortonlam.popularmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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

    public static boolean exists(Context context, long movieId) {
        Cursor c = null;
        try {
            c = context.getContentResolver().query(Uri.parse(FavoritesProvider.URL + "/" + movieId), null, null, null, null);

            return (null != c) && (c.moveToFirst());
        }
        finally {
            if (null != c) {
                c.close();
            }
        }
    }
}
