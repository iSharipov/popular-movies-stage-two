package com.isharipov.popularmoviesstagetwo.ui.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isharipov.popularmoviesstagetwo.R;

/**
 * 02.05.2018.
 */
class TrailerViewHolder extends RecyclerView.ViewHolder {

    private final ImageView trailerStartBtn;
    private final TextView trailerTitle;

    public TrailerViewHolder(View itemView, TrailerViewClickListener clickListener) {
        super(itemView);
        trailerStartBtn = itemView.findViewById(R.id.trailer_start_btn);
        trailerTitle = itemView.findViewById(R.id.trailer_title);

        itemView.setOnClickListener(v -> clickListener.onClick(itemView, getAdapterPosition()));
    }

    public ImageView getTrailerStartBtn() {
        return trailerStartBtn;
    }

    public TextView getTrailerTitle() {
        return trailerTitle;
    }
}