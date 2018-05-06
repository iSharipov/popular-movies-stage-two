package com.isharipov.popularmoviesstagetwo.ui.main;

import com.isharipov.popularmoviesstagetwo.data.network.MovieResult;

/**
 * 06.05.2018.
 */
interface GetMoviesByIdAsyncTaskListener {
    void onPostExecute(MovieResult movieResult);
}
