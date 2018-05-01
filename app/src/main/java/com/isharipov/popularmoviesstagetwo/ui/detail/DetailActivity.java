package com.isharipov.popularmoviesstagetwo.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 01.05.2018.
 */
public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_INFO = "extra_info";

    public static void start(Activity activity, String movieId) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_INFO, movieId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}