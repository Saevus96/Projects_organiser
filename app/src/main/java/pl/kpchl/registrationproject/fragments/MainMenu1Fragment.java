package pl.kpchl.registrationproject.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.kpchl.registrationproject.ExampleProjectsActivity;
import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.models.ProjectClass;
import pl.kpchl.registrationproject.useractivities.CreateGroupActivity;
import pl.kpchl.registrationproject.useractivities.CreateProjectActivity;
import pl.kpchl.registrationproject.useractivities.ManageGroupActivity;
import pl.kpchl.registrationproject.useractivities.ManageProjectsActivity;

public class MainMenu1Fragment extends BaseFragment implements View.OnClickListener {
    private CardView createNewProject;
    private CardView manageYourProjects;
    private CardView searchProjects;
    private CardView createNewGroup;
    private CardView manageYourGroups;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;
    public int groupCounter = 0;
    public int projectCounter = 0;

    public static MainMenu1Fragment newInstance(){
        return new MainMenu1Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu1, container, false);
        setupDatabase();
        components(view);
        checkGroups();
        checkProjects();
        activeButtons();
        animations();

        return view;
    }

    private void checkProjects() {
        mDatabase.child("projects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("projectAdmin").getValue(String.class).equals(getUser())){
                    projectCounter++;
                    manageYourProjects.setCardBackgroundColor(Color.WHITE);
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

    //make button Gray on start fragment
    private void activeButtons(){
        manageYourGroups.setCardBackgroundColor(Color.GRAY);
        createNewProject.setCardBackgroundColor(Color.GRAY);
        manageYourProjects.setCardBackgroundColor(Color.GRAY);
    }

    //count groups if user is admin of group
    private void checkGroups(){
        mDatabase.child("teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("groupAdmin").getValue(String.class).equals(getUser())){
                    groupCounter = 0;
                    groupCounter++;
                    manageYourGroups.setCardBackgroundColor(Color.WHITE);
                    createNewProject.setCardBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("groupAdmin").getValue(String.class).equals(getUser())){
                    groupCounter = 0;
                    groupCounter++;
                    manageYourGroups.setCardBackgroundColor(Color.WHITE);
                    createNewProject.setCardBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("groupAdmin").getValue(String.class).equals(getUser())){
                    groupCounter = 0;
                    groupCounter++;
                    manageYourGroups.setCardBackgroundColor(Color.WHITE);
                    createNewProject.setCardBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //setup components
    private void components(View v) {
        createNewProject = v.findViewById(R.id.createNewProject);
        createNewProject.setOnClickListener(this);
        manageYourProjects = v.findViewById(R.id.manageYourProjects);
        manageYourProjects.setOnClickListener(this);
        searchProjects = v.findViewById(R.id.searchProjects);
        searchProjects.setOnClickListener(this);
        createNewGroup = v.findViewById(R.id.createNewGroup);
        createNewGroup.setOnClickListener(this);
        manageYourGroups = v.findViewById(R.id.manageYourGroups);
        manageYourGroups.setOnClickListener(this);
    }

    //set Animations
    private void animations() {
        Animation animFromRight = AnimationUtils.loadAnimation(getContext(), R.anim.right_to_center);
        Animation animFromLeft = AnimationUtils.loadAnimation(getContext(), R.anim.left_to_center);
        Animation animFromUp = AnimationUtils.loadAnimation(getContext(), R.anim.up_to_center);
        createNewProject.startAnimation(animFromLeft);
        manageYourProjects.startAnimation(animFromRight);
        searchProjects.startAnimation(animFromUp);
        createNewGroup.startAnimation(animFromLeft);
        manageYourGroups.startAnimation(animFromRight);

    }

    //listener for buttons
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createNewProject:
                if(groupCounter>0){
                    startActivity(new Intent(getActivity(), CreateProjectActivity.class));
                }else{
                    createToast("You don't have any active group. Create group first!",R.drawable.ic_alert);
                }

                break;
            case R.id.manageYourProjects:
                if(projectCounter>0){
                    startActivity(new Intent(getActivity(), ManageProjectsActivity.class));
                    getActivity().finish();
                }else{
                    createToast("You don't have any active project. Create project first!", R.drawable.ic_alert);
                }

                break;
            case R.id.searchProjects:
                startActivity(new Intent(getActivity(), ExampleProjectsActivity.class));
                break;
            case R.id.createNewGroup:
                startActivity(new Intent(getActivity(), CreateGroupActivity.class));
                break;
            case R.id.manageYourGroups:
                if(groupCounter>0){
                    startActivity(new Intent(getActivity(), ManageGroupActivity.class));
                    getActivity().finish();
                }else{
                    createToast("You don't have any active group. Create group first!",R.drawable.ic_alert);
                }
                break;

        }
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