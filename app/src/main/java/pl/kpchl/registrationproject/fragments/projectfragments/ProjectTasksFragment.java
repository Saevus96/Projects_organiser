package pl.kpchl.registrationproject.fragments.projectfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.SwipeToDeleteCallback;
import pl.kpchl.registrationproject.adapters.SwipeToDeleteCallbackRight;
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
    private List<TaskClass> tasks = new ArrayList<>();
    private List<String> tasksId = new ArrayList<>();
    private int groupSize = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_tasks_fragment, container, false);
        components(view);
        setupDatabase();
        getDataFromBase();
        swipeDelete();
        checkGroups();
        return view;
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void setupRecyclerView() {
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskListAdapter = new TaskListAdapter(getActivity(), tasks, tasksId);
        taskRecyclerView.setAdapter(taskListAdapter);
        taskListAdapter.notifyDataSetChanged();
    }

    private void swipeDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                taskListAdapter.removeItem(position);
                taskListAdapter.notifyDataSetChanged();
                mDatabase.child("projects").child(ProjectManagmentActivity.projectId)
                        .child("tasks").child(tasksId.get(position)).removeValue();
                tasksId.remove(position);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(taskRecyclerView);

        SwipeToDeleteCallbackRight swipeToDeleteCallbackRight = new SwipeToDeleteCallbackRight(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                taskListAdapter.removeItem(position);
                taskListAdapter.notifyDataSetChanged();
                mDatabase.child("projects").child(ProjectManagmentActivity.projectId)
                        .child("tasks").child(tasksId.get(position)).removeValue();
                tasksId.remove(position);
            }
        };
        ItemTouchHelper itemTouchhelper2 = new ItemTouchHelper(swipeToDeleteCallbackRight);
        itemTouchhelper2.attachToRecyclerView(taskRecyclerView);
    }

    private void checkGroups() {
        mDatabase.child("projects").child(ProjectManagmentActivity.projectId)
                .child("projectGroups").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                groupSize++;
               // Toast.makeText(getActivity(), groupSize, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                groupSize =0;
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataFromBase() {

        mDatabase.child("projects").child(ProjectManagmentActivity.projectId).child("tasks")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        TaskClass task = dataSnapshot.getValue(TaskClass.class);
                        String taskId = dataSnapshot.getKey();
                        tasksId.add(taskId);
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
                if (groupSize > 0) {
                    showDialog();
                } else {
                    createToast("Firstly you need add group to project",R.drawable.ic_alert);
                }

                break;
        }
    }

    public void showDialog() {

        NewTaskDialog newTaskDialog = new NewTaskDialog();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        newTaskDialog.show(fragmentManager, "Dialog");
    }

    //create custom toast
    public void createToast(String text, int image) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);

        ImageView toastImage = layout.findViewById(R.id.toastImage);
        toastImage.setImageResource(image);

        TextView toastText = layout.findViewById(R.id.toastText);
        toastText.setText(text);

        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
