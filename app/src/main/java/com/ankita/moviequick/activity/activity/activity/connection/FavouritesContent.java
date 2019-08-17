package com.ankita.moviequick.activity.activity.activity.connection;

import android.provider.BaseColumns;

public class FavouritesContent {

    public static final class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
