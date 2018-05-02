package com.isharipov.popularmoviesstagetwo.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.isharipov.popularmoviesstagetwo.BuildConfig;
import com.isharipov.popularmoviesstagetwo.R;
import com.isharipov.popularmoviesstagetwo.data.network.Movie;
import com.isharipov.popularmoviesstagetwo.data.network.ReviewResult;
import com.isharipov.popularmoviesstagetwo.data.network.TheMovieDbRestClient;
import com.isharipov.popularmoviesstagetwo.data.network.TrailerResult;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 01.05.2018.
 */
public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_INFO = "extra_info";

    @BindView(R.id.movie_title)
    TextView movieTitle;
    @BindView(R.id.movie_detail_logo)
    ImageView movieDetailLogo;
    @BindView(R.id.release_date)
    TextView releaseDate;
    @BindView(R.id.movie_overview)
    TextView movieOverview;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.favorite_button)
    ImageView favoriteButton;

    public static void start(Activity activity, Movie movie) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_INFO, movie);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_INFO);
        populateUI(movie);
        TheMovieDbRestClient.getInstance().movieTrailersAsync(String.valueOf(movie.getId()), BuildConfig.THEMOVIE_DB_API_KEY, new Callback<TrailerResult>() {
            @Override
            public void onResponse(@NonNull Call<TrailerResult> call, @NonNull Response<TrailerResult> response) {

            }

            @Override
            public void onFailure(@NonNull Call<TrailerResult> call, @NonNull Throwable t) {

            }
        });

        TheMovieDbRestClient.getInstance().movieReviewsAsync(String.valueOf(movie.getId()), BuildConfig.THEMOVIE_DB_API_KEY, new Callback<ReviewResult>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResult> call, @NonNull Response<ReviewResult> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ReviewResult> call, @NonNull Throwable t) {

            }
        });
    }

    private void populateUI(Movie movie) {
        String theMovieDbImgUrl = getString(R.string.themoviedb_img_url);
        Picasso.with(this)
                .load(theMovieDbImgUrl + "w342" + movie.getPosterPath())
                .into(movieDetailLogo);
        movieTitle.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        ratingBar.setRating(movie.getVoteAverage().floatValue());
        movieOverview.setText(movie.getOverview());
    }
}