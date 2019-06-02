package pl.kpchl.registrationproject.fragments.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.fragments.projectfragments.ProjectGroupInfoFragment;
import pl.kpchl.registrationproject.models.GroupClass;
import pl.kpchl.registrationproject.models.RequestClass;
import pl.kpchl.registrationproject.models.UserClass;

public class JoinToGroupDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private String groupId;
    private Button sendButton, cancelButton;
    private TextView userName;
    private TextView userLastName;
    private TextView userEmail;
    private ImageView userImage;
    private TextView groupName;
    private TextView groupSpeciality;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;
    public UserClass user;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    public GroupClass group = new GroupClass();
    public int userGroups = 0;
    private ValueEventListener valueEventListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            groupId = bundle.getString("groupObject");
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.join_to_group, null);
        components(view);
        setupDatabase();
        firebaseStorageSetup();
        readUserDetailsFromDatabase();
        readGroupDetailsFromDatabase();
        //Toast.makeText(getActivity(), groupId, Toast.LENGTH_SHORT).show();
        builder.setView(view);
        return builder.create();
    }

    //setup firebase storage
    private void firebaseStorageSetup() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://projectregistration-a0906.appspot.com");
    }

    //setup components
    private void components(View v) {
        sendButton = v.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);
        cancelButton = v.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        userName = v.findViewById(R.id.userName);
        userLastName = v.findViewById(R.id.userLastName);
        userEmail = v.findViewById(R.id.userEmail);
        userImage = v.findViewById(R.id.userImage);
        groupName = v.findViewById(R.id.groupName);
        groupSpeciality = v.findViewById(R.id.groupSpeciality);
    }

    //setup database
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    private void readUserDetailsFromDatabase() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserClass.class);
                setUserDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.child("users").child(getUser()).addValueEventListener(valueEventListener);
    }

    private void readGroupDetailsFromDatabase() {
        mDatabase.child("teams")
                .child(groupId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        group = dataSnapshot.getValue(GroupClass.class);
                        setGroupDetails();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setUserDetails() {
        userName.setText(user.getFirstName());
        userLastName.setText(user.getLastName());
        userEmail.setText(user.getEmail());

        Glide.with(this)
                .load(storageReference.child("user_images").child(getUser()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(userImage);
    }

    private void setGroupDetails() {
        groupName.setText(group.getGroupName());
        groupSpeciality.setText(group.getGroupSpeciality());
    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton:
                if (userGroups > 0) {

                } else {
                    sendRequest();
                }
                break;
            case R.id.cancelButton:
                getDialog().cancel();
                break;
        }
    }

    private void sendRequest() {
        RequestClass requestClass = new RequestClass();
        requestClass.setUserId(getUser());
        requestClass.setGroupId(groupId);
        requestClass.setRequestStatus("waiting");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());

        requestClass.setRequestDate(currentDateandTime);
        String id = mDatabase.push().getKey();
        mDatabase.child("teams")
                .child(groupId)
                .child("requests")
                .child(id).setValue(requestClass);

        mDatabase.child("users")
                .child(getUser())
                .child("requests")
                .child(id)
                .setValue(requestClass);

        Snackbar.make(ProjectGroupInfoFragment.view.findViewById(R.id.relativeLay),
                "Request sended", Snackbar.LENGTH_LONG).show();
        getDialog().dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDatabase.child("users").child(getUser()).removeEventListener(valueEventListener);
    }

}
