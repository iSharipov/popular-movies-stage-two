package com.isharipov.popularmoviesstagetwo.ui.main;

import android.content.Context;
import android.os.AsyncTask;

import com.isharipov.popularmoviesstagetwo.data.db.Favorite;
import com.isharipov.popularmoviesstagetwo.data.db.FavoriteDataSource;
import com.isharipov.popularmoviesstagetwo.data.network.Movie;
import com.isharipov.popularmoviesstagetwo.data.network.MovieResult;
import com.isharipov.popularmoviesstagetwo.data.network.TheMovieDbRestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 06.05.2018.
 */
public class GetMoviesByIdAsyncTask extends AsyncTask<Void, Void, MovieResult> {

    private final Context context;
    private final GetMoviesByIdAsyncTaskListener listener;
    private final FavoriteDataSource dataSource;

    GetMoviesByIdAsyncTask(Context context, GetMoviesByIdAsyncTaskListener listener) {
        this.context = context;
        this.listener = listener;
        dataSource = new FavoriteDataSource(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MovieResult doInBackground(Void... voids) {
        List<Favorite> allFavorite = dataSource.getAllFavorite();
        MovieResult result = new MovieResult();
        if (allFavorite != null && !allFavorite.isEmpty()) {
            List<Movie> movies = new ArrayList<>();
            for (Favorite favorite : allFavorite) {
                Movie movie = TheMovieDbRestClient.getInstance().moviesSyncById(favorite.getMovieId());
                movies.add(movie);
            }
            result.setResults(movies);
        }
        return result;
    }

    @Override
    protected void onPostExecute(MovieResult movieResult) {
        super.onPostExecute(movieResult);
        listener.onPostExecute(movieResult);
    }

    @Override
    protected void onCancelled(MovieResult movieResult) {
        super.onCancelled(movieResult);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}