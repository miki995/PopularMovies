package miki.inc.com.popularmovies.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by MIKI on 10.3.2018.
 */

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "miki.inc.com.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class MoviesEntry implements BaseColumns {

        public static final String TABLE_MOVIES = "TABLE_MOVIES";

        public static final String ID = "id";
        public static final String MOVIE_ID = "movies_id";
        public static final String MOVIE_ADULT = "movie_adult";
        public static final String MOVIE_POSTER_PATH = "movie_poster_path";
        public static final String MOVIE_OVERVIEW = "movie_overview";
        public static final String MOVIE_GENRES = "movie_genres";
        public static final String MOVIE_RELEASE_DATE = "movie_release_date";
        public static final String MOVIE_TITLE = "movie_title";
        public static final String MOVIE_ORIG_TITLE = "movie_original_title";
        public static final String MOVIE_LANGUAGE = "movie_language";
        public static final String MOVIE_BACKDROP_PATH = "movie_backdrop_path";
        public static final String MOVIE_POPULARITY = "movie_popularity";
        public static final String MOVIE_VIDEO = "movie_video";
        public static final String MOVIE_VOTE_AVERAGE = "movie_vote_average";
        public static final String MOVIE_VOTE_COUNT = "movie_vote_count";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_MOVIES).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}

