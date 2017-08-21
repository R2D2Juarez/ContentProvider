package com.example.w5d3_ex01;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.w5d3_ex01.FeedReaderContract.FeedGenreEntry;
import com.example.w5d3_ex01.FeedReaderContract.FeedMoviesEntry;

import java.util.Map;
import java.util.Set;

public class ProviderTest extends AndroidTestCase{

    private static final String TEST_GENRE_NAME = "Family";
    private static final String TEST_UPDATE_GENRE_NAME = "Scifi";
    private static final String TEST_MOVIE_NAME = "Back to Future I";
    private static final String TEST_UPDATE_MOVIE_NAME = "Back to Future II";
    private static final String TEST_MOVIE_RELEASE_NAME = "1992-02-27";
    private static final String TAG = ProviderTest.class.getSimpleName() + "_TAG";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testDeleteAllRecords();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        testDeleteAllRecords();
    }

    //______________________________________________________________________________________________

    public void testDeleteAllRecords(){

        mContext.getContentResolver().delete(
                FeedGenreEntry.CONTENT_URI,
                null,null
        );

        mContext.getContentResolver().delete(
                FeedMoviesEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                FeedGenreEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals(0, cursor.getCount());

        cursor = mContext.getContentResolver().query(
                FeedMoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals(0, cursor.getCount());
        cursor.close();
    }
    //______________________________________________________________________________________________

    public void testGetType(){

        //_____________________________GENRE________________________________________________________
        String type = mContext.getContentResolver().getType(FeedGenreEntry.CONTENT_URI);
        Log.d(TAG, "testGetType: " + type);
        assertEquals(FeedGenreEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(FeedGenreEntry.buildGenreUri(0));
        Log.d(TAG, "testGetType: " + type);
        assertEquals(FeedGenreEntry.CONTENT_ITEM_TYPE, type);

        //_____________________________MOVIE________________________________________________________
        type = mContext.getContentResolver().getType(FeedMoviesEntry.CONTENT_URI);
        assertEquals(FeedMoviesEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(FeedMoviesEntry.buildMovieUri(0));
        assertEquals(FeedMoviesEntry.CONTENT_ITEM_TYPE, type);
        //__________________________________________________________________________________________

    }
    //______________________________________________________________________________________________

    public void testInsertAndReadGenre(){
        ContentValues values = getGenreContentValues();
        Uri genreInsertUri = mContext.getContentResolver().insert(
                FeedGenreEntry.CONTENT_URI,
                values
        );

        long genreId = ContentUris.parseId(genreInsertUri);

        assertTrue(genreId > 0);

        Cursor cursor = mContext.getContentResolver().query(
                FeedGenreEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor(cursor, values);
        cursor.close();

        cursor = mContext.getContentResolver().query(
                FeedGenreEntry.buildGenreUri(genreId),
                null,
                null,
                null,
                null
        );
        validateCursor(cursor, values);
        cursor.close();
    }
    //______________________________________________________________________________________________

    public void testUpdateGenre(){
        ContentValues values = getGenreContentValues();
        Uri genreUri = mContext.getContentResolver().insert(
                FeedGenreEntry.CONTENT_URI, values
        );

        long genreId = ContentUris.parseId(genreUri);

        ContentValues updateValues = new ContentValues();
        updateValues.put(FeedGenreEntry._ID, genreId);
        updateValues.put(FeedGenreEntry.COLUMN_NAME_NAME, TEST_UPDATE_GENRE_NAME);
    }
    //______________________________________________________________________________________________

    public void testInsertReadMovies(){
        ContentValues values = getGenreContentValues();
        Uri genreIntertUri = mContext.getContentResolver().insert(
                FeedGenreEntry.CONTENT_URI,
                values
        );
        long genreId = ContentUris.parseId(genreIntertUri);

        ContentValues movieValues = getMovieContentValues(genreId);
        Uri movieInsertUri = mContext.getContentResolver().insert(
                FeedMoviesEntry.CONTENT_URI,
                movieValues
        );

        long movieId = ContentUris.parseId(movieInsertUri);
        assertTrue(movieId > 0);

        Cursor movieCursor = mContext.getContentResolver().query(
                FeedMoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor(movieCursor, movieValues);
        movieCursor.close();

        movieCursor = mContext.getContentResolver().query(
                FeedMoviesEntry.buildMovieUri(movieId),
                null,
                null,
                null,
                null
        );

        validateCursor(movieCursor, movieValues);
        movieCursor.close();
    }
    //______________________________________________________________________________________________

    public void testUpdateMovie(){
        ContentValues genreValues = getGenreContentValues();
        Uri genreInsertUri = mContext.getContentResolver().insert(
                FeedGenreEntry.CONTENT_URI,
                genreValues
        );

        long genreId = ContentUris.parseId(genreInsertUri);

        ContentValues movieValues = getMovieContentValues(genreId);
        Uri movieInsertUri = mContext.getContentResolver().insert(
                FeedMoviesEntry.CONTENT_URI,
                movieValues
        );

        long movieId = ContentUris.parseId(movieInsertUri);

        ContentValues updateMovie = new ContentValues(movieValues);
        updateMovie.put(FeedMoviesEntry._ID, movieId);
        updateMovie.put(FeedMoviesEntry.COLUMN_NAME_NAME, TEST_UPDATE_MOVIE_NAME);

        mContext.getContentResolver().update(
                FeedMoviesEntry.CONTENT_URI,
                updateMovie,
                FeedMoviesEntry._ID + "=?",
                new String[]{String.valueOf(movieId)}
        );

        Cursor movieCursor = mContext.getContentResolver().query(
                FeedMoviesEntry.buildMovieUri(movieId),
                null,
                null,
                null,
                null
        );

        validateCursor(movieCursor, updateMovie);
        movieCursor.close();
    }
    //______________________________________________________________________________________________

    private ContentValues getMovieContentValues(long genreId){
        ContentValues values = new ContentValues();
        values.put(FeedMoviesEntry.COLUMN_NAME_NAME, TEST_MOVIE_NAME);
        values.put(FeedMoviesEntry.COLUMN_NAME_DATE, TEST_MOVIE_RELEASE_NAME);
        values.put(FeedMoviesEntry.COLUMN_NAME_GENRE_ID, genreId);
        return values;
    }
    //______________________________________________________________________________________________

    private ContentValues getGenreContentValues(){
        ContentValues values = new ContentValues();
        values.put(FeedGenreEntry.COLUMN_NAME_NAME, TEST_GENRE_NAME);
        return values;
    }
    //______________________________________________________________________________________________

    private void validateCursor(Cursor valueCursor, ContentValues expectedValues){
        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry: valueSet){
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);

            assertFalse(idx == -1);

            switch (valueCursor.getType(idx)){
                case Cursor.FIELD_TYPE_FLOAT:
                    assertEquals(entry.getValue(), valueCursor.getDouble(idx));
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    assertEquals(Integer.parseInt(entry.getValue().toString()), valueCursor.getInt(idx));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    assertEquals(entry.getValue(), valueCursor.getString(idx));
                    break;
                default:
                    assertEquals(entry.getValue(), valueCursor.getString(idx));
                    break;
            }
        }
        valueCursor.close();
    }
    //______________________________________________________________________________________________

}
