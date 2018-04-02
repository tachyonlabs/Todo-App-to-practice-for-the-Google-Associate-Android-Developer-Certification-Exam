package com.tachyonlabs.practicetodoapp.activities;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.adapters.TodoListAdapter;
import com.tachyonlabs.practicetodoapp.databinding.ActivityTodoListBinding;
import com.tachyonlabs.practicetodoapp.models.Todo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class TodoListActivity extends AppCompatActivity implements TodoListAdapter.TodoListAdapterOnClickHandler {
    private RecyclerView mRecyclerView;
    private TodoListAdapter mTodoListAdapter;
    private Todo[] todos;
    private ActivityTodoListBinding mBinding;

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

        displayFakeData();

        FloatingActionButton fab = mBinding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoListActivity.this, AddOrEditTodoActivity.class);
                intent.putExtra(getString(R.string.intent_adding_or_editing_key), getString(R.string.add_new_task));
                startActivity(intent);
            }
        });
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
        startActivity(intent);
    }

    private void displayFakeData() {
        // some fake data to start with
        String[] descriptions = {"Finish Android project", "Make garlic bread", "Tune ukulele", "Answer recruiter's email"};
        int[] priorities = {0, 2, 1, 0};
        int[] dates = {0, 0, 0, 0};
        todos = new Todo[descriptions.length];
        for (int i = 0; i < descriptions.length; i++) {
            todos[i] = new Todo(descriptions[i], priorities[i], dates[i]);
        }
        mTodoListAdapter.setTodoListData(todos);
    }

}
