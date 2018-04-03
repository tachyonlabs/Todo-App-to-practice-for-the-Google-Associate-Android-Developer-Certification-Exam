package com.tachyonlabs.practicetodoapp.activities;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.adapters.TodoListAdapter;
import com.tachyonlabs.practicetodoapp.data.TodoListContract;
import com.tachyonlabs.practicetodoapp.databinding.ActivityTodoListBinding;
import com.tachyonlabs.practicetodoapp.models.Todo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class TodoListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, TodoListAdapter.TodoListAdapterOnClickHandler {
    private RecyclerView mRecyclerView;
    private TodoListAdapter mTodoListAdapter;
    private ActivityTodoListBinding mBinding;

    private static final int ADD_TASK_REQUEST = 1;
    private static final int EDIT_TASK_REQUEST = 2;
    private static final int ID_TODOLIST_LOADER = 2018;

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
                Intent intent = new Intent(TodoListActivity.this, AddOrEditTodoActivity.class);
                intent.putExtra(getString(R.string.intent_adding_or_editing_key), getString(R.string.add_new_task));
                startActivityForResult(intent, ADD_TASK_REQUEST);
            }
        });

        getSupportLoaderManager().initLoader(ID_TODOLIST_LOADER, null, this);
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
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Todo todo) {
        Intent intent = new Intent(this, AddOrEditTodoActivity.class);
        intent.putExtra(getString(R.string.intent_adding_or_editing_key), getString(R.string.edit_task));
        intent.putExtra(getString(R.string.intent_todo_key), todo);
        startActivityForResult(intent, EDIT_TASK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Todo todo = data.getParcelableExtra(getString(R.string.intent_todo_key));
            switch (requestCode) {
                case ADD_TASK_REQUEST:
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(TodoListContract.TodoListEntry.COLUMN_DESCRIPTION, todo.getDescription());
                    contentValues.put(TodoListContract.TodoListEntry.COLUMN_PRIORITY, todo.getPriority());
                    contentValues.put(TodoListContract.TodoListEntry.COLUMN_DUE_DATE, todo.getDueDate());

                    Uri uri = getContentResolver().insert(TodoListContract.TodoListEntry.CONTENT_URI, contentValues);
                    break;
                case EDIT_TASK_REQUEST:
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        if (loaderId == ID_TODOLIST_LOADER) {
            Uri todoListQueryUri = TodoListContract.TodoListEntry.CONTENT_URI;
            String sortOrder = TodoListContract.TodoListEntry.COLUMN_PRIORITY + " ASC";

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
        mTodoListAdapter.swapCursor(null);
    }
}
