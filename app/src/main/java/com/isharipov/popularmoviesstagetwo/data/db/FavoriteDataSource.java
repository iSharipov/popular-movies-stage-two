package com.isharipov.popularmoviesstagetwo.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 03.05.2018.
 */
public class FavoriteDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_MOVIE_ID};

    public FavoriteDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Favorite createFavorite(Long movieId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_MOVIE_ID, movieId);
        long insertId = database.insert(MySQLiteHelper.TABLE_FAVOURITES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FAVOURITES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Favorite favorite = cursorToFavourite(cursor);
        cursor.close();
        return favorite;
    }

    public void deleteFavourite(Favorite favorite) {
        long id = favorite.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_FAVOURITES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public Favorite getFavorite(Long movieId) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FAVOURITES,
                allColumns, MySQLiteHelper.COLUMN_MOVIE_ID + " = " + movieId, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            Favorite favorite = cursorToFavourite(cursor);
            cursor.close();
            return favorite;
        }
        return null;
    }

    public List<Favorite> getAllFavorite() {
        List<Favorite> favorites = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_FAVOURITES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Favorite favorite = cursorToFavourite(cursor);
            favorites.add(favorite);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return favorites;
    }

    private Favorite cursorToFavourite(Cursor cursor) {
        Favorite favorite = new Favorite();
        favorite.setId(cursor.getLong(0));
        favorite.setMovieId(cursor.getLong(1));
        return favorite;
    }
}