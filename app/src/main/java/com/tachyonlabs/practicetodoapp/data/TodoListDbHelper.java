package com.tachyonlabs.practicetodoapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoListDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 3;

    public TodoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TODOLIST_TABLE =

                "CREATE TABLE " + TodoListContract.TodoListEntry.TABLE_NAME + " (" +
                        TodoListContract.TodoListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TodoListContract.TodoListEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        TodoListContract.TodoListEntry.COLUMN_PRIORITY + " INTEGER NOT NULL, " +
                        TodoListContract.TodoListEntry.COLUMN_DUE_DATE + " LONG NOT NULL, " +
                        TodoListContract.TodoListEntry.COLUMN_COMPLETED + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_TODOLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TodoListContract.TodoListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
