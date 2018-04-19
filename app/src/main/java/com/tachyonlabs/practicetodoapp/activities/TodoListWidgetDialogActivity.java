package com.tachyonlabs.practicetodoapp.activities;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.data.TodoListContract;
import com.tachyonlabs.practicetodoapp.databinding.ActivityTodoListWidgetDialogBinding;
import com.tachyonlabs.practicetodoapp.models.TodoTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TodoListWidgetDialogActivity extends AppCompatActivity {
    private ActivityTodoListWidgetDialogBinding mBinding;
    private TodoTask mTodoTaskToCheckUncheckOrEdit;
    private static final int COMPLETED = 1;
    private static final int NOT_COMPLETED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String instructions, title, leftButtonText, rightButtonText;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_todo_list_widget_dialog);
        Bundle bundle = getIntent().getExtras();
        mTodoTaskToCheckUncheckOrEdit = bundle.getParcelable(getString(R.string.intent_todo_key));

        if (mTodoTaskToCheckUncheckOrEdit.getCompleted() == COMPLETED) {
            title = "Uncomplete task?";
            leftButtonText = "Uncomplete";
            rightButtonText = getString(R.string.cancel);
            instructions = "You selected the completed task \"" + mTodoTaskToCheckUncheckOrEdit.getDescription() + "\".\n\n" +
                    "Tap \"UNCOMPLETE\" to mark the task as uncompleted, or \"CANCEL\" to cancel.";
        } else {
            title = getString(R.string.complete_or_edit_task);
            leftButtonText = getString(R.string.task_done);
            rightButtonText = getString(R.string.edit_task);
            instructions = "You selected the task \"" + mTodoTaskToCheckUncheckOrEdit.getDescription() + "\".\n\n" +
                    "Tap \"TASK DONE\" if you've completed the task, or \"EDIT TASK\" to edit it.";
        }

        setTitle(title);
        mBinding.tvWidgetDialogInstructions.setText(instructions);
        mBinding.btnLeftWidgetDialog.setText(leftButtonText);
        mBinding.btnRightWidgetDialog.setText(rightButtonText);
    }

    public void taskCheckOrUncheck(View v) {
        closeDialogAndRemoveFromBackstack();

        // Complete or uncomplete the task
        final String id = String.valueOf(mTodoTaskToCheckUncheckOrEdit.getId());
        final Uri uri = TodoListContract.TodoListEntry.CONTENT_URI.buildUpon().appendPath(id).build();
        int changeCompletionStatus;

        final ContentValues contentValues = new ContentValues();
        contentValues.put(TodoListContract.TodoListEntry.COLUMN_DESCRIPTION, mTodoTaskToCheckUncheckOrEdit.getDescription());
        contentValues.put(TodoListContract.TodoListEntry.COLUMN_PRIORITY, mTodoTaskToCheckUncheckOrEdit.getPriority());
        contentValues.put(TodoListContract.TodoListEntry.COLUMN_DUE_DATE, mTodoTaskToCheckUncheckOrEdit.getDueDate());

        if (mTodoTaskToCheckUncheckOrEdit.getCompleted() == NOT_COMPLETED) {
            changeCompletionStatus = COMPLETED;
        } else {
            changeCompletionStatus = NOT_COMPLETED;
        }

        contentValues.put(TodoListContract.TodoListEntry.COLUMN_COMPLETED, changeCompletionStatus);

        getContentResolver().update(uri, contentValues, "_id=?", new String[]{id});

        // and update the widget accordingly
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(intent);
    }

    public void editTask(View v) {
        closeDialogAndRemoveFromBackstack();

        // If the task has not already been completed -- bring up AddOrEditTaskActivity
        // with the task loaded for editing, and TodoListActivity as its backstack
        if (mTodoTaskToCheckUncheckOrEdit.getCompleted() == NOT_COMPLETED) {
            Intent intent = new Intent(this, AddOrEditTaskActivity.class);
            intent.putExtra(getString(R.string.intent_adding_or_editing_key), getString(R.string.edit_task));
            intent.putExtra(getString(R.string.intent_todo_key), mTodoTaskToCheckUncheckOrEdit);
            PendingIntent pendingIntent = TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                pendingIntent.send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void closeDialogAndRemoveFromBackstack() {
        // We don't want this dialog to stay on the backstack regardless of whether we just
        // return to the app widget, or proceed to edit the selected task
        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finishAffinity();
        }
    }
}
