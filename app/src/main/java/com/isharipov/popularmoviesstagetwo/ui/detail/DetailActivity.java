package com.isharipov.popularmoviesstagetwo.ui.detail;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.isharipov.popularmoviesstagetwo.BuildConfig;
import com.isharipov.popularmoviesstagetwo.R;
import com.isharipov.popularmoviesstagetwo.data.network.Movie;
import com.isharipov.popularmoviesstagetwo.data.network.Review;
import com.isharipov.popularmoviesstagetwo.data.network.ReviewResult;
import com.isharipov.popularmoviesstagetwo.data.network.TheMovieDbRestClient;
import com.isharipov.popularmoviesstagetwo.data.network.Trailer;
import com.isharipov.popularmoviesstagetwo.data.network.TrailerResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
    private Movie movie;
    private List<Trailer> trailers;
    private List<Review> reviews;

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
    @BindView(R.id.trailerView)
    RecyclerView trailerView;
    @BindView(R.id.reviewView)
    RecyclerView reviewView;

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
        initTrailerView();
        initReviewView();
        movie = (Movie) getIntent().getSerializableExtra(EXTRA_INFO);
        populateUI(movie);
    }

    private void initTrailerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        trailerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                trailerView.getContext(),
                linearLayoutManager.getOrientation()
        );
        trailerView.addItemDecoration(dividerItemDecoration);
    }

    private void initReviewView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                reviewView.getContext(),
                linearLayoutManager.getOrientation()
        );
        reviewView.addItemDecoration(dividerItemDecoration);
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

    @Override
    protected void onResume() {
        super.onResume();
        TheMovieDbRestClient.getInstance().movieTrailersAsync(String.valueOf(movie.getId()), BuildConfig.THEMOVIE_DB_API_KEY, new Callback<TrailerResult>() {
            @Override
            public void onResponse(@NonNull Call<TrailerResult> call, @NonNull Response<TrailerResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TrailerResult result = response.body();
                    if (result.getResults() != null) {
                        trailers = new ArrayList<>(result.getResults());
                        trailerView.setAdapter(new TrailerAdapter(trailers, new TrailerClickListener()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailerResult> call, @NonNull Throwable t) {

            }
        });
        TheMovieDbRestClient.getInstance().movieReviewsAsync(String.valueOf(movie.getId()), BuildConfig.THEMOVIE_DB_API_KEY, new Callback<ReviewResult>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResult> call, @NonNull Response<ReviewResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReviewResult result = response.body();
                    if (result.getResults() != null) {
                        reviews = new ArrayList<>(result.getResults());
                        reviewView.setAdapter(new ReviewAdapter(reviews));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResult> call, @NonNull Throwable t) {

            }
        });
    }

    class TrailerClickListener implements TrailerViewClickListener {

        @Override
        public void onClick(View view, int position) {
            watchYoutubeVideo(DetailActivity.this, trailers.get(position).getKey());
        }
    }

    private void watchYoutubeVideo(Context context, String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}