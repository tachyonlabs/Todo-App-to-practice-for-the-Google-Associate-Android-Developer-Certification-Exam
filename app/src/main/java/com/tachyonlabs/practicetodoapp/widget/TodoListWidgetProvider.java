package com.tachyonlabs.practicetodoapp.widget;

import com.tachyonlabs.practicetodoapp.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class TodoListWidgetProvider extends AppWidgetProvider {
    private final static String TAG = TodoListWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_list_widget);
        Intent intent = new Intent(context, TodoListWidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lv_widget, intent);

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