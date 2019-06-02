package pl.kpchl.registrationproject.adapters;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.fragments.dialogfragments.JoinToGroupDialog;
import pl.kpchl.registrationproject.fragments.projectfragments.ProjectGroupInfoFragment;
import pl.kpchl.registrationproject.models.GroupClass;
import pl.kpchl.registrationproject.underactivities.GroupManagmentActivity;
import pl.kpchl.registrationproject.underactivities.YourGroupManagmentActivity;


public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupListViewHolder> {
    private Context mContext;
    private ArrayList<GroupClass> mGroupList;
    private int mGroupPage;
    private ArrayList<String> mGroupsId;
    private DatabaseReference mDatabase;
    private String userId;
    private FirebaseAuth mAuth;
    public static JoinToGroupDialog joinToGroupDialog;


    public GroupListAdapter(Context context, ArrayList<GroupClass> groupList, ArrayList<String> groupsId, int groupPage) {
        mContext = context;
        mGroupList = groupList;
        mGroupPage = groupPage;
        mGroupsId = groupsId;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

    }

    @NonNull
    @Override
    public GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (mGroupPage == 3 || mGroupPage == 1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_group1_view, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_group_view, viewGroup, false);
        }


        return new GroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupListViewHolder groupListViewHolder, final int i) {
        final GroupClass object = mGroupList.get(i);
        groupListViewHolder.groupName.setText(object.getGroupName());
        groupListViewHolder.groupSpeciality.setText(object.getGroupSpeciality());
        if (i % 2 != 0) {
            groupListViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        } else {
            groupListViewHolder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }


        if (mGroupPage == 2) {
            int checker = 0;
            groupListViewHolder.buttonJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ProjectGroupInfoFragment.checker == 1) {
                        showDialog(mGroupsId.get(i));
                    } else {
                        Toast.makeText(mContext, "Fill your details to join", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mDatabase.child("teams").child(mGroupsId.get(i)).child("requests").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (userId.equals(dataSnapshot.child("userId").getValue(String.class))) {
                        groupListViewHolder.buttonJoin.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (userId.equals(dataSnapshot.child("userId").getValue(String.class))) {
                        groupListViewHolder.buttonJoin.setVisibility(View.GONE);
                    }
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
            mDatabase.child("teams").child(mGroupsId.get(i)).child("groupAdmin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue(String.class).equals(userId))
                        groupListViewHolder.buttonJoin.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (mGroupPage == 3 || mGroupPage == 1) {
            groupListViewHolder.buttonJoin.setVisibility(View.GONE);
            mDatabase.child("teams").child(mGroupsId.get(i)).child("requests").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.hasChild("requestDate")) {
                        groupListViewHolder.buttonJoin.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.hasChild("requestDate")) {
                        groupListViewHolder.buttonJoin.setVisibility(View.VISIBLE);
                    } else {
                        groupListViewHolder.buttonJoin.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("requestDate")) {
                        groupListViewHolder.buttonJoin.setVisibility(View.VISIBLE);
                    } else {
                        groupListViewHolder.buttonJoin.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.hasChild("requestDate")) {
                        groupListViewHolder.buttonJoin.setVisibility(View.VISIBLE);
                    } else {
                        groupListViewHolder.buttonJoin.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            groupListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GroupManagmentActivity.class);
                    intent.putExtra("groupId", mGroupsId.get(i));
                    mContext.startActivity(intent);
                }
            });
        } else if (mGroupPage == 4) {
            groupListViewHolder.buttonJoin.setVisibility(View.GONE);
            groupListViewHolder.groupName.setText(object.getGroupName());
            groupListViewHolder.groupSpeciality.setText(object.getGroupSpeciality());

            groupListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, YourGroupManagmentActivity.class);
                    intent.putExtra("groupId", mGroupsId.get(i));
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    public class GroupListViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        TextView groupSpeciality;
        ImageView buttonJoin;

        public GroupListViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupNameTX);
            groupSpeciality = itemView.findViewById(R.id.groupSpecialityTX);
            buttonJoin = itemView.findViewById(R.id.joinButton);
        }
    }

    public void removeItem(int position) {
        mGroupList.remove(position);
        notifyItemRemoved(position);
    }

    public void showDialog(String groupId) {

        joinToGroupDialog = new JoinToGroupDialog();
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        Bundle args = new Bundle();

        args.putString("groupObject", groupId);
        joinToGroupDialog.setArguments(args);
        joinToGroupDialog.show(fragmentManager, "Dialog");
    }


}
