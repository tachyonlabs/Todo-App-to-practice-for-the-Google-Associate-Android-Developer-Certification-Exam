package com.tachyonlabs.practicetodoapp.custom_views;

import com.tachyonlabs.practicetodoapp.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class PriorityStarImageView extends android.support.v7.widget.AppCompatImageView {
    public final static int HIGH = 0;
    public final static int MEDIUM = 1;
    public final static int LOW = 2;
    public final static int COMPLETED = 3;

    public PriorityStarImageView(Context context) {
        super(context);
    }

    public PriorityStarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public PriorityStarImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);
    }

    private void style(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PriorityStarImageView);
        int priority = attributes.getInteger(R.styleable.PriorityStarImageView_priority, HIGH);
        setPriority(priority);
        attributes.recycle();
    }

    public void setPriority(int priority) {
        Resources res = getResources();
        int star = 0;
        int contentDescription = 0;
        switch (priority) {
            case HIGH:
                star = R.drawable.ic_star_red_24dp;
                contentDescription = R.string.high_priority_task_red_star;
                break;
            case MEDIUM:
                star = R.drawable.ic_star_orange_24dp;
                contentDescription = R.string.medium_priority_task_orange_star;
                break;
            case LOW:
                star = R.drawable.ic_star_yellow_24dp;
                contentDescription = R.string.low_priority_task_yellow_star;
                break;
            case COMPLETED:
                star = R.drawable.ic_star_grey_24dp;
                contentDescription = R.string.completed_task_grey_star;
        }

        setBackground(res.getDrawable(star));
        setContentDescription(res.getString(contentDescription));
    }
}
