package pl.kpchl.registrationproject.fragments.projectfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.fragments.dialogfragments.EditDetailsDialog;
import pl.kpchl.registrationproject.models.ProjectClass;
import pl.kpchl.registrationproject.models.UserClass;
import pl.kpchl.registrationproject.underactivities.ProjectManagmentActivity;

public class ProjectInfoFragment extends BaseFragment implements View.OnClickListener {
    private TextView projectName, projectCategory, projectCustomers, projectDescription, projectOrganisation;
    private TextView adminName, adminLastName, adminPhone, adminEmail;
    private ImageView adminImage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    ProjectClass projectClass;
    private int counter = 0;
    private String adminId;
    private ArrayList<String> sendToDialog = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_info_fragment, container, false);
        components(view);
        setupDatabase();
        firebaseStorageSetup();
        fillProjectInfo();


        return view;
    }

    private void findCategories() {
        mDatabase.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.getKey().toString().equals(projectCategory.getText().toString())) {
                    projectCategory.setText(dataSnapshot.getValue(String.class));
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

    private void informationBeautify() {

        mDatabase.child("users").child(projectClass.getProjectAdmin()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);
                adminName.setText(userClass.getFirstName());
                adminLastName.setText(userClass.getLastName());
                adminPhone.setText(userClass.getPhone());
                adminEmail.setText(userClass.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Glide.with(this)
                .load(storageReference.child("user_images").child(projectClass.getProjectAdmin()))
                .override(120, 120)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(adminImage);

    }

    //setup firebase storage
    private void firebaseStorageSetup() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void fillProjectInfo() {

        mDatabase.child("projects").child(ProjectManagmentActivity.projectId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        projectClass = dataSnapshot.getValue(ProjectClass.class);
                        projectName.setText(projectClass.getProjectName());
                        projectCategory.setText(projectClass.getProjectCategory());
                        projectDescription.setText(projectClass.getProjectDescription());
                        projectCustomers.setText(projectClass.getProjectCustomers());
                        projectOrganisation.setText(projectClass.getProjectOrganisation());
                        adminId = projectClass.getProjectAdmin();
                        findCategories();
                        if (counter < 1) {
                            informationBeautify();
                        }
                        counter++;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //setup components
    private void components(View v) {
        projectName = v.findViewById(R.id.projectName);
        projectCategory = v.findViewById(R.id.projectCategory);
        projectDescription = v.findViewById(R.id.projectDescription);
        projectCustomers = v.findViewById(R.id.projectCustomers);
        projectOrganisation = v.findViewById(R.id.projectOrganisation);
        adminName = v.findViewById(R.id.adminName);
        adminLastName = v.findViewById(R.id.adminLastName);
        adminPhone = v.findViewById(R.id.adminPhone);
        adminEmail = v.findViewById(R.id.adminEmail);
        adminImage = v.findViewById(R.id.adminImage);

        projectName.setOnClickListener(this);
        projectCategory.setOnClickListener(this);
        projectDescription.setOnClickListener(this);
        projectCustomers.setOnClickListener(this);
        projectOrganisation.setOnClickListener(this);

    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.projectName:
                sendToDialog.add(0, "projectName");
                sendToDialog.add(1, projectName.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.projectCategory:
                sendToDialog.add(0, "projectCategory");
                showDialog(sendToDialog);
                break;
            case R.id.projectDescription:
                sendToDialog.add(0, "projectDescription");
                sendToDialog.add(1, projectDescription.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.projectCustomers:
                sendToDialog.add(0, "projectCustomers");
                sendToDialog.add(1, projectCustomers.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.projectOrganisation:
                sendToDialog.add(0, "projectOrganisation");
                sendToDialog.add(1, projectOrganisation.getText().toString());
                showDialog(sendToDialog);
                break;
        }
    }

    public void showDialog(ArrayList<String> detailType) {

        EditDetailsDialog editDetailsDialog = new EditDetailsDialog();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle args = new Bundle();

        args.putStringArrayList("detailType", detailType);
        args.putString("dataType","projectInfo");


        editDetailsDialog.setArguments(args);
        editDetailsDialog.show(fragmentManager, "Dialog");

    }
}
