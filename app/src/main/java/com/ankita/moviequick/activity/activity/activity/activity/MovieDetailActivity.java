package com.ankita.moviequick.activity.activity.activity.activity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ankita.moviequick.R;
import com.ankita.moviequick.activity.activity.activity.adapters.CastRecyclerViewAdapter;
import com.ankita.moviequick.activity.activity.activity.connection.FavouritesContent;
import com.ankita.moviequick.activity.activity.activity.connection.NetworkConnection;
import com.ankita.moviequick.activity.activity.activity.model_class.Cast;
import com.ankita.moviequick.activity.activity.activity.model_class.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView tvMovieTitle;
    private ImageView ivMoviePoster;
    private TextView tvMovieGenres;
    private TextView tvMovieProductionCompanies;
    private TextView tvMovieRunTime;
    private TextView tvMovieOverview;
    private TextView tvMovieReleaseDate;
    private TextView tvCast;
    private TextView tvErrorMessage;
    private Button btnFavorites;
    private ProgressBar pbLoadingIndicator;
    private LinearLayout llMovieInfoHolder;
    private RatingBar rbRatingBar;
    private RecyclerView rvCast;
    private Button btnTrailer;
    private int movieId;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        tvMovieTitle = findViewById(R.id.tv_movie_title);
        ivMoviePoster = findViewById(R.id.iv_movie_poster);
        tvMovieGenres = findViewById(R.id.tv_movie_genres);
        tvMovieProductionCompanies = findViewById(R.id.tv_movie_production_companies);
        tvMovieRunTime = findViewById(R.id.tv_movie_runtime);
        tvMovieReleaseDate = findViewById(R.id.tv_movie_release_date);
        tvMovieOverview = findViewById(R.id.tv_movie_overview);
        tvErrorMessage = findViewById(R.id.tv_error_message);
        pbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        llMovieInfoHolder = findViewById(R.id.ll_movie_info_holder);
        rbRatingBar = findViewById(R.id.rb_movie_rating);
        btnFavorites = findViewById(R.id.btn_favorites);
        rvCast = findViewById(R.id.rv_cast);
        tvCast = findViewById(R.id.tv_cast);
        btnTrailer = findViewById(R.id.btn_trailer);

        Intent intent = getIntent();
        movieId = intent.getExtras().getInt("MovieId");
        makeTMDBMovieDetailQuery(movieId);

        Drawable progress = rbRatingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.parseColor("#b9090b"));


        String txtBtnFavorites;
        /*if (isInFavorites()) {
            txtBtnFavorites = getResources().getString(R.string.remove_from_favorites);
        } else {
            txtBtnFavorites = getResources().getString(R.string.add_to_favorites);
        }*/
        /*btnFavorites.setText(txtBtnFavorites);

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            int duration = Toast.LENGTH_LONG;

            public void onClick(View v) {
                if (isInFavorites() && removeFromFavorites()) {
                    Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.favorites_removed_1)+" '" + movie.getTitle() + "' " + getResources().getString(R.string.favorites_removed_2), duration);
                    toast.show();
                    btnFavorites.setText(getResources().getString(R.string.add_to_favorites));
                } else if (addToFavorites()) {
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.favorites_added_1)+" '" + movie.getTitle() + "' "+getResources().getString(R.string.favorites_added_2), duration);
                    toast.show();
                    btnFavorites.setText(getResources().getString(R.string.remove_from_favorites));
                }
            }
        });*/
    }


    private void makeTMDBMovieDetailQuery(int movieId) {
        URL TMDBMovieDetailURL = NetworkConnection.buildMovieDetailUrl(movieId);
        new MovieDetailActivity.TMDBQueryTask().execute(TMDBMovieDetailURL);
    }

    private void showMovieDetails() {
        tvErrorMessage.setVisibility(View.GONE);
        llMovieInfoHolder.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage() {
        llMovieInfoHolder.setVisibility(View.GONE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void parseMovieData(String movieJSONString) throws JSONException {
        JSONObject movieJSONObject = new JSONObject(movieJSONString);

        int movieId = movieJSONObject.getInt("id");
        String movieTitle = movieJSONObject.getString("title");

        String moviePosterPath = null;
        if (!movieJSONObject.isNull("poster_path")) {
            moviePosterPath = movieJSONObject.getString("poster_path");
        }

        String movieBackDropPath = null;
        if (!movieJSONObject.isNull("backdrop_path")) {
            movieBackDropPath = movieJSONObject.getString("backdrop_path");
        }

        Integer movieRunTime = null;
        if (!movieJSONObject.isNull("runtime")) {
            movieRunTime = movieJSONObject.getInt("runtime");
        }

        double movieRating = movieJSONObject.getDouble("vote_average");

        String movieOverview = null;
        if (!movieJSONObject.isNull("overview") && movieJSONObject.getString("overview").trim().length() > 0) {
            movieOverview = movieJSONObject.getString("overview");
        }

        String movieReleaseDate = null;
        if (!movieJSONObject.isNull("release_date")) {
            movieReleaseDate = movieJSONObject.getString("release_date");
        }
        ArrayList<String> movieGenres = new ArrayList<String>();
        JSONArray movieGenresJSONArray = movieJSONObject.getJSONArray("genres");
        for (int i = 0; i < movieGenresJSONArray.length(); i++) {
            JSONObject movieGenreJSONObject = movieGenresJSONArray.getJSONObject(i);
            movieGenres.add(movieGenreJSONObject.getString("name"));
        }

        ArrayList<String> movieProductionCompanies = new ArrayList<String>();
        JSONArray movieProductionCompaniesJSONArray = movieJSONObject.getJSONArray("production_companies");
        for (int i = 0; i < movieProductionCompaniesJSONArray.length(); i++) {
            JSONObject movieProductionCompanyJSONObject = movieProductionCompaniesJSONArray.getJSONObject(i);
            movieProductionCompanies.add(movieProductionCompanyJSONObject.getString("name"));
        }

        JSONObject castsJSONObject = movieJSONObject.getJSONObject("casts");

        ArrayList<Cast> cast = new ArrayList<Cast>();
        JSONArray castJSONArray = castsJSONObject.getJSONArray("cast");
        for (int i = 0; i < castJSONArray.length(); i++) {
            JSONObject castJSONObject = castJSONArray.getJSONObject(i);
            cast.add(new Cast(castJSONObject.getString("name"), castJSONObject.getString("character"), castJSONObject.getString("profile_path")));
        }



        String trailerId = null;
        JSONObject videoJSONObject = movieJSONObject.getJSONObject("videos");
        JSONArray videoJSONArray = videoJSONObject.getJSONArray("results");
        for (int i = 0; i < videoJSONArray.length(); i++) {
            if (videoJSONArray.getJSONObject(i).getString("type").equals("Trailer")) {
                trailerId = videoJSONArray.getJSONObject(i).getString("key");
                break;
            }
        }

        movie = new Movie(movieId, movieTitle, moviePosterPath, movieBackDropPath, movieRunTime, movieRating, movieOverview, movieReleaseDate, movieGenres, movieProductionCompanies, cast, trailerId);
    }


    private void bindMovieData() {
        ImageView headerBackdrop = findViewById(R.id.header_backdrop);
        if (movie.getBackDropLarge() != null) {
            Picasso.get().load(movie.getBackDropLarge()).into(headerBackdrop);
        }
        else {
            Picasso.get().load(R.drawable.backdrop_fallback).into(headerBackdrop);
        }

        setTitle(movie.getTitle());

        tvMovieTitle.setText(movie.getTitle());
        if (movie.getRunTime() != null) {
            tvMovieRunTime.setText(String.valueOf(movie.getRunTime()) + " " + getResources().getString(R.string.minutes));
        } else {
            tvMovieRunTime.setText(R.string.unknown);
        }

        if (movie.getOverview() != null) {
            tvMovieOverview.setText(movie.getOverview());
        } else {
            tvMovieOverview.setText(R.string.no_description);
        }

        if (movie.getReleaseDate() != null) {
            tvMovieReleaseDate.setText(movie.getReleaseDate());
        }
        else {
            tvMovieReleaseDate.setText(R.string.unknown);
        }

        Picasso.get().load(movie.getPosterLarge()).into(ivMoviePoster);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        Animation moveUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
        headerBackdrop.startAnimation(fadeInAnimation);

        rbRatingBar.setRating((float) (movie.getRating() / 2));

        ArrayList<String> genres = movie.getGenres();
        tvMovieGenres.setText(getResources().getString(R.string.genres) + ": ");
        if (genres.size() == 0) {
            tvMovieGenres.append(getResources().getString(R.string.unknown));
        } else {
            for (int i = 0; i < genres.size(); i++) {
                if (i != 0) {
                    tvMovieGenres.append(", ");
                }
                tvMovieGenres.append(genres.get(i));
            }
        }

        ArrayList<String> productionCompanies = movie.getProductionCompanies();
        tvMovieProductionCompanies.setText(getResources().getString(R.string.producers) + ": ");
        if (productionCompanies.size() == 0) {
            tvMovieProductionCompanies.append(getResources().getString(R.string.unknown));
        } else {
            for (int i = 0; i < productionCompanies.size(); i++) {
                if (i != 0) {
                    tvMovieProductionCompanies.append(", ");
                }
                tvMovieProductionCompanies.append(productionCompanies.get(i));
            }
        }

        if (movie.getCast().size() > 0) {
            CastRecyclerViewAdapter castAdapter = new CastRecyclerViewAdapter(getApplicationContext(), movie.getCast());
            LinearLayoutManager castLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rvCast.setLayoutManager(castLayoutManager);
            rvCast.setAdapter(castAdapter);
        } else {
            tvCast.setVisibility(View.GONE);
            rvCast.setVisibility(View.GONE);
        }



        final String trailerUrl = movie.getTrailerUrl();
        if (trailerUrl != null) {
            btnTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(trailerUrl));
                    startActivity(i);
                }
            });
        } else {
            btnTrailer.setText(R.string.no_trailer);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnTrailer.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_disabled));
            }
        }

    }

    /*private Boolean isInFavorites() {
        Cursor mCursor = mDatabase.rawQuery(
                "SELECT * FROM " + FavouritesContent.FavoritesEntry.TABLE_NAME +
                        " WHERE " + FavouritesContent.FavoritesEntry.COLUMN_MOVIE_ID + "= " + movieId
                , null);

        return mCursor.moveToFirst();
    }


    private Boolean addToFavorites() {
        ContentValues values = new ContentValues();
        values.put(FavouritesContent.FavoritesEntry.COLUMN_MOVIE_ID, movieId);
        values.put(FavouritesContent.FavoritesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        return mDatabase.insert(FavouritesContent.FavoritesEntry.TABLE_NAME, null, values) > 0;
    }


    private Boolean removeFromFavorites() {
        return mDatabase.delete(FavouritesContent.FavoritesEntry.TABLE_NAME, FavouritesContent.FavoritesEntry.COLUMN_MOVIE_ID + " = " + movieId, null) > 0;
    }*/

    public class TMDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            Log.e("url", searchUrl.toString());
            String TMDBResults = null;
            try {
                TMDBResults = NetworkConnection.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return TMDBResults;
        }



        @Override
        protected void onPostExecute(String s) {
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
                showMovieDetails();
                try {
                    parseMovieData(s);
                    bindMovieData();
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
