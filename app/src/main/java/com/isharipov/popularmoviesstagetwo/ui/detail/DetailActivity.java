package com.isharipov.popularmoviesstagetwo.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.isharipov.popularmoviesstagetwo.BuildConfig;
import com.isharipov.popularmoviesstagetwo.data.network.ReviewResult;
import com.isharipov.popularmoviesstagetwo.data.network.TheMovieDbRestClient;
import com.isharipov.popularmoviesstagetwo.data.network.TrailerResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        String id = (String) getIntent().getSerializableExtra(EXTRA_INFO);
        TheMovieDbRestClient.getInstance().movieTrailersAsync(id, BuildConfig.THEMOVIE_DB_API_KEY, new Callback<TrailerResult>() {
            @Override
            public void onResponse(@NonNull Call<TrailerResult> call, @NonNull Response<TrailerResult> response) {

            }

            @Override
            public void onFailure(@NonNull Call<TrailerResult> call, @NonNull Throwable t) {

            }
        });

        TheMovieDbRestClient.getInstance().movieReviewsAsync(id, BuildConfig.THEMOVIE_DB_API_KEY, new Callback<ReviewResult>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResult> call, @NonNull Response<ReviewResult> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ReviewResult> call, @NonNull Throwable t) {

            }
        });
    }
}