package com.isharipov.popularmoviesstagetwo.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.isharipov.popularmoviesstagetwo.R;

/**
 * 01.05.2018.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView gridItemImage;

    MovieViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        gridItemImage = itemView.findViewById(R.id.grid_item_image);
    }

    @Override
    public void onClick(View v) {

    }

    public ImageView getGridItemImage() {
        return gridItemImage;
    }
}