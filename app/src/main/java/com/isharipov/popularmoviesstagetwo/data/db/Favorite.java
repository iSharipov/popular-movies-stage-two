package com.isharipov.popularmoviesstagetwo.data.db;

/**
 * 03.05.2018.
 */
public class Favorite {
    private long id;
    private long movieId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }
}
