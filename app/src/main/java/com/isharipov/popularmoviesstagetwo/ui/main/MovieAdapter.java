package com.isharipov.popularmoviesstagetwo.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isharipov.popularmoviesstagetwo.R;
import com.isharipov.popularmoviesstagetwo.data.network.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 01.05.2018.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private final Context context;
    private final List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, null);
        return new MovieViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        String theMovieDbImgUrl = context.getString(R.string.themoviedb_img_url);
        Picasso.with(context)
                .load(theMovieDbImgUrl + "w342" + movies.get(position).getPosterPath())
                .into(holder.getGridItemImage());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}