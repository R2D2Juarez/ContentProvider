package com.example.w5d3_ex01;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public final class FeedReaderContract {

    public  static final String CONTENT_AUTHORITY = "com.example.w5d3_ex01";
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_GENRE = "genre";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

//__________________________________________________________________________________________________

    public static class FeedGenreEntry implements BaseColumns{

        //content://com.example.w5d3_ex01/genre
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_GENRE;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_GENRE;

        public  static Uri buildGenreUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = "genre";
        public static final String COLUMN_NAME_NAME = "name";
    }
//__________________________________________________________________________________________________

    public static class FeedMoviesEntry implements BaseColumns{

        //content://com.example.w5d3_ex01/movie
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;

        public  static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_GENRE_ID = "genre_id";
    }
}
