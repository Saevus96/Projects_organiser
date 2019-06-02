package pl.kpchl.registrationproject.fragments.groupfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.GroupRequestsAdapter;
import pl.kpchl.registrationproject.adapters.SwipeToAcceptUserCallback;
import pl.kpchl.registrationproject.adapters.SwipeToDeleteCallback;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.models.RequestClass;
import pl.kpchl.registrationproject.models.UserClass;
import pl.kpchl.registrationproject.underactivities.GroupManagmentActivity;

public class GroupRequestsFragment extends BaseFragment {
    private RecyclerView groupRequestsRecyclerView;
    private ArrayList<RequestClass> requestList = new ArrayList<>();
    private ArrayList<UserClass> users = new ArrayList<>();
    private ArrayList<String> usersId = new ArrayList<>();
    private ArrayList<String> requestKeys = new ArrayList<>();
    private GroupRequestsAdapter groupRequestsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_requests_fragment, container, false);
        components(view);
        readRequests();
        swipeFunctions();
        return view;
    }

    private void swipeFunctions() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                groupRequestsAdapter.removeItem(position);

            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(groupRequestsRecyclerView);

        SwipeToAcceptUserCallback swipeToAcceptUserCallback = new SwipeToAcceptUserCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                groupRequestsAdapter.addItem(position);
            }
        };
        ItemTouchHelper itemTouchhelper2 = new ItemTouchHelper(swipeToAcceptUserCallback);
        itemTouchhelper2.attachToRecyclerView(groupRequestsRecyclerView);
    }

    private void components(View v) {
        groupRequestsRecyclerView = v.findViewById(R.id.recyclerViewGroupsRequests);
    }

    private void setAdapter() {
        setupRecyclerView();
        groupRequestsAdapter = new GroupRequestsAdapter(getContext(), requestList, users, usersId,requestKeys);
        groupRequestsRecyclerView.setAdapter(groupRequestsAdapter);
    }

    private void setupRecyclerView() {
        groupRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void readUsersRequest(String userId) {
        GroupManagmentActivity.mDatabase
                .child("users")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserClass userClass = dataSnapshot.getValue(UserClass.class);
                        users.add(userClass);
                        setAdapter();
                        groupRequestsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void readRequests() {
        GroupManagmentActivity.mDatabase
                .child("teams")
                .child(GroupManagmentActivity.groupId)
                .child("requests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RequestClass requestClass = dataSnapshot.getValue(RequestClass.class);
                requestList.add(requestClass);
                readUsersRequest(requestClass.getUserId());
                usersId.add(requestClass.getUserId());
                String requestKey = dataSnapshot.getKey();
                requestKeys.add(requestKey);


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
}
