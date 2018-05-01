package com.tachyonlabs.practicetodoapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TodoListProvider extends ContentProvider{
    public static final int CODE_TASKS = 100;
    public static final int CODE_TASK_WITH_ID = 101;
    // tasks are sorted first by completion, then by the selected sort order, then by the other
    // sort order, then by the description
    public static final String SORT_ORDER_PRIORITY = TodoListContract.TodoListEntry.COLUMN_COMPLETED + ", " + TodoListContract.TodoListEntry.COLUMN_PRIORITY + ", " + TodoListContract.TodoListEntry.COLUMN_DUE_DATE + ", " + TodoListContract.TodoListEntry.COLUMN_DESCRIPTION;
    public static final String SORT_ORDER_DUEDATE = TodoListContract.TodoListEntry.COLUMN_COMPLETED + ", " + TodoListContract.TodoListEntry.COLUMN_DUE_DATE + ", " + TodoListContract.TodoListEntry.COLUMN_PRIORITY + ", " + TodoListContract.TodoListEntry.COLUMN_DESCRIPTION;

    private static UriMatcher sUriMatcher = buildUriMatcher();
    private TodoListDbHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new TodoListDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_TASKS:
                cursor = mOpenHelper.getReadableDatabase().query(
                        TodoListContract.TodoListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CODE_TASKS:
                long id = db.insert(TodoListContract.TodoListEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(TodoListContract.TodoListEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        switch (match) {
            case CODE_TASK_WITH_ID:
                tasksDeleted = db.delete(TodoListContract.TodoListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_TASKS:
                tasksDeleted = db.delete(TodoListContract.TodoListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksUpdated;

        switch (match) {
            case CODE_TASK_WITH_ID:
                tasksUpdated = db.update(TodoListContract.TodoListEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksUpdated;
    }

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TodoListContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, TodoListContract.PATH_TODOLIST, CODE_TASKS);
        matcher.addURI(authority, TodoListContract.PATH_TODOLIST + "/#", CODE_TASK_WITH_ID);
        return matcher;
    }

}
