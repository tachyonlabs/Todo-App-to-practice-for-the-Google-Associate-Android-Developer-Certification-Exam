package com.tachyonlabs.practicetodoapp.activities;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.databinding.ActivityAddOrEditTaskBinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AddOrEditTaskActivity extends AppCompatActivity {
    private ActivityAddOrEditTaskBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_or_edit_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String addOrEdit = bundle.getString(getString(R.string.adding_or_editing));
        setTitle(addOrEdit);
        if (addOrEdit.equals(getString(R.string.add_new_task))) {
            // when adding a task, default to high priority and no due date
            mBinding.rbHighPriority.setChecked(true);
            mBinding.rbNoDueDate.setChecked(true);
        } else {

        }

    }
}
