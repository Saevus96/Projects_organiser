package pl.kpchl.registrationproject.fragments.projectfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.models.ProjectClass;
import pl.kpchl.registrationproject.useractivities.ProjectInformations;

public class ProjectDetailsFragment extends BaseFragment {
    private TextView projectName;
    private TextView projectCategory;
    private TextView projectDescription;
    private TextView projectCustomers;
    private TextView projectOrganisation;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_details_fragment, container, false);
        components(view);
        setupDatabase();
        readDataFromDatabase();
        return view;
    }

    private void readDataFromDatabase() {
        mDatabase.child("projects").child(ProjectInformations.projectId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProjectClass project = dataSnapshot.getValue(ProjectClass.class);
                projectName.setText(project.getProjectName());
                projectCategory.setText(project.getProjectCategory());
                projectOrganisation.setText(project.getProjectOrganisation());
                projectCustomers.setText(project.getProjectCustomers());
                projectDescription.setText(project.getProjectDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void components(View v) {
        projectName = v.findViewById(R.id.projectName);
        projectCategory = v.findViewById(R.id.projectCategory);
        projectDescription = v.findViewById(R.id.projectDescription);
        projectCustomers = v.findViewById(R.id.projectCustomers);
        projectOrganisation = v.findViewById(R.id.projectOrganisation);
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
