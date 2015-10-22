package com.nortonlam.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.nortonlam.popularmovies.model.Movie;

/**
 * Created by norton
 * <p/>
 * Created date: 10/21/15.
 */
public class MoviesProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.nortonlam.popularmovies.db.Movies";
    public static final String URL = "content://" + PROVIDER_NAME + "/movies";
    public static final Uri BASE_PATH = Uri.parse(URL);

    static final int MOVIES = 1;
    static final int MOVIES_ID = 2;

    private SQLiteDatabase _db;

    static final UriMatcher _uriMatcher;

    static {
        _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        _uriMatcher.addURI(PROVIDER_NAME, "movies", MOVIES);
        _uriMatcher.addURI(PROVIDER_NAME, "movies/#", MOVIES_ID);
    }

    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        _db = dbHelper.getWritableDatabase();

        return (null != _db);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        // Set the table
        queryBuilder.setTables(MoviesTable.TABLE_NAME);

        int uriType = _uriMatcher.match(uri);
        switch (uriType) {
            case MOVIES:
                break;
            case MOVIES_ID:
                queryBuilder.appendWhere(MoviesTable.ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(_db, projection, selection, selectionArgs, null, null, sortOrder);

        // notify listeners
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = _uriMatcher.match(uri);

        long id;
        switch (uriType) {
            case MOVIES:
                id = _db.insert(MoviesTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType = _uriMatcher.match(uri);

        int rowsDeleted;
        switch (uriType) {
            case MOVIES:
                rowsDeleted = _db.delete(MoviesTable.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = _db.delete(MoviesTable.TABLE_NAME, MoviesTable.ID + "=" + id, null);
                }
                else {
                    rowsDeleted = _db.delete(MoviesTable.TABLE_NAME, MoviesTable.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = _uriMatcher.match(uri);

        int rowsUpdated;
        switch (uriType) {
            case MOVIES:
                rowsUpdated = _db.update(MoviesTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = _db.update(MoviesTable.TABLE_NAME, values, MoviesTable.ID + "=" + id, null);
                }
                else {
                    rowsUpdated = _db.update(MoviesTable.TABLE_NAME, values, MoviesTable.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
