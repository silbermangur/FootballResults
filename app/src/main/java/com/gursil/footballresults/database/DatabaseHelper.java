package com.gursil.footballresults.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gursil.footballresults.Models.GoalsContract;
import com.gursil.footballresults.Models.MatchContract;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "football_results.db";
    private static final int DATABASE_VERSION = 3;

    private static final String SQL_CREATE_MATCH_TABLE =
            "CREATE TABLE " + MatchContract.MatchEntry.TABLE_NAME + " (" +
                    MatchContract.MatchEntry._ID + " INTEGER PRIMARY KEY," +
                    MatchContract.MatchEntry.COLUMN_DATE + " TEXT," +
                    MatchContract.MatchEntry.COLUMN_CITY + " TEXT," +
                    MatchContract.MatchEntry.COLUMN_GROUP_A + " TEXT," +
                    MatchContract.MatchEntry.COLUMN_GROUP_B + " TEXT," +
                    MatchContract.MatchEntry.COLUMN_NUM_GOALS_A + " INTEGER," +
                    MatchContract.MatchEntry.COLUMN_NUM_GOALS_B + " INTEGER," +
                    MatchContract.MatchEntry.COLUMN_OUTCOME + " TEXT)";

    private static final String SQL_CREATE_GOALS_TABLE =
            "CREATE TABLE " + GoalsContract.GoalsEntry.TABLE_NAME + " (" +
                    GoalsContract.GoalsEntry._ID + " INTEGER PRIMARY KEY," +
                    GoalsContract.GoalsEntry.COLUMN_GROUP + " TEXT," +
                    GoalsContract.GoalsEntry.COLUMN_GAMES + " INTEGER," +
                    GoalsContract.GoalsEntry.COLUMN_WINS + " INTEGER," +
                    GoalsContract.GoalsEntry.COLUMN_DRAW + " INTEGER," +
                    GoalsContract.GoalsEntry.COLUMN_LOST + " INTEGER," +
                    GoalsContract.GoalsEntry.COLUMN_GOALS + " INTEGER," +
                    GoalsContract.GoalsEntry.COLUMN_GOALS_CONCEDED + " INTEGER," +
                    GoalsContract.GoalsEntry.COLUMN_POINTS + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MATCH_TABLE);
        db.execSQL(SQL_CREATE_GOALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // Drop the existing goals table
            db.execSQL("DROP TABLE IF EXISTS " + GoalsContract.GoalsEntry.TABLE_NAME);

            // Recreate the goals table with the updated schema
            db.execSQL(SQL_CREATE_GOALS_TABLE);

            // Drop the existing match table
            db.execSQL("DROP TABLE IF EXISTS " + MatchContract.MatchEntry.TABLE_NAME);

            // Recreate the goals table with the updated schema
            db.execSQL(SQL_CREATE_MATCH_TABLE);
        }
    }
}

