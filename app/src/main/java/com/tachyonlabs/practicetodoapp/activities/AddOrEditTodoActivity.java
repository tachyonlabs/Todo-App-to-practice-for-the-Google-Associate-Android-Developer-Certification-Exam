package com.tachyonlabs.practicetodoapp.activities;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.databinding.ActivityAddOrEditTodoBinding;
import com.tachyonlabs.practicetodoapp.models.Todo;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class AddOrEditTodoActivity extends AppCompatActivity {
    private ActivityAddOrEditTodoBinding mBinding;
    private int todoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_or_edit_todo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String addOrEdit = bundle.getString(getString(R.string.intent_adding_or_editing_key));

        setTitle(addOrEdit);
        if (addOrEdit.equals(getString(R.string.add_new_task))) {
            // when adding a task, default to high priority and no due date
            mBinding.rbHighPriority.setChecked(true);
            mBinding.rbNoDueDate.setChecked(true);
            mBinding.btnAddOrUpdateTask.setText(R.string.add_task);
        } else {
            mBinding.btnAddOrUpdateTask.setText(R.string.update_task);
            Todo todoToAddOrEdit = bundle.getParcelable(getString(R.string.intent_todo_key));
            todoId = todoToAddOrEdit.getId();
            mBinding.etTaskDescription.setText(todoToAddOrEdit.getDescription());

            switch (todoToAddOrEdit.getPriority()) {
                case 0:
                    mBinding.rbHighPriority.setChecked(true);
                    break;
                case 1:
                    mBinding.rbMediumPriority.setChecked(true);
                    break;
                case 2:
                    mBinding.rbLowPriority.setChecked(true);
            }

            if (todoToAddOrEdit.getDueDate() == 0) {
                mBinding.rbNoDueDate.setChecked(true);
            } else {

            }
        }

    }

    public void addOrUpdateTask(View view) {
        String description = mBinding.etTaskDescription.getText().toString().trim();
        int priority = 0;
        int dueDate = 0;

        if (description.equals("")) {
            Toast.makeText(this, getString(R.string.description_cannot_be_empty), Toast.LENGTH_LONG).show();
        } else {
            if (mBinding.rbMediumPriority.isChecked()) {
                priority = 1;
            } else if (mBinding.rbLowPriority.isChecked()) {
                priority = 2;
            }
            Todo todo = new Todo(description, priority, dueDate, todoId);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(getString(R.string.intent_todo_key), todo);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
