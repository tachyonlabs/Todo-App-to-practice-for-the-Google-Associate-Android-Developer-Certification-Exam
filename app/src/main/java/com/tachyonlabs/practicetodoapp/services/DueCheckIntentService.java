package com.tachyonlabs.practicetodoapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class DueCheckIntentService extends IntentService {
    private static final String TAG = DueCheckIntentService.class.getSimpleName();
    public DueCheckIntentService() {
        super("DueCheckIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service running");
    }
}
