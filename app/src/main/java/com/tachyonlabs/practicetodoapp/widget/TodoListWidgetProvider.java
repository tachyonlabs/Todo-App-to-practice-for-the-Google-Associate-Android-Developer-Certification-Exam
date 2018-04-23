package com.tachyonlabs.practicetodoapp.widget;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.activities.AddOrEditTaskActivity;
import com.tachyonlabs.practicetodoapp.activities.TodoListActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

public class TodoListWidgetProvider extends AppWidgetProvider {
    private final static String TAG = TodoListWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_list_widget);
        Intent intent = new Intent(context, TodoListWidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lv_widget, intent);

        // An intent and click handler to launch the app when the widget title bar is tapped
        Intent titleIntent = new Intent(context, TodoListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        views.setOnClickPendingIntent(R.id.tv_widget_header, pendingIntent);

        // An intent and click handler to launch AddOrEditTaskActivity with the task loaded for
        // editing, and TodoListActivity as its backstack
        // Many thanks to
        // https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
        // for this and other techniques for using a ListView in an app widget.
        Intent clickIntentTemplate = new Intent(context, AddOrEditTaskActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.lv_widget, clickPendingIntentTemplate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // called when there's a database or sort order change, to update the widget accordingly
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName todoListWidget = new ComponentName(context.getPackageName(), TodoListWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(todoListWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);
        super.onReceive(context, intent);
    }

}