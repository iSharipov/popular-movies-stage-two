package com.isharipov.popularmoviesstagetwo.data.network;

import com.google.gson.Gson;
import com.isharipov.popularmoviesstagetwo.BuildConfig;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 08.03.2018.
 */

public class TheMovieDbRestClient {
    private static final String BASE_URL = "http://api.themoviedb.org/";
    private static final String API_KEY = BuildConfig.THEMOVIE_DB_API_KEY;

    private static volatile TheMovieDbRestClient instance;

    private TheMovieDbService apiService;

    private TheMovieDbRestClient() {
        Gson gson = new Gson();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(chain -> {
            Request request = chain
                    .request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            return chain.proceed(request);
        }).addInterceptor(loggingInterceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(TheMovieDbService.class);
    }

    /**
     * Get default instance of {@code DaDataRestClient}.
     *
     * @return an instance of DaDataRestClient.
     */
    public static TheMovieDbRestClient getInstance() {
        TheMovieDbRestClient localInstance = instance;
        if (localInstance == null) {
            synchronized (TheMovieDbRestClient.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TheMovieDbRestClient();
                }
            }
        }
        return localInstance;
    }

    /**
     * Get movies asynchronously.
     *
     * @param callback a Retrofit callback.
     */
    public void moviesAsync(String sortType, Callback<MovieResult> callback) {
        Call<MovieResult> moviesSync = apiService.getMoviesBySortType(sortType, API_KEY);
        moviesSync.enqueue(callback);
    }

    /**
     * Get movies synchronously.
     *
     * @return
     */
    public MovieResult moviesSync(String sortType) throws IOException {
        Call<MovieResult> moviesSync = apiService.getMoviesBySortType(sortType, API_KEY);
        return moviesSync.execute().body();
    }

    /**
     * Get movies synchronously.
     *
     * @return
     */
    public Movie moviesSyncById(Long id) {
        try{
            Call<Movie> moviesSync = apiService.getMovieById(id, API_KEY);
            return moviesSync.execute().body();
        }catch (IOException e){

        }
        return null;
    }

    /**
     * Get movie trailers asynchronously.
     *
     * @param callback a Retrofit callback.
     */
    public void movieTrailersAsync(String id, Callback<TrailerResult> callback) {
        Call<TrailerResult> movieTrailersSync = apiService.getMovieTrailers(id, API_KEY);
        movieTrailersSync.enqueue(callback);
    }

    /**
     * Get movie reviews asynchronously.
     *
     * @param callback a Retrofit callback.
     */
    public void movieReviewsAsync(String id, Callback<ReviewResult> callback) {
        Call<ReviewResult> movieReviewsSync = apiService.getMovieReviews(id, API_KEY);
        movieReviewsSync.enqueue(callback);
    }

    /**
     * Get movie trailers
     *
     * @return
     */
    public TrailerResult movieTrailersSync(String id) throws IOException {
        Call<TrailerResult> moviesSync = apiService.getMovieTrailers(id, API_KEY);
        return moviesSync.execute().body();
    }

    /**
     * Get movie reviews synchronously.
     *
     * @return
     */
    public ReviewResult movieReviewsSync(String id) throws IOException {
        Call<ReviewResult> moviesSync = apiService.getMovieReviews(id, API_KEY);
        return moviesSync.execute().body();
    }
}