package pl.kpchl.registrationproject.fragments.groupfragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import pl.kpchl.registrationproject.adapters.GroupUsersAdapter;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.models.UserClass;
import pl.kpchl.registrationproject.underactivities.YourGroupManagmentActivity;

public class YourGroupUsersFragment extends BaseFragment {
    private RecyclerView groupUsersRecyclerView;
    private ArrayList<UserClass> groupUsers = new ArrayList<>();
    private ArrayList<String> usersIdList = new ArrayList<>();
    private GroupUsersAdapter groupUsersAdapter;
    private String groupId;
    private DatabaseReference mDatabase;
    private ImageView adminImage;
    private TextView adminName;
    private TextView adminLastName;
    private TextView adminAge;
    private TextView adminEmail;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private LinearLayout include;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_users, container, false);

        mDatabase = YourGroupManagmentActivity.mDatabase;
        groupId = YourGroupManagmentActivity.groupId;
        components(view);
        setupStorage();
        readAdmin();
        readGroupUsersFromDataBase();
        return view;
    }

    private void readGroupUsersFromDataBase() {
        mDatabase
                .child("teams")
                .child(groupId)
                .child("groupMembers")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        UserClass userClass = dataSnapshot.getValue(UserClass.class);
                        groupUsers.add(userClass);
                        String usersId = dataSnapshot.getKey();
                        usersIdList.add(usersId);
                        setAdapter();
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
    private void setupStorage(){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }
    private void readAdmin(){
        mDatabase.child("teams").child(groupId).child("groupAdmin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String adminId = dataSnapshot.getValue(String.class);
                setAdminDetails(adminId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAdminDetails(final String adminId) {
        mDatabase.child("users").child(adminId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);
                Glide.with(getActivity())
                        .load(storageReference.child("user_images").child(adminId))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(adminImage);
                adminName.setText(userClass.getFirstName());
                adminLastName.setText(userClass.getLastName());
                adminEmail.setText(userClass.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void components(View v) {
        groupUsersRecyclerView = v.findViewById(R.id.recyclerViewGroupUsers);
        adminImage = v.findViewById(R.id.userImage);
        adminName = v.findViewById(R.id.userFirstName);
        adminLastName = v.findViewById(R.id.userLastName);
        adminAge = v.findViewById(R.id.userAge);
        adminEmail = v.findViewById(R.id.userEmail);
        adminAge.setVisibility(View.GONE);
        include = v.findViewById(R.id.include);
        include.setBackgroundColor(Color.parseColor("#ffff00"));
    }

    private void setAdapter() {
        setupRecyclerView();
        groupUsersAdapter = new GroupUsersAdapter(getContext(), groupUsers, usersIdList,2);
        groupUsersRecyclerView.setAdapter(groupUsersAdapter);
        groupUsersAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        groupUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}
