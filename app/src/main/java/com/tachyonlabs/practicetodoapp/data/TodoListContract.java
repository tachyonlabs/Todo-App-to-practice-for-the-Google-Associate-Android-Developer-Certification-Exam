package com.tachyonlabs.practicetodoapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class TodoListContract {
    public static final String CONTENT_AUTHORITY = "com.tachyonlabs.practicetodoapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TODOLIST = "todolist";

    public static final class TodoListEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TODOLIST).build();

        public static final String TABLE_NAME = "todolist";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_DUE_DATE = "due_date";
        public static final String COLUMN_COMPLETED = "completed";
    }
}
