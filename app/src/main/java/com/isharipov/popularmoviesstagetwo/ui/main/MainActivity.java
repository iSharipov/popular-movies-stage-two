package com.isharipov.popularmoviesstagetwo.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.isharipov.popularmoviesstagetwo.R;
import com.isharipov.popularmoviesstagetwo.data.network.Movie;
import com.isharipov.popularmoviesstagetwo.data.network.MovieResult;
import com.isharipov.popularmoviesstagetwo.data.network.TheMovieDbRestClient;
import com.isharipov.popularmoviesstagetwo.ui.detail.DetailActivity;
import com.isharipov.popularmoviesstagetwo.ui.pref.SettingsPrefActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    public static boolean refreshDisplay = true;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.offline_button)
    Button offlineButton;
    @BindView(R.id.gridView)
    RecyclerView gridView;
    private String sortType;
    private MovieResult movieResult;
    private Parcelable gridViewState;
    private Bundle bundleRecyclerViewState;
    private GridLayoutManager gridLayoutManager;
    private NetworkReceiver networkReceiver;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPreferences();
        gridLayoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                bundleRecyclerViewState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            bundleRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        }
    }

    public void updateConnectedFlags() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    private void makeMovieDbSearchQuery(String sortType) {
        if (wifiConnected || mobileConnected) {
            offlineButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            if (sortType.equals("favorite")) {
                new GetMoviesByIdAsyncTask(this, new GetMoviesByIdAsyncTaskListener() {
                    @Override
                    public void onPostExecute(MovieResult movieResult) {
                        progressBar.setVisibility(View.INVISIBLE);
                        gridView.setAdapter(new MovieAdapter(MainActivity.this, movieResult.getResults(), new MovieClickListener(movieResult.getResults().get(0))));
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                TheMovieDbRestClient.getInstance().moviesAsync(sortType, new Callback<MovieResult>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResult> call, @NonNull Response<MovieResult> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.isSuccessful()) {
                            MovieResult result = response.body();
                            if (result != null && result.getResults() != null) {
                                gridView.setAdapter(new MovieAdapter(MainActivity.this, result.getResults(), new MovieClickListener(null)));
                                movieResult = result;
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResult> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        addButton();
                    }
                });
            }
        } else {
            addButton();
        }
    }

    class MovieClickListener implements MovieViewClickListener {
        private final Movie movie;

        MovieClickListener(Movie movie) {
            this.movie = movie;
        }

        @Override
        public void onClick(View view, int position) {
            DetailActivity.start(MainActivity.this, movie != null ? movie : movieResult.getResults().get(position));

        }
    }

    public void addButton() {
        offlineButton.setVisibility(View.VISIBLE);
        offlineButton.setOnClickListener(v -> {
            updateConnectedFlags();
            makeMovieDbSearchQuery(sortType);
        });
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                refreshDisplay = true;
            } else {
                refreshDisplay = false;
                Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateConnectedFlags();
        networkReceiver = new NetworkReceiver();
        this.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        if (refreshDisplay) {
            makeMovieDbSearchQuery(sortType);
        } else {
            addButton();
        }
        if (bundleRecyclerViewState != null) {
            gridViewState = bundleRecyclerViewState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            gridView.getLayoutManager().onRestoreInstanceState(gridViewState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkReceiver != null) {
            this.unregisterReceiver(networkReceiver);
        }
        bundleRecyclerViewState = new Bundle();
        gridViewState = gridView.getLayoutManager().onSaveInstanceState();
        bundleRecyclerViewState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                gridViewState);
        gridView.setAdapter(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsPrefActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sortType = sharedPref.getString(SettingsPrefActivity.KEY_PREF_MOVIE_SORT_TYPE, getString(R.string.pref_sort_type_default));
    }
}