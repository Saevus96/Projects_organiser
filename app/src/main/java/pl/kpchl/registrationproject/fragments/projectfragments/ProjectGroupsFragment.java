package pl.kpchl.registrationproject.fragments.projectfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.GroupListAdapter;
import pl.kpchl.registrationproject.adapters.SwipeToDeleteCallback;
import pl.kpchl.registrationproject.adapters.SwipeToDeleteCallbackRight;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.fragments.dialogfragments.AddGroupDialog;
import pl.kpchl.registrationproject.models.GroupClass;
import pl.kpchl.registrationproject.underactivities.ProjectManagmentActivity;


public class ProjectGroupsFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView projectGroupsRecyclerView;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference mDatabase;
    private ArrayList<String> sendToDialog = new ArrayList<>();
    private ArrayList<String> groupsId = new ArrayList<>();
    public int groupCounter = 0;
    private ArrayList<GroupClass> groups = new ArrayList<>();
    private GroupListAdapter groupListAdapter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.project_groups_fragment, container, false);
        components(view);
        setAdapter();
        setupDatabase();
        readGroupsFromDataBase();
        //addToRecyclerView();
        swipeDelete();

        return view;
    }

    private void swipeDelete() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                groupListAdapter.removeItem(position);
                mDatabase.child("projects")
                        .child(ProjectManagmentActivity.projectId)
                        .child("projectGroups")
                        .child(groupsId.get(position)).removeValue();
                groupListAdapter.notifyDataSetChanged();
                groupsId.remove(position);

            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(projectGroupsRecyclerView);

        SwipeToDeleteCallbackRight swipeToDeleteCallbackRight = new SwipeToDeleteCallbackRight(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                groupListAdapter.removeItem(position);
                mDatabase.child("projects")
                        .child(ProjectManagmentActivity.projectId)
                        .child("projectGroups")
                        .child(groupsId.get(position)).removeValue();
                groupListAdapter.notifyDataSetChanged();
                groupsId.remove(position);

            }
        };
        ItemTouchHelper itemTouchhelper2 = new ItemTouchHelper(swipeToDeleteCallbackRight);
        itemTouchhelper2.attachToRecyclerView(projectGroupsRecyclerView);
    }

    private void addToRecyclerView(final String groupId) {
        mDatabase.child("teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(groupId)) {
                    GroupClass group = dataSnapshot.getValue(GroupClass.class);
                    groups.add(group);
                    setAdapter();
                    projectGroupsRecyclerView.setAdapter(groupListAdapter);
                    groupListAdapter.notifyDataSetChanged();
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

    private void setAdapter() {
        setupRecyclerView();
        groupListAdapter = new GroupListAdapter(getContext(), groups,groupsId, 1);

    }

    private void setupRecyclerView() {
        projectGroupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void readGroupsFromDataBase() {
        mDatabase.child("projects")
                .child(ProjectManagmentActivity.projectId)
                .child("projectGroups")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String groupId = dataSnapshot.getKey();
                        groupsId.add(groupId);
                        projectGroupsRecyclerView.setAdapter(groupListAdapter);

                        //Toast.makeText(getActivity(), dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                        addToRecyclerView(groupId);
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
        floatingActionButton = v.findViewById(R.id.fabButton);
        floatingActionButton.setOnClickListener(this);
        projectGroupsRecyclerView = v.findViewById(R.id.recyclerViewProjectGroups);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabButton:
                addProjectGroup();
                break;
        }
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void addProjectGroup() {
        AddGroupDialog addGroupDialog = new AddGroupDialog();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putStringArrayList("taskObject", sendToDialog);
        addGroupDialog.setArguments(args);
        addGroupDialog.show(fragmentManager, "Dialog");

    }


}
