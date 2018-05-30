package com.tachyonlabs.practicetodoapp.utils;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.activities.TodoListActivity;
import com.tachyonlabs.practicetodoapp.data.TodoListContract;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils {
    private static final int PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_ID = 8785;
    private static final int PRACTICE_TODO_APP_PENDING_INTENT_ID = 3757;
    private static final String PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_CHANNEL_ID = "practice_todo_app_notification_channel";
    private static final int DUE_TODAY = 1;
    private static final int OVERDUE = 0;

    private static PendingIntent contentIntent(Context context) {
        Intent startTodoListActivityIntent = new Intent(context, TodoListActivity.class);
        return PendingIntent.getActivity(
                context,
                PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_ID,
                startTodoListActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static int countDueOrOverdueTasks(Context context, int dueOrOverDue) {
        String today = Long.toString(TodoDateUtils.getTodaysDateInMillis());
        String equalsForDueOrLessThanForOverdue = null;
        int count;

        if (dueOrOverDue == DUE_TODAY) {
            equalsForDueOrLessThanForOverdue = " = ";
        } else if (dueOrOverDue == OVERDUE) {
            equalsForDueOrLessThanForOverdue = " < ";
        }

        String selection = TodoListContract.TodoListEntry.COLUMN_DUE_DATE +
                equalsForDueOrLessThanForOverdue + today + " AND " +
                TodoListContract.TodoListEntry.COLUMN_COMPLETED + " = 0";

        Cursor mCursor = context.getContentResolver().query(
                TodoListContract.TodoListEntry.CONTENT_URI,
                null,
                selection,
                null,
                null);

        if (mCursor != null) {
            count = mCursor.getCount();
        } else {
            count = 0;
        }

        mCursor.close();

        return count;
    }

    public static void notifyUserOfDueAndOverdueTasks(Context context) {
        int due = countDueOrOverdueTasks(context, DUE_TODAY);
        int overdue = countDueOrOverdueTasks(context, OVERDUE);
        String contentText = context.getString(R.string.you_have);

        // No due or overdue, no due or overdue notification needed
        if (due + overdue == 0) {
            return;
        }

        if (due == 0) {
            contentText += context.getResources().getQuantityString(R.plurals.overdueTasks, overdue, overdue);
        } else if (overdue == 0) {
            contentText += context.getResources().getQuantityString(R.plurals.dueTasks, due, due) +
                    context.getString(R.string.due_today) + ".";
        } else {
            contentText += context.getResources().getQuantityString(R.plurals.dueTasks, due, due) +
                    context.getString(R.string.due_today) + context.getString(R.string.and) +
                    context.getResources().getQuantityString(R.plurals.overdueTasks, overdue, overdue);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_CHANNEL_ID)
                        .setContentIntent(contentIntent(context))
                        .setSmallIcon(R.drawable.ic_notification_small_icon_24dp)
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setContentTitle(context.getString(R.string.due_and_overdue_tasks))
                        .setContentText(contentText)
                        .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_ID, notificationBuilder.build());
    }
}
