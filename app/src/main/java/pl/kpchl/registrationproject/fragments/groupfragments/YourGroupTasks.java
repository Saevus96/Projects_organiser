package pl.kpchl.registrationproject.fragments.groupfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.TaskListAdapter;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.models.TaskClass;
import pl.kpchl.registrationproject.underactivities.YourGroupManagmentActivity;

public class YourGroupTasks extends BaseFragment {
    private DatabaseReference mDatabase;
    private String groupId;
    private RecyclerView taskRecyclerView;
    private TaskListAdapter taskListAdapter;
    private ArrayList<TaskClass> tasks = new ArrayList<>();
    private ArrayList<String> tasksId = new ArrayList<>();
    private ArrayList<String> projectIdList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_group_tasks, container, false);
        mDatabase = YourGroupManagmentActivity.mDatabase;
        groupId = YourGroupManagmentActivity.groupId;
        readProjects();
        components(view);
        return view;
    }

    private void components(View v) {
        taskRecyclerView = v.findViewById(R.id.taskRecyclerView);
    }

    private void readProjects() {
        mDatabase.child("projects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String projectId = dataSnapshot.getKey();
                readTasks(projectId);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readTasks(final String projectId) {
        mDatabase.child("projects").child(projectId).child("tasks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("taskGroupId").getValue(String.class).equals(groupId)) {
                    TaskClass taskClass = dataSnapshot.getValue(TaskClass.class);
                    String taskId = dataSnapshot.getKey();
                    tasks.add(taskClass);
                    tasksId.add(taskId);
                    projectIdList.add(projectId);
                    setupRecyclerView();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupRecyclerView() {
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskListAdapter = new TaskListAdapter(getActivity(), tasks, tasksId, projectIdList, 2);
        taskRecyclerView.setAdapter(taskListAdapter);
        taskListAdapter.notifyDataSetChanged();
    }

}
