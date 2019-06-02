package pl.kpchl.registrationproject.fragments.groupfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.GroupUsersAdapter;
import pl.kpchl.registrationproject.adapters.SwipeToDeleteCallback;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.models.UserClass;
import pl.kpchl.registrationproject.underactivities.GroupManagmentActivity;
import pl.kpchl.registrationproject.underactivities.YourGroupManagmentActivity;

public class GroupUsersFragment extends BaseFragment {
    private RecyclerView groupUsersRecyclerView;
    private ArrayList<UserClass> groupUsers = new ArrayList<>();
    private ArrayList<String> usersIdList = new ArrayList<>();
    private GroupUsersAdapter groupUsersAdapter;
    private String groupId;
    private DatabaseReference mDatabase;
    private LinearLayout include;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_users, container, false);

        mDatabase = GroupManagmentActivity.mDatabase;
        groupId = GroupManagmentActivity.groupId;
        components(view);
        readGroupUsersFromDataBase();
        swipeFunctions();
        return view;
    }

    private void swipeFunctions() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                groupUsersAdapter.removeItem(position);

            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(groupUsersRecyclerView);
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

    private void components(View v) {
        groupUsersRecyclerView = v.findViewById(R.id.recyclerViewGroupUsers);
        include = v.findViewById(R.id.include);
        include.setVisibility(View.GONE);
    }

    private void setAdapter() {
        setupRecyclerView();
        groupUsersAdapter = new GroupUsersAdapter(getContext(), groupUsers, usersIdList,1);
        groupUsersRecyclerView.setAdapter(groupUsersAdapter);
        groupUsersAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        groupUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}
