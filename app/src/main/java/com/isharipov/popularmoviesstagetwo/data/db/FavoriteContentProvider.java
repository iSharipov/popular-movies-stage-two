package com.isharipov.popularmoviesstagetwo.data.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 08.05.2018.
 */
public class FavoriteContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.isharipov.popularmoviesstagetwo.data.db.favorites";
    private static final String BASE_PATH = "favorites";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int FAVORITES = 10;
    private static final int FAVORITE_ID = 20;

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, FAVORITES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FAVORITE_ID);
    }

    private MySQLiteHelper database;


    @Override
    public boolean onCreate() {
        database = new MySQLiteHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(FavoriteTable.TABLE_FAVORITE);
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case FAVORITES:
                break;
            case FAVORITE_ID:
                queryBuilder.appendWhere(FavoriteTable.COLUMN_MOVIE_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    private void checkColumns(String[] projection) {
        String[] available = {FavoriteTable.COLUMN_MOVIE_ID,
                FavoriteTable.COLUMN_ID};
        if (projection != null) {
            Set<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            Set<String> availableColumns = new HashSet<>(Arrays.asList(available));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case FAVORITES:
                id = sqlDB.insert(FavoriteTable.TABLE_FAVORITE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case FAVORITES:
                rowsDeleted = sqlDB.delete(FavoriteTable.TABLE_FAVORITE, selection,
                        selectionArgs);
                break;
            case FAVORITE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(FavoriteTable.TABLE_FAVORITE,
                            FavoriteTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(FavoriteTable.TABLE_FAVORITE,
                            FavoriteTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
