package com.example.w5d3_ex01;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by pharr on 16/08/17.
 */

import static com.example.w5d3_ex01.FeedReaderContract.FeedGenreEntry;
import static com.example.w5d3_ex01.FeedReaderContract.FeedMoviesEntry;

import static com.example.w5d3_ex01.FeedReaderContract.*;

public class MovieProvider extends ContentProvider{

    //Arbitrary numbers :)
    private static final int GENRE = 100;
    private static final int GENRE_ID = 101;

    private static final int MOVIE = 200;
    private static final int MOVIE_ID = 201;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private static final String TAG = MovieProvider.class.getSimpleName() + "_TAG";
    private DBHelper dbHelper;

    public static  UriMatcher buildUriMatcher(){
        String content = CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(content, PATH_GENRE, GENRE);
        matcher.addURI(content, PATH_GENRE + "/#", GENRE_ID);
        matcher.addURI(content, PATH_MOVIE, MOVIE);
        matcher.addURI(content, PATH_MOVIE + "/#", MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        Log.d(TAG, "query: Inside the  MovieProvider query");
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor returnCursor;
        long _id;
        switch (uriMatcher.match(uri)){
            case GENRE:
                //TODO Implement GENRE Query
                //throw new UnsupportedOperationException("To bew implemented");
                returnCursor = db.query(
                    FeedGenreEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;
            case GENRE_ID:
                 _id = ContentUris.parseId(uri);
                 returnCursor = db.query(
                    FeedGenreEntry.TABLE_NAME,
                    projection,
                    FeedGenreEntry._ID + "=?",
                    new String[]{String.valueOf(_id)},
                    null,
                    null,sortOrder
                );
                break;
            case MOVIE:
                returnCursor = db.query(
                    FeedMoviesEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;
            case MOVIE_ID:
                _id = ContentUris.parseId(uri);
                returnCursor = db.query(
                    FeedMoviesEntry.TABLE_NAME,
                    projection,
                    FeedMoviesEntry._ID + "=?",
                    new String[]{String.valueOf(_id)},
                    null,
                    null,sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unksnown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case GENRE:
                return FeedGenreEntry.CONTENT_TYPE;
            case GENRE_ID:
                return FeedGenreEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return FeedMoviesEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return FeedMoviesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        long _id;

        Uri returnUri;
        switch (uriMatcher.match(uri)){
            case GENRE:
                _id = db.insert(FeedGenreEntry.TABLE_NAME,null,contentValues);
                if (_id > 0){
                    returnUri = FeedGenreEntry.buildGenreUri(_id);
                }else {
                    throw new UnsupportedOperationException("Unable to insert into: " + uri);
                }
                break;
            case MOVIE:
                _id = db.insert(FeedMoviesEntry.TABLE_NAME,null,contentValues);
                if (_id > 0){
                    returnUri = FeedMoviesEntry.buildMovieUri(_id);
                }else {
                    throw new UnsupportedOperationException("Unable to insert into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;

        switch (uriMatcher.match(uri)){
            case GENRE:
                rows = db.delete(FeedGenreEntry.TABLE_NAME, selection,selectionArgs);
                break;
            case MOVIE:
                rows = db.delete(FeedMoviesEntry.TABLE_NAME, selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (selection == null || rows !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;

        switch (uriMatcher.match(uri)){
            case GENRE:
                rows = db.update(FeedMoviesEntry.TABLE_NAME, contentValues, selection,selectionArgs);
                break;
            case MOVIE:
                rows = db.update(FeedMoviesEntry.TABLE_NAME, contentValues, selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rows !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }
}
