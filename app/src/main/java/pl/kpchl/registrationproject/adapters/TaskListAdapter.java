package pl.kpchl.registrationproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.models.TaskClass;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {
    private Context mContext;
    private ArrayList<TaskClass> taskList;

    public TaskListAdapter(Context mContext, ArrayList<TaskClass> taskList) {
        this.mContext = mContext;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_task_view, viewGroup, false);

        return new TaskListAdapter.TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder taskListViewHolder, int i) {
        TaskClass object = taskList.get(i);
        taskListViewHolder.taskName.setText(object.getTaskName());
        taskListViewHolder.taskStart.setText(object.getTaskDateStart());
        taskListViewHolder.taskEnd.setText(object.getTaskDateEnd());
        if(i%2 != 0){
            taskListViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{
            taskListViewHolder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskListViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private TextView taskStart;
        private TextView taskEnd;
        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskStart = itemView.findViewById(R.id.taskDateStart);
            taskEnd = itemView.findViewById(R.id.taskDateEnd);
        }
    }
}
