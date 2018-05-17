package com.tachyonlabs.practicetodoapp.widget;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.data.TodoListContract;
import com.tachyonlabs.practicetodoapp.data.TodoListProvider;
import com.tachyonlabs.practicetodoapp.models.TodoTask;
import com.tachyonlabs.practicetodoapp.utils.TodoDateUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class TodoListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = TodoListWidgetRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private Resources mRes;
    private Cursor mCursor;
    private int mDescriptionIndex;
    private int mPriorityIndex;
    private int mDueDateIndex;
    private int m_IDIndex;
    private int mCompletedIndex;

    public TodoListWidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mRes = mContext.getResources();
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
            sortOrder = TodoListProvider.SORT_ORDER_PRIORITY;
        } else {
            sortOrder = TodoListProvider.SORT_ORDER_DUEDATE;
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
        mCompletedIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_COMPLETED);

        String dueDateString;
        long dueDate = mCursor.getLong(mDueDateIndex);
        if (dueDate == TodoTask.NO_DUE_DATE) {
            dueDateString = mContext.getString(R.string.no_due_date);
        } else {
            dueDateString = TodoDateUtils.formatDueDate(mContext, dueDate);
        }
        int[] priorityStars = {R.drawable.ic_star_red_24dp, R.drawable.ic_star_orange_24dp, R.drawable.ic_star_yellow_24dp};
        int completedStar = R.drawable.ic_star_grey_24dp;
        String[] priorityContentDescriptions = {mContext.getString(R.string.high_priority),
                mContext.getString(R.string.medium_priority),
                mContext.getString(R.string.low_priority)};

        int priority = mCursor.getInt(mPriorityIndex);
        int isCompleted = mCursor.getInt(mCompletedIndex);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_todo_list_widget);
        rv.setTextViewText(R.id.tv_widget_todo_description, mCursor.getString(mDescriptionIndex));
        rv.setContentDescription(R.id.iv_widget_todo_priority_star, priorityContentDescriptions[priority]);
        Log.d(TAG, priorityContentDescriptions[priority]);

        if (isCompleted == TodoTask.TASK_COMPLETED) {
            rv.setTextColor(R.id.tv_widget_todo_description, mRes.getColor(R.color.colorCompleted));
            rv.setInt(R.id.iv_widget_todo_priority_star, "setBackgroundResource", completedStar);
            rv.setTextViewText(R.id.tv_widget_todo_due_date, mContext.getString(R.string.completed));
            rv.setInt(R.id.ll_widget_todo_item_layout, "setBackgroundColor", mRes.getColor(R.color.colorCompletedBackground));
        } else {
            rv.setTextColor(R.id.tv_widget_todo_description, mRes.getColor(R.color.colorPrimaryDark));
            rv.setInt(R.id.iv_widget_todo_priority_star, "setBackgroundResource", priorityStars[priority]);
            rv.setTextViewText(R.id.tv_widget_todo_due_date, dueDateString);
            rv.setInt(R.id.ll_widget_todo_item_layout, "setBackgroundColor", mRes.getColor(R.color.colorUncompletedBackground));
            if (dueDate < TodoDateUtils.getTodaysDateInMillis()) {
                // display overdue tasks with the date in red
                // yeah, I know red for both overdue and high priority may be not the best idea
                rv.setTextColor(R.id.tv_widget_todo_due_date, mRes.getColor(R.color.colorOverdue));
            } else {
                rv.setTextColor(R.id.tv_widget_todo_due_date, mRes.getColor(R.color.colorNormalWidgetText));
            }
        }

        TodoTask todoTask = new TodoTask(mCursor.getString(mDescriptionIndex),
                mCursor.getInt(mPriorityIndex),
                mCursor.getLong(mDueDateIndex),
                mCursor.getInt(m_IDIndex),
                mCursor.getInt(mCompletedIndex));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(mContext.getString(R.string.intent_todo_key), todoTask);
        fillInIntent.putExtra(mContext.getString(R.string.intent_adding_or_editing_key), mContext.getString(R.string.edit_task));
        rv.setOnClickFillInIntent(R.id.ll_widget_todo_item_layout, fillInIntent);

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
