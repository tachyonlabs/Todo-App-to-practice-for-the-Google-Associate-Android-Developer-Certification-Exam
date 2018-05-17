package com.tachyonlabs.practicetodoapp.activities;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.adapters.TodoListAdapter;
import com.tachyonlabs.practicetodoapp.broadcast_receivers.DailyAlarmReceiver;
import com.tachyonlabs.practicetodoapp.data.TodoListContract;
import com.tachyonlabs.practicetodoapp.data.TodoListProvider;
import com.tachyonlabs.practicetodoapp.databinding.ActivityTodoListBinding;
import com.tachyonlabs.practicetodoapp.models.TodoTask;
import com.tachyonlabs.practicetodoapp.utils.NotificationUtils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

public class TodoListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        TodoListAdapter.TodoListAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = TodoListActivity.class.getSimpleName();
    private static final int ADD_TASK_REQUEST = 1;
    private static final int EDIT_TASK_REQUEST = 2;
    private static final int ID_TODOLIST_LOADER = 2018;

    private RecyclerView mRecyclerView;
    private TodoListAdapter mTodoListAdapter;
    private ActivityTodoListBinding mBinding;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_todo_list);

        mRecyclerView = mBinding.rvTodoList;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mTodoListAdapter = new TodoListAdapter(this, this);
        mRecyclerView.setAdapter(mTodoListAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        FloatingActionButton fab = mBinding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoListActivity.this, AddOrEditTaskActivity.class);
                intent.putExtra(getString(R.string.intent_adding_or_editing_key), getString(R.string.add_new_task));
                startActivityForResult(intent, ADD_TASK_REQUEST);
            }
        });

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        getSupportLoaderManager().initLoader(ID_TODOLIST_LOADER, null, this);

        //scheduleDailyDueCheckerAlarm();
        //cancelAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todo_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_delete_test:
                // later a service will take care of this
                deleteAllCheckedTasks();
                break;
            case R.id.action_test_something:
                // just a place for me to test whatever I'm testing at the moment
                NotificationUtils.notifyUserOfDueAndOverdueTasks(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(TodoTask todoTask, View view) {
        // are they checking or unchecking the task checkbox, or tapping the task to edit it?
        if (view instanceof CheckBox) {
            // checking off task gets it flagged for deletion soon, and unchecking it reprieves it
            final String id = String.valueOf(todoTask.getId());
            final Uri uri = TodoListContract.TodoListEntry.CONTENT_URI.buildUpon().appendPath(id).build();
            int isCompleted;

            final ContentValues contentValues = new ContentValues();
            contentValues.put(TodoListContract.TodoListEntry.COLUMN_DESCRIPTION, todoTask.getDescription());
            contentValues.put(TodoListContract.TodoListEntry.COLUMN_PRIORITY, todoTask.getPriority());
            contentValues.put(TodoListContract.TodoListEntry.COLUMN_DUE_DATE, todoTask.getDueDate());

            if (((CheckBox) view).isChecked()) {
                isCompleted = TodoTask.TASK_COMPLETED;
            } else {
                isCompleted = TodoTask.TASK_NOT_COMPLETED;
            }

            contentValues.put(TodoListContract.TodoListEntry.COLUMN_COMPLETED, isCompleted);

            // Wait half a second so they can briefly see the check appear or disappear before
            // the task is moved to or from the bottom
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    getContentResolver().update(uri, contentValues, "_id=?", new String[]{id});
                    updateWidget();
                }
            }, 500);
        } else {
            // edit the task
            Intent intent = new Intent(this, AddOrEditTaskActivity.class);
            intent.putExtra(getString(R.string.intent_adding_or_editing_key), getString(R.string.edit_task));
            intent.putExtra(getString(R.string.intent_todo_key), todoTask);
            startActivityForResult(intent, EDIT_TASK_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            updateWidget();
        }
    }

    private void updateWidget() {
        // let the widget know there's been a database or sort order change
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        if (loaderId == ID_TODOLIST_LOADER) {
            String sortOrderPreference = getSortOrderPreference();
            String sortOrder;
            Uri todoListQueryUri = TodoListContract.TodoListEntry.CONTENT_URI;
            // sort order preference is the primary sort, with the other sort order as secondary
            if (sortOrderPreference.equals(getString(R.string.priority))) {
                sortOrder = TodoListProvider.SORT_ORDER_PRIORITY;
            } else {
                sortOrder = TodoListProvider.SORT_ORDER_DUEDATE;
            }

            return new CursorLoader(this,
                    todoListQueryUri,
                    null,
                    null,
                    null,
                    sortOrder);
        } else {
            throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTodoListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    private String getSortOrderPreference() {
        return mSharedPreferences.getString(getString(R.string.pref_sort_by_key), getString(R.string.priority));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        mTodoListAdapter.swapCursor(null);
        getSupportLoaderManager().restartLoader(ID_TODOLIST_LOADER, null, this);
        updateWidget();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // This is so that if we've edited a task directly from the widget, the widget will still
        // get updated when we come to this activity after clicking UPDATE TASK in AddOrEditTaskActivity
        updateWidget();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void deleteAllCheckedTasks() {
        // this is just for testing, later a service will do it periodically
        Uri uri = TodoListContract.TodoListEntry.CONTENT_URI;
        int deletedTasksCount = getContentResolver().delete(uri, "completed=?", new String[]{String.valueOf(TodoTask.TASK_COMPLETED)});
        if (deletedTasksCount > 0) {
            updateWidget();
        }
    }

    public void scheduleDailyDueCheckerAlarm() {
        Intent intent = new Intent(getApplicationContext(), DailyAlarmReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                DailyAlarmReceiver.REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); // alarm is set right away

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), DailyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, DailyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}
