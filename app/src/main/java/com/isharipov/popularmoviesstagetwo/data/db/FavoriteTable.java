package com.isharipov.popularmoviesstagetwo.data.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * 09.05.2018.
 */
public class FavoriteTable {
    public static final String TABLE_FAVORITE = "favourites";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MOVIE_ID = "movie_id";


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_FAVORITE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MOVIE_ID + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(database);
    }
}
