package pl.kpchl.registrationproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.fragments.dialogfragments.TaskInfoDialog;
import pl.kpchl.registrationproject.models.TaskClass;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {
    private Context mContext;
    private List<TaskClass> taskList;
    private List<String> taskId;
    private ArrayList<String> sendToDialog = new ArrayList<>();
    private String groupNameFromBase;
    private DatabaseReference mDatabase;

    public TaskListAdapter(Context mContext, List<TaskClass> taskList, List<String> taskId) {
        this.mContext = mContext;
        this.taskList = taskList;
        this.taskId = taskId;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_task_view, viewGroup, false);
        setupDatabase();
        return new TaskListAdapter.TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskListViewHolder taskListViewHolder, final int i) {
        final TaskClass object = taskList.get(i);
        taskListViewHolder.taskName.setText(object.getTaskName());
        taskListViewHolder.taskStart.setText(object.getTaskDateStart());
        taskListViewHolder.taskEnd.setText(object.getTaskDateEnd());
        mDatabase.child("teams").child(object.getTaskGroupId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String group = dataSnapshot.child("groupName").getValue(String.class);
                        taskListViewHolder.groupName.setText(group);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if (i % 2 != 0) {
            taskListViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            taskListViewHolder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
        taskListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToDialog.clear();
                sendToDialog.add(0, object.getTaskName());
                sendToDialog.add(1, object.getTaskDescription());
                sendToDialog.add(2, object.getTaskDateStart());
                sendToDialog.add(3, object.getTaskDateEnd());
                sendToDialog.add(4, taskListViewHolder.groupName.getText().toString());
                showDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskListViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private TextView taskStart;
        private TextView taskEnd;
        private TextView groupName;

        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskStart = itemView.findViewById(R.id.taskDateStart);
            taskEnd = itemView.findViewById(R.id.taskDateEnd);
            groupName = itemView.findViewById(R.id.groupName);

        }
    }

    public void removeItem(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public void showDialog() {

        TaskInfoDialog taskInfoDialog = new TaskInfoDialog();
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putStringArrayList("taskObject", sendToDialog);
        taskInfoDialog.setArguments(args);
        taskInfoDialog.show(fragmentManager, "Dialog");

    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
