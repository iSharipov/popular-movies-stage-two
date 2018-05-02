package com.isharipov.popularmoviesstagetwo.ui.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isharipov.popularmoviesstagetwo.R;
import com.isharipov.popularmoviesstagetwo.data.network.Review;

import java.util.List;

/**
 * 02.05.2018.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private final List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.getReviewAuthor().setText(reviews.get(position).getAuthor());
        holder.getReviewContent().setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}