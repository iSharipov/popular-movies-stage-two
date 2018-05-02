package com.isharipov.popularmoviesstagetwo.ui.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isharipov.popularmoviesstagetwo.R;
import com.isharipov.popularmoviesstagetwo.data.network.Trailer;

import java.util.List;

/**
 * 02.05.2018.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    private final List<Trailer> trailers;
    private final TrailerViewClickListener clickListener;

    public TrailerAdapter(List<Trailer> trailers, TrailerViewClickListener clickListener) {
        this.trailers = trailers;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(layoutView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.getTrailerTitle().setText(trailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}
