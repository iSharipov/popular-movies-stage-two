package com.isharipov.popularmoviesstagetwo.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_FAVOURITES = "favourites";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MOVIE_ID = "movie_id";

    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 1;
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_FAVOURITES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_MOVIE_ID
            + " integer not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        onCreate(db);
    }

}