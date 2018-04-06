package com.tachyonlabs.practicetodoapp.adapters;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.data.TodoListContract;
import com.tachyonlabs.practicetodoapp.models.Todo;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListAdapterViewHolder> {
    private final String TAG = TodoListAdapter.class.getSimpleName();
    private final TodoListAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private Drawable[] priorityStars;
    private Cursor mCursor;
    private int mDescriptionIndex;
    private int mPriorityIndex;
    private int mDueDateIndex;
    private int m_IDIndex;

    public TodoListAdapter(Context context, TodoListAdapterOnClickHandler todoListAdapterOnClickHandler) {
        mClickHandler = todoListAdapterOnClickHandler;
        mContext = context;
        Resources res = context.getResources();
        // not really the place for this, but I had too much trouble trying to read them from @arrays
        priorityStars = new Drawable[]{res.getDrawable(R.drawable.ic_star_red_24dp), res.getDrawable(R.drawable.ic_star_orange_24dp), res.getDrawable(R.drawable.ic_star_yellow_24dp)};
    }

    @Override
    public TodoListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_todo_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TodoListAdapterViewHolder viewHolder = new TodoListAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoListAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.cbTodoDescription.setText(mCursor.getString(mDescriptionIndex));
        // so deleting an item doesn't propagate the checked checkbox when the view is recycled
        holder.cbTodoDescription.setChecked(false);

        String dueDateString;
        long dueDate = mCursor.getLong(mDueDateIndex);
        if (dueDate == Long.MAX_VALUE) {
            dueDateString = mContext.getString(R.string.no_due_date);
        } else {
            dueDateString = DateUtils.formatDateTime(mContext, dueDate,
                    DateUtils.FORMAT_SHOW_DATE |
                            DateUtils.FORMAT_ABBREV_MONTH |
                            DateUtils.FORMAT_SHOW_YEAR |
                            DateUtils.FORMAT_ABBREV_WEEKDAY |
                            DateUtils.FORMAT_SHOW_WEEKDAY);
        }
        int priority = mCursor.getInt(mPriorityIndex);
        holder.tvTodoDueDate.setText(dueDateString);
        holder.tvTodoPriority.setText(mContext.getResources().getStringArray(R.array.priorities)[priority]);
        holder.ivTodoPriorityStar.setBackground(priorityStars[priority]);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        if (mCursor != null) {
            mDescriptionIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_DESCRIPTION);
            mPriorityIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_PRIORITY);
            mDueDateIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_DUE_DATE);
            m_IDIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_ID);
        }
        notifyDataSetChanged();
    }

    public interface TodoListAdapterOnClickHandler {
        void onClick(Todo todo, View view);
    }

    public class TodoListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final CheckBox cbTodoDescription;
        final TextView tvTodoDueDate;
        final TextView tvTodoPriority;
        final ImageView ivTodoPriorityStar;

        public TodoListAdapterViewHolder(View itemView) {
            super(itemView);
            cbTodoDescription = itemView.findViewById(R.id.cb_todo_description);
            tvTodoDueDate = itemView.findViewById(R.id.tv_todo_due_date);
            tvTodoPriority = itemView.findViewById(R.id.tv_todo_priority);
            ivTodoPriorityStar = itemView.findViewById(R.id.iv_todo_priority_star);
            itemView.setOnClickListener(this);
            cbTodoDescription.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            Todo todo = new Todo(mCursor.getString(mDescriptionIndex),
                    mCursor.getInt(mPriorityIndex),
                    mCursor.getLong(mDueDateIndex),
                    mCursor.getInt(m_IDIndex));
            mClickHandler.onClick(todo, view);
        }
    }
}
