package pl.kpchl.registrationproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
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
import pl.kpchl.registrationproject.fragments.dialogfragments.ProjectInfoDialog;
import pl.kpchl.registrationproject.models.ProjectClass;
import pl.kpchl.registrationproject.underactivities.ProjectManagmentActivity;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectListViewHolder> {

    private Context mContext;
    private ArrayList<ProjectClass> mProjectList;
    private ArrayList<String> sendToDialog = new ArrayList<>();
    private int mProjectPage;
    private DatabaseReference mDatabase;
    private ArrayList<String> mProjectId;

    public ProjectListAdapter(Context context, ArrayList<ProjectClass> projectList,ArrayList<String> projectId, int projectPage) {
        mContext = context;
        mProjectList = projectList;
        mProjectPage = projectPage;
        mProjectId = projectId;
    }


    public class ProjectListViewHolder extends RecyclerView.ViewHolder {
        public TextView projectName;
        public TextView projectOrganisation;
        public TextView projectCategory;
        private ImageView projectInfo;

        public ProjectListViewHolder(@NonNull View itemView) {
            super(itemView);

            projectName = itemView.findViewById(R.id.projectName);
            projectOrganisation = itemView.findViewById(R.id.projectOrganisation);
            projectCategory = itemView.findViewById(R.id.projectCategory);
            projectInfo = itemView.findViewById(R.id.informationImage);


        }
    }


    @NonNull
    @Override
    public ProjectListAdapter.ProjectListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_project_view, viewGroup, false);

        return new ProjectListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ProjectListAdapter.ProjectListViewHolder projectListViewHolder, final int i) {
        final ProjectClass object = mProjectList.get(i);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().toString().equals(object.getProjectCategory())) {
                    projectListViewHolder.projectCategory.setText(dataSnapshot.getValue(String.class));
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
        projectListViewHolder.projectName.setText(object.getProjectName());
        projectListViewHolder.projectOrganisation.setText(object.getProjectOrganisation());

        projectListViewHolder.projectCategory.setText(object.getProjectCategory());
        if (mProjectPage == 2) {
            projectListViewHolder.projectInfo.setVisibility(View.INVISIBLE);
            projectListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext,ProjectManagmentActivity.class);
                    intent.putExtra("projectId", mProjectId.get(i));
                    mContext.startActivity(intent);
                    //Toast.makeText(mContext, mProjectId.get(i), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (i % 2 != 0) {
            projectListViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            // projectListViewHolder.projectName.setTextColor();
        } else {
            projectListViewHolder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }



        projectListViewHolder.projectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendToDialog.add(0, object.getProjectName());
                sendToDialog.add(1, object.getProjectDescription());
                sendToDialog.add(2, object.getProjectOrganisation());
                sendToDialog.add(3, projectListViewHolder.projectCategory.getText().toString());
                showDialog();
            }
        });

    }

    public void showDialog() {

        ProjectInfoDialog projectInfoDialog = new ProjectInfoDialog();
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        Bundle args = new Bundle();

        args.putStringArrayList("projectObject", sendToDialog);
        projectInfoDialog.setArguments(args);
        projectInfoDialog.show(fragmentManager, "Dialog");

    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }
}