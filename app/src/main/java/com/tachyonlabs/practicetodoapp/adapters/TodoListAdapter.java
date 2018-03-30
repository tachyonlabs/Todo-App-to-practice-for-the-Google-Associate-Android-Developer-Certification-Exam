package com.tachyonlabs.practicetodoapp.adapters;

import com.tachyonlabs.practicetodoapp.R;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListAdapterViewHolder> {
    private final String TAG = TodoListAdapter.class.getSimpleName();
    private final TodoListAdapterOnClickHandler mClickHandler;
    private String[] mTodos;

    public TodoListAdapter(TodoListAdapterOnClickHandler todoListAdapterOnClickHandler) {
        mClickHandler = todoListAdapterOnClickHandler;
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
        String todoListItem = mTodos[position];
        holder.cbTodoListItem.setText(todoListItem);
        holder.tvDueDate.setText(R.string.no_due_date);
        holder.tvPriority.setText(R.string.high_priority);
    }

    public void setTodoListData(String[] todos) {
        mTodos = todos;
        notifyDataSetChanged();
    }

    public interface TodoListAdapterOnClickHandler {
        void onClick(String string);
    }

    public class TodoListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final CheckBox cbTodoListItem;
        final TextView tvDueDate;
        final TextView tvPriority;

        public TodoListAdapterViewHolder(View itemView) {
            super(itemView);
            cbTodoListItem = itemView.findViewById(R.id.cb_todo_list_item);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);
            tvPriority = itemView.findViewById(R.id.tv_priority);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String todo = mTodos[getAdapterPosition()];
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
