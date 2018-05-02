package com.tachyonlabs.practicetodoapp.utils;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.activities.TodoListActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils {
    private static final int PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_ID = 8785;
    private static final int PRACTICE_TODO_APP_PENDING_INTENT_ID = 3757;
    private static final String PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_CHANNEL_ID = "practice_todo_app_notification_channel";

    private static PendingIntent contentIntent(Context context) {
        Intent startTodoListActivityIntent = new Intent(context, TodoListActivity.class);
        return PendingIntent.getActivity(
                context,
                PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_ID,
                startTodoListActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void notifyUserOfDueAndOverdueTasks(Context context) {
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
                .setContentTitle("You have due and overdue tasks")
                .setContentText("You have three tasks due today, and three overdue tasks.")
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(PRACTICE_TODO_APP_DUE_AND_OVERDUE_NOTIFICATION_ID, notificationBuilder.build());
    }
}
