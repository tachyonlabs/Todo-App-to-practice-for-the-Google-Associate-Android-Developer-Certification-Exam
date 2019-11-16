package com.tachyonlabs.practicetodoapp.adapters;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.custom_views.PriorityStarImageView;
import com.tachyonlabs.practicetodoapp.data.TodoListContract;
import com.tachyonlabs.practicetodoapp.databinding.ItemTodoListBinding;
import com.tachyonlabs.practicetodoapp.models.TodoTask;
import com.tachyonlabs.practicetodoapp.utils.TodoDateUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListAdapterViewHolder> {
    private final static String TAG = TodoListAdapter.class.getSimpleName();
    private final TodoListAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private Resources mRes;
    private Cursor mCursor;
    private int mDescriptionIndex;
    private int mPriorityIndex;
    private int mDueDateIndex;
    private int m_IDIndex;
    private int mCompletedIndex;
    private ColorStateList completedCheckboxColors;
    private ColorStateList unCompletedCheckboxColors;

    public TodoListAdapter(Context context, TodoListAdapterOnClickHandler todoListAdapterOnClickHandler) {
        mClickHandler = todoListAdapterOnClickHandler;
        mContext = context;
        mRes = context.getResources();

        completedCheckboxColors = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked},
                },
                new int[]{
                        Color.DKGRAY,
                        mRes.getColor(R.color.colorCompleted),
                });

        unCompletedCheckboxColors = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked},
                },
                new int[]{
                        Color.DKGRAY,
                        mRes.getColor(R.color.colorAccent),
                });
    }

    @Override
    public TodoListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        ItemTodoListBinding binding = ItemTodoListBinding.inflate(inflater,viewGroup,shouldAttachToParentImmediately);

        TodoListAdapterViewHolder viewHolder = new TodoListAdapterViewHolder(binding);

        return viewHolder;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoListAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bind(mCursor);
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
            mCompletedIndex = mCursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_COMPLETED);
        }
        notifyDataSetChanged();
    }

    public interface TodoListAdapterOnClickHandler {
        void onClick(TodoTask todoTask, View view);
    }

    public class TodoListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ConstraintLayout clTodoListItem;
        ItemTodoListBinding binding;

        public TodoListAdapterViewHolder(ItemTodoListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            clTodoListItem = (ConstraintLayout) itemView;
            itemView.setOnClickListener(this);
            binding.cbTodoDescription.setOnClickListener(this);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            TodoTask todoTask = new TodoTask(mCursor.getString(mDescriptionIndex),
                    mCursor.getInt(mPriorityIndex),
                    mCursor.getLong(mDueDateIndex),
                    mCursor.getInt(m_IDIndex),
                    mCursor.getInt(mCompletedIndex));
            mClickHandler.onClick(todoTask, view);
        }
        @SuppressLint("RestrictedApi")
        public void bind(Cursor cursor){
            binding.cbTodoDescription.setText(cursor.getString(mDescriptionIndex));
            binding.tvTodoDueDate.setTextColor(binding.tvTodoPriority.getCurrentTextColor());

            String dueDateString;
            long dueDate = cursor.getLong(mDueDateIndex);
            Log.d(TAG, cursor.getString(mDescriptionIndex) + " " + dueDate);
            if (dueDate == TodoTask.NO_DUE_DATE) {
                dueDateString = mContext.getString(R.string.no_due_date);
            } else {
                dueDateString = TodoDateUtils.formatDueDate(mContext, dueDate);
            }

            int priority = cursor.getInt(mPriorityIndex);
            binding.tvTodoDueDate.setText(dueDateString);
            binding.tvTodoPriority.setText(mRes.getStringArray(R.array.priorities)[priority]);
            int isCompleted = cursor.getInt(mCompletedIndex);
            binding.cbTodoDescription.setChecked(isCompleted == TodoTask.TASK_COMPLETED);

            if (isCompleted == TodoTask.TASK_COMPLETED) {
                // if the task is completed, we want everything grey
                clTodoListItem.setBackground(mRes.getDrawable(R.drawable.list_item_completed_touch_selector));
                binding.cbTodoDescription.setTextColor(mRes.getColor(R.color.colorCompleted));
                binding.cbTodoDescription.setSupportButtonTintList(completedCheckboxColors);
                binding.tvTodoPriority.setText(mRes.getString(R.string.completed));
                priority = PriorityStarImageView.COMPLETED;
            } else {
                clTodoListItem.setBackground(mRes.getDrawable(R.drawable.list_item_touch_selector));
                binding.cbTodoDescription.setTextColor(mRes.getColor(R.color.colorPrimaryDark));
                binding.cbTodoDescription.setSupportButtonTintList(unCompletedCheckboxColors);
                binding.tvTodoPriority.setText(mRes.getStringArray(R.array.priorities)[priority]);
                if (dueDate < TodoDateUtils.getTodaysDateInMillis()) {
                    // display overdue tasks with the date in red
                    // yeah, I know red for both overdue and high priority may be not the best idea
                    binding.tvTodoDueDate.setTextColor(mRes.getColor(R.color.colorOverdue));
                } else {
                    binding.tvTodoDueDate.setTextColor(binding.tvTodoPriority.getCurrentTextColor());
                    Log.d(TAG, "color is " + (binding.tvTodoPriority.getCurrentTextColor()));
                }
            }
            binding.ivTodoPriorityStar.setPriority(priority);
        }
    }
}
