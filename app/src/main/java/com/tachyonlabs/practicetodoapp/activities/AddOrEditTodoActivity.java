package com.tachyonlabs.practicetodoapp.activities;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.databinding.ActivityAddOrEditTodoBinding;
import com.tachyonlabs.practicetodoapp.models.Todo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AddOrEditTodoActivity extends AppCompatActivity {
    private ActivityAddOrEditTodoBinding mBinding;

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
            Todo todoToEdit = bundle.getParcelable(getString(R.string.intent_todo_key));
            mBinding.etTaskDescription.setText(todoToEdit.getDescription());

            switch (todoToEdit.getPriority()) {
                case 0:
                    mBinding.rbHighPriority.setChecked(true);
                    break;
                case 1:
                    mBinding.rbMediumPriority.setChecked(true);
                    break;
                case 2:
                    mBinding.rbLowPriority.setChecked(true);
            }

            if (todoToEdit.getDate() == 0) {
                mBinding.rbNoDueDate.setChecked(true);
            } else {

            }
        }

    }

    public void addOrUpdateTask(View view) {
        finish();
    }
}
