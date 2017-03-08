package com.example.jonathanfils_aime.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jonathanfils-aime on 3/3/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + "( " +
            FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
            FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME + " VARCHAR(50)," +
            FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME + " VARCHAR(50)," +
            FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER + " INTEGER," +
            FeedReaderContract.FeedEntry.COLUMN_NAME_SAVINGS_ACCOUNT + " INTEGER," +
            FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL + " INTEGER," +
            FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT + " INTEGER," +
            FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK + " INTEGER," +
            FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK + " INTEGER," +
            FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXIST " + FeedReaderContract.FeedEntry.TABLE_NAME;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "feedReader.db";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
