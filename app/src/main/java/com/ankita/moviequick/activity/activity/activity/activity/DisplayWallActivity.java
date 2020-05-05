package com.ankita.moviequick.activity.activity.activity.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankita.moviequick.R;
import com.ankita.moviequick.activity.activity.activity.adapters.MovieRecyclerViewAdapter;
import com.ankita.moviequick.activity.activity.activity.connection.NetworkConnection;
import com.ankita.moviequick.activity.activity.activity.model_class.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DisplayWallActivity extends AppCompatActivity {

    private TextView tvErrorMessage;
    private ProgressBar pbLoadingIndicator;
    private RecyclerView rvMovieList;
    private ArrayList<Movie> movies;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_grossing:
                    return true;
                case R.id.action_search:
                    Intent searchActivityIntent = new Intent(DisplayWallActivity.this, SearchActivity.class);
                    startActivity(searchActivityIntent);
                    return true;
                case R.id.action_favourites:
                    Intent favoritesIntent = new Intent(DisplayWallActivity.this, FavouritesActivity.class);
                    startActivity(favoritesIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_wall_layout);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_displayWall);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set bottombar active item
        navigation.setSelectedItemId(R.id.action_grossing);

        rvMovieList = findViewById(R.id.rv_movie_list_displayWall);
        rvMovieList.setNestedScrollingEnabled(false);
        tvErrorMessage = findViewById(R.id.tv_error_message);
        pbLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        // Starts the query
        makeTMDBTrendingQuery();
    }

    /**
     * Makes the query to get current trending movies.
     */
    private void makeTMDBTrendingQuery() {
        URL TMDBTrendingURL = NetworkConnection.buildGrossingUrl();
        new TMDBQueryTask().execute(TMDBTrendingURL);
    }

    /**
     * Shows the recyclerview.
     */
    private void showRecyclerView() {
        tvErrorMessage.setVisibility(View.INVISIBLE);
        rvMovieList.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the error message.
     */
    private void showErrorMessage() {
        rvMovieList.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Parses the movies JSON string and stores them into the movie array.
     *
     * @param moviesJSONString
     */
    private void parseMovies(String moviesJSONString) throws JSONException {
        JSONObject resultJSONObject = new JSONObject(moviesJSONString);
        JSONArray moviesJSONArray = resultJSONObject.getJSONArray("results");
        movies = new ArrayList<>();

        // Loop through the JSON array results
        for (int i = 0; i < moviesJSONArray.length(); i++) {
            JSONObject movieJSONObject = new JSONObject(moviesJSONArray.get(i).toString());
            if (!movieJSONObject.isNull("poster_path")) {
                String posterPath = movieJSONObject.getString("poster_path");
                int movieId = movieJSONObject.getInt("id");

                // Add new movie object to the movie array
                movies.add(new Movie(movieId, posterPath));
            }
        }
    }

    /**
     * Populates the recycler view with the retrieved movies.
     */
    private void populateRecyclerView() {
        MovieRecyclerViewAdapter rvAdapter = new MovieRecyclerViewAdapter(DisplayWallActivity.this, movies);

        // Decide the number of columns based on the screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int spanCount = 2;
        if (width > 1400) {
            spanCount = 5;
        } else if (width > 700) {
            spanCount = 3;
        }

        rvMovieList.setLayoutManager(new GridLayoutManager(DisplayWallActivity.this, spanCount));
        rvMovieList.setAdapter(rvAdapter);
    }

    /**
     * Inner class that takes care of the query task.
     */
    public class  TMDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String TMDBTrendingResults = null;
            try {
                TMDBTrendingResults = NetworkConnection.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return TMDBTrendingResults;
        }

        /**
         * Executes when the API call is finished.
         */
        @Override
        protected void onPostExecute(String s) {
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
            showRecyclerView();
            try {
                parseMovies(s);
                populateRecyclerView();
                MovieRecyclerViewAdapter rvAdapter = new MovieRecyclerViewAdapter(DisplayWallActivity.this, movies);

                // Decide the number of columns based on the screen width
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                int width = displayMetrics.widthPixels;
                int spanCount = 2;
                if (width > 1400) {
                    spanCount = 5;
                } else if (width > 700) {
                    spanCount = 3;
                }

                rvMovieList.setLayoutManager(new GridLayoutManager(DisplayWallActivity.this,spanCount));
                rvMovieList.setAdapter(rvAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
                showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }
    }

}
