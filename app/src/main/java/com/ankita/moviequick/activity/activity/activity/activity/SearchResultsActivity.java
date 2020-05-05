package com.ankita.moviequick.activity.activity.activity.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankita.moviequick.R;
import com.ankita.moviequick.activity.activity.activity.adapters.MovieRecyclerViewAdapter;
import com.ankita.moviequick.activity.activity.activity.connection.NetworkConnection;
import com.ankita.moviequick.activity.activity.activity.model_class.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    private String searchQuery;
    private TextView ErrorMessage;
    private TextView ResultsTitle;
    private ProgressBar pbLoadingIndicator;
    private RecyclerView rvMovieList;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ErrorMessage = findViewById(R.id.tv_error_message_searchResult);
        pbLoadingIndicator = findViewById(R.id.pb_loading_indicator_searchResult);
        ResultsTitle = findViewById(R.id.tv_results_title_searchResult);

        rvMovieList = findViewById(R.id.rv_movie_list_searchresult);
        rvMovieList.setNestedScrollingEnabled(false);

        Intent intent = getIntent();
        searchQuery = intent.getExtras().getString("SearchQuery");

        URL TMDBSearchURL = NetworkConnection.buildSearchUrl(searchQuery);
        new SearchResultsActivity.TMDBQueryTask().execute(TMDBSearchURL);
    }


    public class TMDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String TMDBSearchResults = null;
            try {
                TMDBSearchResults = NetworkConnection.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return TMDBSearchResults;
        }


        @Override
        protected void onPostExecute(String s) {
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
                showRecyclerView();
                try {
                    parseMovies(s);
                    populateRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }
    }


    private void parseMovies(String moviesJSONString) throws JSONException {
        JSONObject resultJSONObject = new JSONObject(moviesJSONString);
        JSONArray moviesJSONArray = resultJSONObject.getJSONArray("results");
        movies = new ArrayList<Movie>();

        for (int i = 0; i < moviesJSONArray.length(); i++) {
            JSONObject movieJSONObject = new JSONObject(moviesJSONArray.get(i).toString());
            if (!movieJSONObject.isNull("poster_path")) {
                int movieId = movieJSONObject.getInt("id");
                String posterPath = movieJSONObject.getString("poster_path");

                movies.add(new Movie(movieId, posterPath));
            }
        }

        ResultsTitle.setText(movies.size() + " " +  getResources().getString(R.string.results_heading) + " '" + searchQuery + "'");
    }


    private void populateRecyclerView() {
        MovieRecyclerViewAdapter rvAdapter = new MovieRecyclerViewAdapter(SearchResultsActivity.this, movies);
        rvMovieList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rvMovieList.setAdapter(rvAdapter);
    }

    private void showRecyclerView() {
        ErrorMessage.setVisibility(View.INVISIBLE);
        rvMovieList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        rvMovieList.setVisibility(View.INVISIBLE);
        ErrorMessage.setVisibility(View.VISIBLE);
    }
}
