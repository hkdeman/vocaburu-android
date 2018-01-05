package com.welcomebuddy.randomtech.vocabsensei.Database;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract() {}

    public static class QuizEntry implements BaseColumns {
        public static final String TABLE_NAME = "score_track";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_DETAIL = "detail";
    }

}
