package pl.kpchl.registrationproject.fragments.groupfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.ProjectListAdapter;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.models.ProjectClass;
import pl.kpchl.registrationproject.models.UserClass;
import pl.kpchl.registrationproject.underactivities.YourGroupManagmentActivity;

public class YourGroupProjectsFragment extends BaseFragment {
    private String groupId;
    private DatabaseReference mDatabase;
    private RecyclerView groupProjectsRecyclerView;
    private ProjectListAdapter projectListAdapter;
    private ArrayList<ProjectClass> projectsList = new ArrayList<>();
    private ArrayList<String> projectIdList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_projects_fragment, container, false);
        mDatabase = YourGroupManagmentActivity.mDatabase;
        groupId = YourGroupManagmentActivity.groupId;
        components(view);
        readProjects();
        return view;
    }

    private void components(View v) {
        groupProjectsRecyclerView = v.findViewById(R.id.recyclerViewGroupProjects);

    }


    private void readProjects() {
        mDatabase.child("projects")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String projectId = dataSnapshot.getKey();
                        readGroupsProject(projectId);
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

    private void readGroupsProject(final String projectId) {
        mDatabase.child("projects")
                .child(projectId)
                .child("projectGroups").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(groupId)) {
                    saveGroupsProject(projectId);
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

    private void saveGroupsProject(final String projectId) {
        mDatabase.child("projects")
                .child(projectId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProjectClass projectClass = dataSnapshot.getValue(ProjectClass.class);
                        Log.d("projectt", "id:  " + projectId + "  name :" + projectClass.getProjectName());
                        projectIdList.add(projectId);
                        projectsList.add(projectClass);
                        setAdapter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setAdapter() {
        setupRecyclerView();
        projectListAdapter = new ProjectListAdapter(getContext(), projectsList, projectIdList, 3);
        groupProjectsRecyclerView.setAdapter(projectListAdapter);
        projectListAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        groupProjectsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
