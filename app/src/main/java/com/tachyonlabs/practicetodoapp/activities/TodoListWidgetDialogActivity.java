package com.tachyonlabs.practicetodoapp.activities;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.data.TodoListContract;
import com.tachyonlabs.practicetodoapp.databinding.ActivityTodoListWidgetDialogBinding;
import com.tachyonlabs.practicetodoapp.models.TodoTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
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
    private TodoTask mTodoTaskToDeleteOrEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_todo_list_widget_dialog);
        Bundle bundle = getIntent().getExtras();
        mTodoTaskToDeleteOrEdit = bundle.getParcelable(getString(R.string.intent_todo_key));
        String instructions = "You selected the task \"" + mTodoTaskToDeleteOrEdit.getDescription() + "\".\n\n" +
                "Tap \"TASK DONE\" if you've completed the task, or \"EDIT TASK\" to edit it.";

        mBinding.tvWidgetDialogInstructions.setText(instructions);
    }

    public void taskDone(View v) {
        // close the dialog in a way that removes it from the backstack
        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finishAffinity();
        }
        // and delete the completed task
        final String id = String.valueOf(mTodoTaskToDeleteOrEdit.getId());
        final Uri uri = TodoListContract.TodoListEntry.CONTENT_URI.buildUpon().appendPath(id).build();
        getContentResolver().delete(uri, "_id=?", new String[]{id});

        // and update the widget accordingly
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(intent);
    }

    public void editTask(View v) {
        // close the dialog in a way that removes it from the backstack
        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finishAffinity();
        }

        // and bring up AddOrEditTaskActivity with the task loaded for editing, and
        // TodoListActivity as its backstack
        Intent intent = new Intent(this, AddOrEditTaskActivity.class);
        intent.putExtra(getString(R.string.intent_adding_or_editing_key), getString(R.string.edit_task));
        intent.putExtra(getString(R.string.intent_todo_key), mTodoTaskToDeleteOrEdit);
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
