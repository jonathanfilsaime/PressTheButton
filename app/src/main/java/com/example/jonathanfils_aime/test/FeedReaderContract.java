package com.example.jonathanfils_aime.test;

import android.provider.BaseColumns;

/**
 * Created by jonathanfils-aime on 3/3/17.
 */

public class FeedReaderContract {
    private FeedReaderContract()
    {

    }

    public static class FeedEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "records";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_GOAL = "Goal";
        public static final String COLUMN_NAME_CURRENT = "Current";
        public static final String COLUMN_NAME_SINGLE_CLICK = "Single_Click";
        public static final String COLUMN_NAME_DOUBLE_CLICK = "Double_Click";
        public static final String COLUMN_NAME_LONG_PRESS = "Long_Press";
        public static final String COLUMN_NAME_PROFILE = "profile";
        public static final String COLUMN_NAME_SINGLE_CLICK_CHOICE = "single_click_choice";
        public static final String COLUMN_NAME_DOUBLE_CLICK_CHOICE = "single_click_choice";
        public static final String COLUMN_NAME_LONG_PRESS_CLICK_CHOICE = "single_click_choice";

    }
}
