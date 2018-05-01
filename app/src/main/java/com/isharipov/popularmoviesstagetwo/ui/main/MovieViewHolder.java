package com.isharipov.popularmoviesstagetwo.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.isharipov.popularmoviesstagetwo.R;

/**
 * 01.05.2018.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    private final ImageView gridItemImage;

    MovieViewHolder(View itemView, MovieViewClickListener clickListener) {
        super(itemView);
        gridItemImage = itemView.findViewById(R.id.grid_item_image);

        itemView.setOnClickListener(v -> clickListener.onClick(itemView, getAdapterPosition()));
    }

    public ImageView getGridItemImage() {
        return gridItemImage;
    }
}