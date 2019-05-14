package pl.kpchl.registrationproject.fragments.projectfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.TaskListAdapter;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.fragments.dialogfragments.NewTaskDialog;
import pl.kpchl.registrationproject.models.TaskClass;
import pl.kpchl.registrationproject.underactivities.ProjectManagmentActivity;

public class ProjectTasksFragment extends BaseFragment implements View.OnClickListener {
    private FloatingActionButton fabButton;
    private RecyclerView taskRecyclerView;
    private DatabaseReference mDatabase;
    private TaskListAdapter taskListAdapter;
    private ArrayList<TaskClass> tasks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_tasks_fragment, container, false);
        components(view);
        setupDatabase();
        getDataFromBase();
        //setupRecyclerView();
        return view;
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void setupRecyclerView() {
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskListAdapter = new TaskListAdapter(getActivity(), tasks);
        taskRecyclerView.setAdapter(taskListAdapter);
        taskListAdapter.notifyDataSetChanged();
    }

    private void getDataFromBase() {
        //
        mDatabase.child("projects").child(ProjectManagmentActivity.projectId).child("tasks")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        TaskClass task = dataSnapshot.getValue(TaskClass.class);
                        tasks.add(task);
                        setupRecyclerView();
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

    private void components(View v) {
        fabButton = v.findViewById(R.id.fabButton);
        fabButton.setOnClickListener(this);
        taskRecyclerView = v.findViewById(R.id.taskRecyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabButton:
                showDialog();
                break;
        }
    }

    public void showDialog() {

        NewTaskDialog newTaskDialog = new NewTaskDialog();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        newTaskDialog.show(fragmentManager, "Dialog");

    }
}
