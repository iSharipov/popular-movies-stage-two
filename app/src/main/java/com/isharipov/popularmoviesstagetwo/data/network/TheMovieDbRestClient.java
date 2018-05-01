package com.isharipov.popularmoviesstagetwo.data.network;

import com.google.gson.Gson;

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
     * Get suggestion asynchronously.
     *
     * @param callback a Retrofit callback.
     */
    public void moviesAsync(String sortType, String apiKey, Callback<MovieResult> callback) {
        Call<MovieResult> moviesSync = apiService.getMoviesBySortType(sortType, apiKey);
        moviesSync.enqueue(callback);
    }

    /**
     * Get suggestion synchronously.
     *
     * @return
     */
    public MovieResult moviesSync(String sortType, String apiKey) throws IOException {
        Call<MovieResult> moviesSync = apiService.getMoviesBySortType(sortType, apiKey);
        return moviesSync.execute().body();
    }
}
