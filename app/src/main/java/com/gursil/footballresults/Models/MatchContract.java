package com.gursil.footballresults.Models;

import android.provider.BaseColumns;

public class MatchContract {
    private MatchContract() {}

    public static class MatchEntry implements BaseColumns {
        public static final String TABLE_NAME = "match_results";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_GROUP_A = "group_a";
        public static final String COLUMN_GROUP_B = "group_b";
        public static final String COLUMN_NUM_GOALS_A = "num_goals_a";
        public static final String COLUMN_NUM_GOALS_B = "num_goals_b";
        public static final String COLUMN_OUTCOME = "outcome";
    }
}

