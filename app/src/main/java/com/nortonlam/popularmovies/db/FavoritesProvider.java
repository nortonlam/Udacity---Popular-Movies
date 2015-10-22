package com.nortonlam.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by norton
 * <p/>
 * Created date: 10/21/15.
 */
public class FavoritesProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.nortonlam.popularmovies.db.Favorites";
    static final String URL = "content://" + PROVIDER_NAME + "/favorites";
    public static final Uri BASE_PATH = Uri.parse(URL);

    static final int FAVORITES = 1;
    static final int FAVORITES_ID = 2;

    private SQLiteDatabase _db;

    static final UriMatcher _uriMatcher;

    static {
        _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        _uriMatcher.addURI(PROVIDER_NAME, "favorites", FAVORITES);
        _uriMatcher.addURI(PROVIDER_NAME, "favorites/#", FAVORITES_ID);
    }

    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        _db = dbHelper.getWritableDatabase();

        return (null != _db);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        // Set the table
        queryBuilder.setTables(FavoritesTable.TABLE_NAME);

        int uriType = _uriMatcher.match(uri);
        switch (uriType) {
            case FAVORITES:
                break;
            case FAVORITES_ID:
                queryBuilder.appendWhere(FavoritesTable.ID + "=" + uri.getLastPathSegment());
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
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = _uriMatcher.match(uri);

        long id;
        switch (uriType) {
            case FAVORITES:
                id = _db.insert(FavoritesTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = _uriMatcher.match(uri);

        int rowsDeleted;
        switch (uriType) {
            case FAVORITES:
                rowsDeleted = _db.delete(FavoritesTable.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = _db.delete(FavoritesTable.TABLE_NAME, FavoritesTable.ID + "=" + id, null);
                }
                else {
                    rowsDeleted = _db.delete(FavoritesTable.TABLE_NAME, FavoritesTable.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = _uriMatcher.match(uri);

        int rowsUpdated;
        switch (uriType) {
            case FAVORITES:
                rowsUpdated = _db.update(FavoritesTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FAVORITES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = _db.update(FavoritesTable.TABLE_NAME, values, FavoritesTable.ID + "=" + id, null);
                }
                else {
                    rowsUpdated = _db.update(FavoritesTable.TABLE_NAME, values, FavoritesTable.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
