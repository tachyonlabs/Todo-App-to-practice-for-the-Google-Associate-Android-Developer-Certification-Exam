package com.tachyonlabs.practicetodoapp.utils;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

public class TodoDateUtils {
    private final static String TAG = TodoDateUtils.class.getSimpleName();

    public static long getTodaysDateInMillis() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        // zeroing out all the time info because I only want to compare due/overdue dates in terms
        // of which day it is right now
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long today = calendar.getTimeInMillis();
        Log.d(TAG, "millis = " + today);
        return today;
    }

    public static String formatDueDate(Context context, long dueDateInMillis) {
        String formattedDueDateString = android.text.format.DateUtils.formatDateTime(context, dueDateInMillis,
                android.text.format.DateUtils.FORMAT_SHOW_DATE |
                        android.text.format.DateUtils.FORMAT_ABBREV_MONTH |
                        android.text.format.DateUtils.FORMAT_SHOW_YEAR |
                        android.text.format.DateUtils.FORMAT_ABBREV_WEEKDAY |
                        android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY);
        return formattedDueDateString;
    }
}
