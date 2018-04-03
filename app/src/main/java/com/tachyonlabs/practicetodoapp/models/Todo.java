package com.tachyonlabs.practicetodoapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Todo implements Parcelable {
    public static final Parcelable.Creator<Todo> CREATOR
            = new Parcelable.Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };
    private String description;
    private int priority;
    private int dueDate;
    private int id;

    public Todo(String description, int priority, int dueDate, int id) {
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.id = id;
    }

    private Todo(Parcel in) {
        description = in.readString();
        priority = in.readInt();
        dueDate = in.readInt();
        id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(description);
        parcel.writeInt(priority);
        parcel.writeInt(dueDate);
        parcel.writeInt(id);
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

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
