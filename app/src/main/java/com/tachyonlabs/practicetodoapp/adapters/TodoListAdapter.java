package com.tachyonlabs.practicetodoapp.adapters;

import com.tachyonlabs.practicetodoapp.R;
import com.tachyonlabs.practicetodoapp.models.Todo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListAdapterViewHolder> {
    private final String TAG = TodoListAdapter.class.getSimpleName();
    private final TodoListAdapterOnClickHandler mClickHandler;
    private Todo[] mTodos;
    private Context mContext;
    private Drawable[] priorityStars;

    public TodoListAdapter(Context context, TodoListAdapterOnClickHandler todoListAdapterOnClickHandler) {
        mClickHandler = todoListAdapterOnClickHandler;
        mContext = context;
        Resources res = context.getResources();
        // not really the place for this, but I had too much trouble trying to read them from @arrays
        priorityStars = new Drawable[] {res.getDrawable(R.drawable.ic_star_red_24dp), res.getDrawable(R.drawable.ic_star_orange_24dp), res.getDrawable(R.drawable.ic_star_yellow_24dp)};
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
        Todo todoListItem = mTodos[position];
        holder.cbTodoDescription.setText(todoListItem.getDescription());
        // TODO put this date stuff as a function somewhere else
        String dueDateString = "";
        int dueDate = todoListItem.getDate();
        if (dueDate == 0) {
            dueDateString = mContext.getString(R.string.no_due_date);
        }
        holder.tvTodoDueDate.setText(dueDateString);
        holder.tvTodoPriority.setText(mContext.getResources().getStringArray(R.array.priorities)[todoListItem.getPriority()]);
        holder.ivTodoPriorityStar.setBackground(priorityStars[todoListItem.getPriority()]);
    }

    public void setTodoListData(Todo[] todos) {
        mTodos = todos;
        notifyDataSetChanged();
    }

    public interface TodoListAdapterOnClickHandler {
        void onClick(Todo todo);
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
        }

        @Override
        public void onClick(View view) {
            Todo todo = mTodos[getAdapterPosition()];
            mClickHandler.onClick(todo);
        }
    }

    @Override
    public int getItemCount() {
        if (mTodos == null) {
            return 0;
        } else {
            return mTodos.length;
        }
    }
}
