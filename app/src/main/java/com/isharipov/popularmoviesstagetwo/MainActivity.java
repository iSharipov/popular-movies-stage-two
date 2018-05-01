package com.isharipov.popularmoviesstagetwo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.isharipov.popularmoviesstagetwo.data.network.MovieResult;
import com.isharipov.popularmoviesstagetwo.data.network.TheMovieDbRestClient;

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
    protected ProgressBar progressBar;
    private String sortType;

    private NetworkReceiver networkReceiver = new NetworkReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        networkReceiver = new NetworkReceiver();
        this.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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
            findViewById(R.id.offline_button).setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            TheMovieDbRestClient.getInstance().moviesAsync(sortType, BuildConfig.THEMOVIE_DB_API_KEY, new Callback<MovieResult>() {
                @Override
                public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (response != null) {
                        System.out.println(response);
                    }
                }

                @Override
                public void onFailure(Call<MovieResult> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            addButton();
        }
    }

    public void addButton() {
        Button offlineButton = findViewById(R.id.offline_button);
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
    protected void onStart() {
        super.onStart();
        updateConnectedFlags();
        if (refreshDisplay) {
            makeMovieDbSearchQuery("popular");
        } else {
            addButton();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkReceiver != null) {
            this.unregisterReceiver(networkReceiver);
        }
    }
}