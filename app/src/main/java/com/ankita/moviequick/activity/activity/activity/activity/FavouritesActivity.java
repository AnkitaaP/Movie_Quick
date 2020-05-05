package com.ankita.moviequick.activity.activity.activity.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankita.moviequick.R;
import com.ankita.moviequick.activity.activity.activity.adapters.MovieRecyclerViewAdapter;
import com.ankita.moviequick.activity.activity.activity.model_class.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {

    private RecyclerView rvMovieList;
    private ArrayList<Movie> movies;
    private TextView tvNoFavorites;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_grossing:
                    Intent trendingIntent = new Intent(getApplicationContext(), DisplayWallActivity.class);
                    startActivity(trendingIntent);
                    return true;
                case R.id.action_search:
                    Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(searchIntent);
                    return true;
                case R.id.action_favourites:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_favourites);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        rvMovieList = findViewById(R.id.rv_movie_list);
        rvMovieList.setNestedScrollingEnabled(false);

        navigation.setSelectedItemId(R.id.action_favourites);


        /*Cursor cursorFavorites = getFavorites();
        initMovies(cursorFavorites);
        populateRecyclerView();

        tvNoFavorites = findViewById(R.id.tv_no_favorites);
        if (movies.size() > 0) {
            tvNoFavorites.setVisibility(View.GONE);
        }*/
    }


    /*@Override
    protected void onResume() {
        super.onResume();
        Cursor cursorFavorites = getFavorites();
        initMovies(cursorFavorites);
        populateRecyclerView();
    }*/


    private void populateRecyclerView() {
        MovieRecyclerViewAdapter rvAdapter = new MovieRecyclerViewAdapter(getApplicationContext(), movies);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int spanCount = 2;
        if (width > 1400) {
            spanCount = 4;
        } else if (width > 700) {
            spanCount = 3;
        }

        rvMovieList.setLayoutManager(new GridLayoutManager(getApplicationContext(), spanCount));
        rvMovieList.setAdapter(rvAdapter);
    }


    private void initMovies(Cursor cursorMovies) {
        try {
            movies = new ArrayList<>();
            while (cursorMovies.moveToNext()) {
                int movieId = cursorMovies.getInt(cursorMovies.getColumnIndex("movieId"));
                String posterPath = cursorMovies.getString(cursorMovies.getColumnIndex("posterPath"));
                movies.add(new Movie(movieId, posterPath));
            }
        } finally {
            cursorMovies.close();
        }
    }


}