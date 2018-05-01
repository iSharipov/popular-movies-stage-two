package com.isharipov.popularmoviesstagetwo.data.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 08.03.2018.
 */

public interface TheMovieDbService {

    String API_KEY = "api_key";
    String SORT_TYPE = "sort_type";

    @GET("/3/movie/{sort_type}")
    Call<MovieResult> getMoviesBySortType(@Path(SORT_TYPE) String sortType, @Query(API_KEY) String apiKey);
}
