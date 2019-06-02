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

import pl.kpchl.registrationproject.MainActivity;
import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.useractivities.CheckYourApplicationsActivity;
import pl.kpchl.registrationproject.useractivities.CheckYourDetailsActivity;
import pl.kpchl.registrationproject.useractivities.CheckYourGroupsActivity;
import pl.kpchl.registrationproject.useractivities.FillDetailsActivity;

public class MainMenu2Fragment extends BaseFragment implements View.OnClickListener {
    private CardView checkYourGroups;
    private CardView checkYourApplications;
    private CardView fillYourDetails;
    private CardView signOutButton;
    private CardView checkYourDetails;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String currentUser;
    public int userDetailsCounter = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu2, container, false);
        setupDatabase();
        components(view);
        checkFilledDetails();
        activeButtons();
        animations();

        return view;
    }

    //make button Gray on start fragment
    public void activeButtons(){
        checkYourDetails.setCardBackgroundColor(Color.GRAY);

    }
    //check if user details are filled
    private int checkFilledDetails() {
        mDatabase.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals(getUser())){
                    checkYourDetails.setCardBackgroundColor(Color.WHITE);
                    fillYourDetails.setCardBackgroundColor(Color.GRAY);
                    userDetailsCounter++;
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
        return userDetailsCounter;
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //setup animations
    private void animations() {
        Animation animFromRight = AnimationUtils.loadAnimation(getContext(), R.anim.right_to_center);
        Animation animFromLeft = AnimationUtils.loadAnimation(getContext(), R.anim.left_to_center);
        checkYourDetails.startAnimation(animFromRight);
        checkYourGroups.startAnimation(animFromLeft);
        checkYourApplications.startAnimation(animFromRight);
        fillYourDetails.startAnimation(animFromLeft);
        signOutButton.startAnimation(animFromLeft);
    }

    //setup components
    private void components(View v) {
        checkYourGroups = v.findViewById(R.id.checkYourGroups);
        checkYourApplications = v.findViewById(R.id.checkYourApplications);
        fillYourDetails = v.findViewById(R.id.fillInYourDetails);
        signOutButton = v.findViewById(R.id.signOutButton);
        checkYourDetails = v.findViewById(R.id.checkYourDetails);
        checkYourDetails.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        fillYourDetails.setOnClickListener(this);
        checkYourApplications.setOnClickListener(this);
        checkYourGroups.setOnClickListener(this);
    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    private void signOut() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    // button listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkYourGroups:
                startActivity(new Intent(getActivity(), CheckYourGroupsActivity.class));
                getActivity().recreate();
                break;
            case R.id.checkYourApplications:
                startActivity(new Intent(getActivity(), CheckYourApplicationsActivity.class));
                getActivity().recreate();
                break;
            case R.id.fillInYourDetails:
                if (userDetailsCounter < 1) {
                    startActivity(new Intent(getActivity(), FillDetailsActivity.class));
                    getActivity().recreate();
                } else {
                    createToast("You already filled your details", R.drawable.ic_alert);
                }
                break;
            case R.id.checkYourDetails:
                if (userDetailsCounter > 0) {
                    startActivity(new Intent(getActivity(), CheckYourDetailsActivity.class));
                    getActivity().recreate();
                } else {
                    createToast("You need to fill your details to see and edit your Details", R.drawable.ic_alert);
                }
                break;
            case R.id.signOutButton:
                signOut();
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
