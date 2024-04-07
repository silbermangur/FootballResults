package com.gursil.footballresults.Models;

import android.provider.BaseColumns;

public class GoalsContract {
    private GoalsContract() {}

    public static class GoalsEntry implements BaseColumns {
        public static final String TABLE_NAME = "goals";
        public static final String COLUMN_GROUP = "group_name";
        public static final String COLUMN_GAMES = "games";
        public static final String COLUMN_WINS = "wins";
        public static final String COLUMN_DRAW = "draw";
        public static final String COLUMN_LOST = "lost";
        public static final String COLUMN_GOALS = "goals";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_GOALS_CONCEDED = "goals_conceded";
    }
}

