package com.example.todolist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class toDoAdapter extends RecyclerView.Adapter<toDoAdapter.ViewHolder>{
        private List<TodoModel> todoList;
        private MainActivity activity;
        private DatabaseHandler db;

        public toDoAdapter(DatabaseHandler db, MainActivity activity){
            this.activity = activity;
            this.db = db;
        }

        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
            return new ViewHolder(itemView);
        }

        public Context getContext(){
            return activity;
        }

        public void deleteItem(int position){
            TodoModel item = todoList.get(position);
            db.deleteTask(item.getId());
            todoList.remove(position);
            notifyItemRemoved(position);
        }
        public void editItem(int position){
            TodoModel item = todoList.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            bundle.putString("task", item.getTask());
            AddNewTask fragment = new AddNewTask();
            fragment.setArguments(bundle);
            fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            CheckBox task;
            public ViewHolder(@NonNull View view){
                super(view);
                task = view.findViewById(R.id.todoCheckBox);
            }
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position){
            db.openDatabase();
            TodoModel item = todoList.get(position);
            holder.task.setText(item.getTask());
            holder.task.setChecked(toBoolean(item.getStatus()));
            holder.task.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    db.updateStatus(item.getId(), 1);
                }else{
                    db.updateStatus(item.getId(), 0);
                }
            });
        }

        private boolean toBoolean(int n){
            return n != 0;
        }

        public void setTask(List<TodoModel> todoList){
            this.todoList = todoList;
            notifyDataSetChanged();
    }

        public int getItemCount(){
            return todoList.size();
        }


}
