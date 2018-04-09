package com.tachyonlabs.practicetodoapp.widget;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.data.TodoListContract;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class TodoListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private int mDescriptionIndex;
    private int mPriorityIndex;
    private int mDueDateIndex;
    private int m_IDIndex;

    public TodoListWidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        Uri uri = TodoListContract.TodoListEntry.CONTENT_URI;

        // Get the current sort order from SharedPreferences
        SharedPreferences sharedPreferencesForWidget = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortOrderPreference = sharedPreferencesForWidget.getString(mContext.getString(R.string.pref_sort_by_key), mContext.getString(R.string.priority));
        String sortOrder;

        // sort order preference is the primary sort, with the other sort order as secondary
        if (sortOrderPreference.equals(mContext.getString(R.string.priority))) {
            sortOrder = TodoListContract.TodoListEntry.COLUMN_PRIORITY + ", " + TodoListContract.TodoListEntry.COLUMN_DUE_DATE;
        } else {
            sortOrder = TodoListContract.TodoListEntry.COLUMN_DUE_DATE + ", " + TodoListContract.TodoListEntry.COLUMN_PRIORITY;
        }

        mCursor = mContext.getContentResolver().query(uri, null, null, null, sortOrder);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        mDescriptionIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_DESCRIPTION);
        mPriorityIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_PRIORITY);
        mDueDateIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_DUE_DATE);
        m_IDIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_ID);

        String dueDateString;
        long dueDate = mCursor.getLong(mDueDateIndex);
        if (dueDate == Long.MAX_VALUE) {
            dueDateString = mContext.getString(R.string.no_due_date);
        } else {
            dueDateString = DateUtils.formatDateTime(mContext, dueDate,
                    DateUtils.FORMAT_SHOW_DATE |
                            DateUtils.FORMAT_ABBREV_MONTH |
                            DateUtils.FORMAT_SHOW_YEAR |
                            DateUtils.FORMAT_ABBREV_WEEKDAY |
                            DateUtils.FORMAT_SHOW_WEEKDAY);
        }
        int priority = mCursor.getInt(mPriorityIndex);
        int[] priorityStars = {R.drawable.ic_star_red_24dp, R.drawable.ic_star_orange_24dp, R.drawable.ic_star_yellow_24dp};

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_todo_list_widget);
        rv.setTextViewText(R.id.tv_widget_todo_description, mCursor.getString(mDescriptionIndex));
        rv.setTextViewText(R.id.tv_widget_todo_due_date, dueDateString);
        rv.setInt(R.id.iv_widget_todo_priority_star, "setBackgroundResource", priorityStars[priority]);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        // all the ListView items will use the same layout
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
