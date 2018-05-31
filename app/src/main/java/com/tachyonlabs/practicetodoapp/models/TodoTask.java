package com.tachyonlabs.practicetodoapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TodoTask implements Parcelable {
    public final static int HIGH_PRIORITY = 0;
    public final static int MEDIUM_PRIORITY = 1;
    public final static int LOW_PRIORITY = 2;
    public final static int TASK_COMPLETED = 1;
    public final static int TASK_NOT_COMPLETED = 0;
    public final static long NO_DUE_DATE = Long.MAX_VALUE;

    public static final Parcelable.Creator<TodoTask> CREATOR
            = new Parcelable.Creator<TodoTask>() {
        @Override
        public TodoTask createFromParcel(Parcel in) {
            return new TodoTask(in);
        }

        @Override
        public TodoTask[] newArray(int size) {
            return new TodoTask[size];
        }
    };
    private String description;
    private int priority;
    private long dueDate;
    private int id;
    private int completed;

    public TodoTask() {
        this.description = null;
        this.priority = 0;
        this.dueDate = 0L;
        this.id = 0;
        this.completed = 0;
    }

    public TodoTask(String description, int priority, long dueDate, int id, int completed) {
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.id = id;
        this.completed = completed;
    }

    private TodoTask(Parcel in) {
        description = in.readString();
        priority = in.readInt();
        dueDate = in.readLong();
        id = in.readInt();
        completed = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(description);
        parcel.writeInt(priority);
        parcel.writeLong(dueDate);
        parcel.writeInt(id);
        parcel.writeInt(completed);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

}
