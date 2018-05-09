package com.isharipov.popularmoviesstagetwo.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * 03.05.2018.
 */
public class FavoriteDataSource {
    private final Context context;

    public FavoriteDataSource(Context context) {
        this.context = context;
    }

    public Favorite createFavorite(Long movieId) {
        ContentValues values = new ContentValues();
        values.put(FavoriteTable.COLUMN_MOVIE_ID, movieId);
        context.getContentResolver().insert(FavoriteContentProvider.CONTENT_URI, values);
        return getFavorite(movieId);
    }

    public Favorite getFavorite(Long movieId) {
        Uri favoriteUri = Uri.parse(FavoriteContentProvider.CONTENT_URI + "/" + movieId);
        String[] projection = {FavoriteTable.COLUMN_ID, FavoriteTable.COLUMN_MOVIE_ID};
        Cursor cursor = context.getContentResolver().query(favoriteUri, projection, null, null, null);
        Favorite favorite = null;
        if (cursor.moveToFirst()) {
            favorite = new Favorite();
            favorite.setId(cursor.getLong(0));
            favorite.setMovieId(cursor.getLong(1));
        }
        cursor.close();
        return favorite;
    }

    public void deleteFavorite(Favorite favorite) {
        long id = favorite.getId();
        Uri favoriteUri = Uri.parse(FavoriteContentProvider.CONTENT_URI + "/" + id);
        context.getContentResolver().delete(favoriteUri, null, null);
    }

    public List<Favorite> getAllFavorite() {
        List<Favorite> favorites = new ArrayList<>();

        String[] projection = {FavoriteTable.COLUMN_ID, FavoriteTable.COLUMN_MOVIE_ID};
        Cursor cursor = context.getContentResolver().query(FavoriteContentProvider.CONTENT_URI, projection, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                Favorite favorite = cursorToFavourite(cursor);
                favorites.add(favorite);
            }while(cursor.moveToNext());
        }
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