package com.tachyonlabs.practicetodoapp.activities;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.databinding.ActivityAddOrEditTodoBinding;
import com.tachyonlabs.practicetodoapp.models.Todo;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

public class AddOrEditTodoActivity extends AppCompatActivity {
    private static final String TAG = AddOrEditTodoActivity.class.getSimpleName();
    private ActivityAddOrEditTodoBinding mBinding;
    private int todoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_or_edit_todo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        long dueDate;

        Bundle bundle = getIntent().getExtras();
        String addOrEdit = bundle.getString(getString(R.string.intent_adding_or_editing_key));

        setTitle(addOrEdit);
        if (savedInstanceState == null) {
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

                selectPriorityRadioButton(todoToAddOrEdit.getPriority());

                dueDate = todoToAddOrEdit.getDueDate();
                Log.d(TAG, "Due date in millis " + dueDate);
                if (dueDate == Long.MAX_VALUE) {
                    mBinding.rbNoDueDate.setChecked(true);
                } else {
                    mBinding.rbSelectDueDate.setChecked(true);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(dueDate);
                    mBinding.dpDueDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                }
            }
        } else {
            mBinding.etTaskDescription.setText(savedInstanceState.getString(getString(R.string.task_description_key)));
            selectPriorityRadioButton(savedInstanceState.getInt(getString(R.string.priority_key)));
            boolean noDueDate = savedInstanceState.getBoolean(getString(R.string.no_due_date_key));
            if (noDueDate) {
                mBinding.rbNoDueDate.setChecked(true);
            } else {
                mBinding.rbSelectDueDate.setChecked(true);
            }
            mBinding.dpDueDate.updateDate(savedInstanceState.getInt(getString(R.string.year_key)),
                    savedInstanceState.getInt(getString(R.string.month_key)),
                    savedInstanceState.getInt(getString(R.string.day_key)));
        }
    }

    private void selectPriorityRadioButton(int priority) {
        switch (priority) {
            case 0:
                mBinding.rbHighPriority.setChecked(true);
                break;
            case 1:
                mBinding.rbMediumPriority.setChecked(true);
                break;
            case 2:
                mBinding.rbLowPriority.setChecked(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save values on rotation
        outState.putString(getString(R.string.task_description_key), mBinding.etTaskDescription.getText().toString());
        int priority = 0;
        if (mBinding.rbMediumPriority.isChecked()) {
            priority = 1;
        } else if (mBinding.rbLowPriority.isChecked()) {
            priority = 2;
        }
        outState.putInt(getString(R.string.priority_key), priority);
        outState.putBoolean(getString(R.string.no_due_date), mBinding.rbNoDueDate.isChecked());
        outState.putInt(getString(R.string.year_key), mBinding.dpDueDate.getYear());
        outState.putInt(getString(R.string.month_key), mBinding.dpDueDate.getMonth());
        outState.putInt(getString(R.string.day_key), mBinding.dpDueDate.getDayOfMonth());
        super.onSaveInstanceState(outState);
    }

    public void addOrUpdateTask(View view) {
        String description = mBinding.etTaskDescription.getText().toString().trim();
        int priority = 0;
        long dueDate = Long.MAX_VALUE;

        if (description.equals("")) {
            Toast.makeText(this, getString(R.string.description_cannot_be_empty), Toast.LENGTH_LONG).show();
        } else {
            // get the priority setting
            if (mBinding.rbMediumPriority.isChecked()) {
                priority = 1;
            } else if (mBinding.rbLowPriority.isChecked()) {
                priority = 2;
            }

            // get the due date, if one has been selected
            if (mBinding.rbSelectDueDate.isChecked()) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(mBinding.dpDueDate.getYear(), mBinding.dpDueDate.getMonth(), mBinding.dpDueDate.getDayOfMonth());
                dueDate = calendar.getTimeInMillis();
            }
            Todo todo = new Todo(description, priority, dueDate, todoId);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(getString(R.string.intent_todo_key), todo);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
