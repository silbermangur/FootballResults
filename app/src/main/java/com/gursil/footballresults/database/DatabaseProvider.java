package com.gursil.footballresults.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.gursil.footballresults.Models.GoalsContract;
import com.gursil.footballresults.Models.MatchContract;
import com.gursil.footballresults.Models.TeamStats;

import java.util.ArrayList;
import java.util.List;

public class DatabaseProvider {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DatabaseProvider(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertMatchResult(String date, String city, String groupA, String groupB, int goalsA, int goalsB, String outcome) {
        ContentValues values = new ContentValues();
        values.put(MatchContract.MatchEntry.COLUMN_DATE, date);
        values.put(MatchContract.MatchEntry.COLUMN_CITY, city);
        values.put(MatchContract.MatchEntry.COLUMN_GROUP_A, groupA);
        values.put(MatchContract.MatchEntry.COLUMN_GROUP_B, groupB);
        values.put(MatchContract.MatchEntry.COLUMN_NUM_GOALS_A, goalsA);
        values.put(MatchContract.MatchEntry.COLUMN_NUM_GOALS_B, goalsB);
        values.put(MatchContract.MatchEntry.COLUMN_OUTCOME, outcome);

        long newRowId = database.insert(MatchContract.MatchEntry.TABLE_NAME, null, values);

        updateGroupGoals(groupA, goalsA, goalsB, outcome);

        updateGroupGoals(groupB, goalsB, goalsA, invertOutcome(outcome));

        return newRowId;
    }

    @SuppressLint("Range")
    private void updateGroupGoals(String group, int goalsScored, int goalsConceded, String outcome) {

        // Retrieve the current stats for the group
        String selection = GoalsContract.GoalsEntry.COLUMN_GROUP + " = ?";
        String[] selectionArgs = {group};

        Cursor cursor = database.query(
                GoalsContract.GoalsEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ContentValues values = new ContentValues();

        if (cursor != null && cursor.moveToFirst()) {
            // Group exists, update the stats
            int games = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GAMES));
            int wins = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_WINS));
            int draws = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_DRAW));
            int losses = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_LOST));
            int goals = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GOALS));
            int goalsConcededTotal = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GOALS_CONCEDED));
            int points = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_POINTS));

            games++;
            goals += goalsScored;
            goalsConcededTotal += goalsConceded;

            if (outcome.equals("Win")) {
                wins++;
                points += 3;
            } else if (outcome.equals("Draw")) {
                draws++;
                points++;
            } else if (outcome.equals("Loss")) {
                losses++;
            }

            values.put(GoalsContract.GoalsEntry.COLUMN_GAMES, games);
            values.put(GoalsContract.GoalsEntry.COLUMN_WINS, wins);
            values.put(GoalsContract.GoalsEntry.COLUMN_DRAW, draws);
            values.put(GoalsContract.GoalsEntry.COLUMN_LOST, losses);
            values.put(GoalsContract.GoalsEntry.COLUMN_GOALS, goals);
            values.put(GoalsContract.GoalsEntry.COLUMN_GOALS_CONCEDED, goalsConcededTotal);
            values.put(GoalsContract.GoalsEntry.COLUMN_POINTS, points);

            database.update(GoalsContract.GoalsEntry.TABLE_NAME, values, selection, selectionArgs);
        } else {
            // Group doesn't exist, insert new record
            values.put(GoalsContract.GoalsEntry.COLUMN_GROUP, group);
            values.put(GoalsContract.GoalsEntry.COLUMN_GAMES, 1);
            values.put(GoalsContract.GoalsEntry.COLUMN_WINS, outcome.equals("Win") ? 1 : 0);
            values.put(GoalsContract.GoalsEntry.COLUMN_DRAW, outcome.equals("Draw") ? 1 : 0);
            values.put(GoalsContract.GoalsEntry.COLUMN_LOST, outcome.equals("Loss") ? 1 : 0);
            values.put(GoalsContract.GoalsEntry.COLUMN_GOALS, goalsScored);
            values.put(GoalsContract.GoalsEntry.COLUMN_GOALS_CONCEDED, goalsConceded);
            values.put(GoalsContract.GoalsEntry.COLUMN_POINTS, outcome.equals("Win") ? 3 : (outcome.equals("Draw") ? 1 : 0));


            database.insert(GoalsContract.GoalsEntry.TABLE_NAME, null, values);


        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private String invertOutcome(String outcome) {
        switch (outcome) {
            case "Win":
                return "Loss";
            case "Loss":
                return "Win";
            case "Draw":
                return "Draw";
            default:
                return "";
        }
    }

    public Cursor getMatchDetails(long id) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();

        // Use SQLiteQueryBuilder to build the query
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MatchContract.MatchEntry.TABLE_NAME);

        // Specify the columns you want to retrieve
        String[] projection = {
                MatchContract.MatchEntry._ID,
                MatchContract.MatchEntry.COLUMN_DATE,
                MatchContract.MatchEntry.COLUMN_CITY,
                MatchContract.MatchEntry.COLUMN_GROUP_A,
                MatchContract.MatchEntry.COLUMN_GROUP_B,
                MatchContract.MatchEntry.COLUMN_OUTCOME,
                MatchContract.MatchEntry.COLUMN_NUM_GOALS_A,
                MatchContract.MatchEntry.COLUMN_NUM_GOALS_B
        };

        // Build the WHERE clause with the provided ID
        String selection = MatchContract.MatchEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        // Execute the query and return the Cursor
        return queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }


    public Cursor getAllMatches() {
        return database.query(
                MatchContract.MatchEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


    public boolean updateMatchDetails(long id, String date, String city, String groupA, String groupB, int goalsA, int goalsB, String outcome) {
        ContentValues values = new ContentValues();
        values.put(MatchContract.MatchEntry.COLUMN_DATE, date);
        values.put(MatchContract.MatchEntry.COLUMN_CITY, city);
        values.put(MatchContract.MatchEntry.COLUMN_GROUP_A, groupA);
        values.put(MatchContract.MatchEntry.COLUMN_GROUP_B, groupB);
        values.put(MatchContract.MatchEntry.COLUMN_NUM_GOALS_A, goalsA);
        values.put(MatchContract.MatchEntry.COLUMN_NUM_GOALS_B, goalsB);
        values.put(MatchContract.MatchEntry.COLUMN_OUTCOME, outcome);

        int rowsUpdated = database.update(MatchContract.MatchEntry.TABLE_NAME,
                values,
                MatchContract.MatchEntry._ID + "=?",
                new String[]{String.valueOf(id)});

        // Update the goals table for the respective groups
        updateGroupGoals(groupA, goalsA, goalsB, outcome);
        updateGroupGoals(groupB, goalsB, goalsA, invertOutcome(outcome));

        return rowsUpdated > 0;
    }

    @SuppressLint("Range")
    public void deleteMatch(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Retrieve the match details before deleting
        Cursor cursor = db.query(MatchContract.MatchEntry.TABLE_NAME,
                new String[]{MatchContract.MatchEntry.COLUMN_GROUP_A, MatchContract.MatchEntry.COLUMN_GROUP_B,
                        MatchContract.MatchEntry.COLUMN_NUM_GOALS_A, MatchContract.MatchEntry.COLUMN_NUM_GOALS_B,
                        MatchContract.MatchEntry.COLUMN_OUTCOME},
                MatchContract.MatchEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String groupA = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_GROUP_A));
            String groupB = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_GROUP_B));
            int goalsA = cursor.getInt(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NUM_GOALS_A));
            int goalsB = cursor.getInt(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NUM_GOALS_B));
            String outcome = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_OUTCOME));

            cursor.close();

            // Delete the match from the database
            String selection = MatchContract.MatchEntry._ID + "=?";
            String[] selectionArgs = {String.valueOf(id)};
            db.delete(MatchContract.MatchEntry.TABLE_NAME, selection, selectionArgs);

            // Update the team stats for group A
            updateGroupGoalsOnDelete(groupA, goalsA, goalsB, outcome);

            // Update the team stats for group B
            updateGroupGoalsOnDelete(groupB, goalsB, goalsA, invertOutcome(outcome));
        }
    }

    @SuppressLint("Range")
    private void updateGroupGoalsOnDelete(String group, int goalsScored, int goalsConceded, String outcome) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Retrieve the current stats for the group
        String selection = GoalsContract.GoalsEntry.COLUMN_GROUP + "=?";
        String[] selectionArgs = {group};

        Cursor cursor = db.query(GoalsContract.GoalsEntry.TABLE_NAME,
                new String[]{GoalsContract.GoalsEntry.COLUMN_GAMES, GoalsContract.GoalsEntry.COLUMN_WINS,
                        GoalsContract.GoalsEntry.COLUMN_DRAW, GoalsContract.GoalsEntry.COLUMN_LOST,
                        GoalsContract.GoalsEntry.COLUMN_GOALS, GoalsContract.GoalsEntry.COLUMN_GOALS_CONCEDED,
                        GoalsContract.GoalsEntry.COLUMN_POINTS},
                selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int games = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GAMES));
            int wins = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_WINS));
            int draws = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_DRAW));
            int losses = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_LOST));
            int goals = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GOALS));
            int goalsConcededTotal = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GOALS_CONCEDED));
            int points = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_POINTS));

            cursor.close();

            // Decrement the stats based on the deleted match
            games--;
            goals -= goalsScored;
            goalsConcededTotal -= goalsConceded;

            if (outcome.equals("win")) {
                wins--;
                points -= 3;
            } else if (outcome.equals("draw")) {
                draws--;
                points--;
            } else if (outcome.equals("loss")) {
                losses--;
            }

            // Check if the team has any remaining matches
            if (games == 0) {
                // If no matches remain, delete the team from the goals table
                db.delete(GoalsContract.GoalsEntry.TABLE_NAME, selection, selectionArgs);
            } else {
                // Update the team stats in the database
                ContentValues values = new ContentValues();
                values.put(GoalsContract.GoalsEntry.COLUMN_GAMES, games);
                values.put(GoalsContract.GoalsEntry.COLUMN_WINS, wins);
                values.put(GoalsContract.GoalsEntry.COLUMN_DRAW, draws);
                values.put(GoalsContract.GoalsEntry.COLUMN_LOST, losses);
                values.put(GoalsContract.GoalsEntry.COLUMN_GOALS, goals);
                values.put(GoalsContract.GoalsEntry.COLUMN_GOALS_CONCEDED, goalsConcededTotal);
                values.put(GoalsContract.GoalsEntry.COLUMN_POINTS, points);

                db.update(GoalsContract.GoalsEntry.TABLE_NAME, values, selection, selectionArgs);
            }
        }
    }


    @SuppressLint("Range")
    public List<TeamStats> getAllTeamStats() {
        List<TeamStats> teamStatsList = new ArrayList<>();

        Cursor cursor = database.query(
                GoalsContract.GoalsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String teamName = cursor.getString(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GROUP));
                int games = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GAMES));
                int wins = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_WINS));
                int draws = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_DRAW));
                int losses = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_LOST));
                int goalsScored = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GOALS));
                int goalsConceded = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GOALS_CONCEDED)); // Add this column to the goals table if it doesn't exist
                int points = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_POINTS));

                TeamStats teamStats = new TeamStats(teamName, games, wins, draws, losses, goalsScored, goalsConceded, points);
                teamStatsList.add(teamStats);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return teamStatsList;
    }

    @SuppressLint("Range")
    public void updateTeamStats(String teamName, int goalsScored, int goalsConceded, String outcome) {
        // Retrieve the current stats for the team
        String selection = GoalsContract.GoalsEntry.COLUMN_GROUP + " = ?";
        String[] selectionArgs = {teamName};

        Cursor cursor = database.query(
                GoalsContract.GoalsEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int games = 0;
        int wins = 0;
        int draws = 0;
        int losses = 0;
        int goals = 0;
        int points = 0;

        if (cursor != null && cursor.moveToFirst()) {
            games = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GAMES));
            wins = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_WINS));
            draws = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_DRAW));
            losses = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_LOST));
            goals = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_GOALS));
            points = cursor.getInt(cursor.getColumnIndex(GoalsContract.GoalsEntry.COLUMN_POINTS));
        }

        if (cursor != null) {
            cursor.close();
        }

        // Update the stats based on the match outcome
        games++;
        goals += goalsScored;

        if (outcome.equals("Win")) {
            wins++;
            points += 3;
        } else if (outcome.equals("Draw")) {
            draws++;
            points += 1;
        } else if (outcome.equals("Loss")) {
            losses++;
        }

        // Update the team stats in the database
        ContentValues values = new ContentValues();
        values.put(GoalsContract.GoalsEntry.COLUMN_GAMES, games);
        values.put(GoalsContract.GoalsEntry.COLUMN_WINS, wins);
        values.put(GoalsContract.GoalsEntry.COLUMN_DRAW, draws);
        values.put(GoalsContract.GoalsEntry.COLUMN_LOST, losses);
        values.put(GoalsContract.GoalsEntry.COLUMN_GOALS, goals);
        values.put(GoalsContract.GoalsEntry.COLUMN_GOALS_CONCEDED, goalsConceded);
        values.put(GoalsContract.GoalsEntry.COLUMN_POINTS, points);

        database.update(
                GoalsContract.GoalsEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }


    public Cursor getAllGoals() {
        return database.query(
                GoalsContract.GoalsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


    public boolean updateMatchResult(long id, String date, String city, String groupA, String groupB, int goalsA, int goalsB, String outcome) {
        ContentValues values = new ContentValues();
        values.put(MatchContract.MatchEntry.COLUMN_DATE, date);
        values.put(MatchContract.MatchEntry.COLUMN_CITY, city);
        values.put(MatchContract.MatchEntry.COLUMN_GROUP_A, groupA);
        values.put(MatchContract.MatchEntry.COLUMN_GROUP_B, groupB);
        values.put(MatchContract.MatchEntry.COLUMN_NUM_GOALS_A, goalsA);
        values.put(MatchContract.MatchEntry.COLUMN_NUM_GOALS_B, goalsB);
        values.put(MatchContract.MatchEntry.COLUMN_OUTCOME, outcome);

        int rowsUpdated = database.update(MatchContract.MatchEntry.TABLE_NAME,
                values,
                MatchContract.MatchEntry._ID + "=?",
                new String[]{String.valueOf(id)});

        return rowsUpdated > 0;
    }

    public void updateTeamStatsOnMatchUpdate(String oldGroup, int oldGoalsScored, int oldGoalsConceded, String oldOutcome,
                                             String newGroup, int newGoalsScored, int newGoalsConceded, String newOutcome) {
        // Revert the old team stats
        updateGroupGoalsOnDelete(oldGroup, oldGoalsScored, oldGoalsConceded, oldOutcome);

        // Update the new team stats
        updateGroupGoals(newGroup, newGoalsScored, newGoalsConceded, newOutcome);
    }


}

