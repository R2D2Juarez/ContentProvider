package com.example.w5d3_ex01;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.w5d3_ex01.FeedReaderContract.FeedGenreEntry;
import com.example.w5d3_ex01.FeedReaderContract.FeedMoviesEntry;


public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "films.db";
    private static final String TAG = DBHelper.class.getSimpleName() + "_TAG";

//__________________________________________________________________________________________________

    public static final String SQL_CREATE_GENRE_ENTRIES = " CREATE TABLE " +
            FeedGenreEntry.TABLE_NAME + "(" +
            FeedGenreEntry._ID + " INTEGER PRIMARY KEY, " +
            FeedGenreEntry.COLUMN_NAME_NAME + " TEXT )";

    public static final String SQL_CREATE_MOVIES_ENTRIES = " CREATE TABLE " +
            FeedMoviesEntry.TABLE_NAME + "(" +
            FeedMoviesEntry._ID + " INTEGER PRIMARY KEY, " +
            FeedMoviesEntry.COLUMN_NAME_NAME + " TEXT, " +
            FeedMoviesEntry.COLUMN_NAME_DATE + " TEXT, " +
            FeedMoviesEntry.COLUMN_NAME_GENRE_ID +" INTEGER , " +
            "FOREIGN KEY(" + FeedMoviesEntry.COLUMN_NAME_GENRE_ID +") REFERENCES "+
                             FeedGenreEntry.TABLE_NAME +" (" + FeedGenreEntry._ID + "))";
//__________________________________________________________________________________________________

    public static final  String SQL_DELETE_GENRE_ENTRIES = "DROP TABLE IF EXISTS " + FeedGenreEntry.TABLE_NAME;
    public static final  String SQL_DELETE_MOVIES_ENTRIES = "DROP TABLE IF EXISTS " + FeedMoviesEntry.TABLE_NAME;

//__________________________________________________________________________________________________

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//__________________________________________________________________________________________________

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_GENRE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_ENTRIES);


        Cursor cursor;
        cursor = sqLiteDatabase.rawQuery("select * from " + FeedGenreEntry.TABLE_NAME , null);
        if (cursor != null) Log.d(TAG, "onCreate: TABLE GENRE CREATED");

        cursor = sqLiteDatabase.rawQuery("select * from " + FeedMoviesEntry.TABLE_NAME , null);
        if (cursor != null) Log.d(TAG, "onCreate: TABLE MOVIES CREATED");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_GENRE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_MOVIES_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
