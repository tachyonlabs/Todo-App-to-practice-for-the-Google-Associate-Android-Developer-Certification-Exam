package com.tachyonlabs.practicetodoapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TodoListWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoListWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
