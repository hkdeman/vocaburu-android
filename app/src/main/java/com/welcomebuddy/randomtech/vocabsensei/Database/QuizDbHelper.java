package com.welcomebuddy.randomtech.vocabsensei.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuizDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + QuizContract.QuizEntry.TABLE_NAME + " (" +
                    QuizContract.QuizEntry._ID + " INTEGER PRIMARY KEY," +
                    QuizContract.QuizEntry.COLUMN_KEY + " TEXT," +
                    QuizContract.QuizEntry.COLUMN_DETAIL + " TEXT," +
                    QuizContract.QuizEntry.COLUMN_SCORE+ " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + QuizContract.QuizEntry.TABLE_NAME;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
