package com.ankita.moviequick.activity.activity.activity.connection;



import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

import static android.support.constraint.motion.MotionScene.TAG;


public class NetworkConnection {

    private final static String SEARCH_BASE_URL = "https://api.themoviedb.org/3/search/movie";
    private final static String GROSSING_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private final static String DETAIL_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String PARAM_QUERY = "query";
    private final static String PARAM_API_KEY = "api_key";
    private final static String VALUE_API_KEY = "e285e04625009603b135c164d8f1f648";
    private final static String PARAM_APPEND_TO_RESPONSE = "append_to_response";
    private final static String VALUE_APPEND_TO_RESPONSE = "videos,casts";
    private final static String PARAM_LANGUAGE = "language";

    public static URL buildSearchUrl(String SearchQuery) {
        Uri builtUri = Uri.parse(SEARCH_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, SearchQuery)
                .appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, getLanguage())
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildGrossingUrl() {
        Uri builtUri = Uri.parse(GROSSING_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, getLanguage())
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieDetailUrl(int movieId) {
        String detailUrl = DETAIL_BASE_URL + movieId;
        Uri detailUri = Uri.parse(detailUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY)
                .appendQueryParameter(PARAM_APPEND_TO_RESPONSE, VALUE_APPEND_TO_RESPONSE)
                .appendQueryParameter(PARAM_LANGUAGE, getLanguage())
                .build();
        URL url = null;
        try {
            Log.e("info", detailUri.toString());
            url = new URL(detailUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            InputStream in = urlConnection.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            //read.useDelimiter("\\A");
            String line;
            try {
                while ((line = read.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                Log.e(TAG, "IO Exception : " + e.getMessage());
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "IO Exception : " + e.getMessage());
                }
            }

            return sb.toString();

    }
    private static String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        if (language.equals("nl")) {
            return "nl";
        }
        else if (language.equals("de")) {
            return "de";
        }
        return "en";
    }
}