package com.isharipov.popularmoviesstagetwo.ui.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.isharipov.popularmoviesstagetwo.R;

/**
 * 02.05.2018.
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder {

    private final TextView reviewAuthor;
    private final TextView reviewContent;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        reviewAuthor = itemView.findViewById(R.id.review_author);
        reviewContent = itemView.findViewById(R.id.review_content);
    }

    public TextView getReviewAuthor() {
        return reviewAuthor;
    }

    public TextView getReviewContent() {
        return reviewContent;
    }
}