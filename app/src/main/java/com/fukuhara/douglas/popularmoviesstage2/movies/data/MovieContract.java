package com.fukuhara.douglas.popularmoviesstage2.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dofukuhara on 28/11/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.fukuhara.douglas.popularmoviesstage2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String MOVIES_PATH = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIES_PATH).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_BACK_PATH = "backPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";


    }
}
