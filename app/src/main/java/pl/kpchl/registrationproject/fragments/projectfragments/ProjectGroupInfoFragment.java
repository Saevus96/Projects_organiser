package pl.kpchl.registrationproject.fragments.projectfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.GroupListAdapter;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.fragments.dialogfragments.JoinToGroupDialog;
import pl.kpchl.registrationproject.models.GroupClass;
import pl.kpchl.registrationproject.useractivities.ProjectInformations;

public class ProjectGroupInfoFragment extends BaseFragment {
    private RecyclerView projectGroupsRecyclerView;
    private DatabaseReference mDatabase;
    private ArrayList<String> sendToDialog = new ArrayList<>();
    private ArrayList<String> groupsId = new ArrayList<>();
    private ArrayList<GroupClass> groups = new ArrayList<>();
    private GroupListAdapter groupListAdapter;
    public static View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_info_fragment, container, false);
        components(view);
        setAdapter();
        setupDatabase();
        readGroupsFromDataBase();
        return view;
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
        groupListAdapter = new GroupListAdapter(getContext(), groups, groupsId, 2);


    }

    private void setupRecyclerView() {
        projectGroupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void readGroupsFromDataBase() {
        mDatabase.child("projects")
                .child(ProjectInformations.projectId)
                .child("projectGroups")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String groupId = dataSnapshot.getKey();
                        groupsId.add(groupId);
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
        projectGroupsRecyclerView = v.findViewById(R.id.recyclerViewProjectGroups);
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


}
