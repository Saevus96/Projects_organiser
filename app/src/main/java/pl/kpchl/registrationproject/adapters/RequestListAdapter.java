package pl.kpchl.registrationproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.models.RequestClass;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.RequestListViewHolder> {
    private Context mContext;
    ArrayList<RequestClass> mRequestList;
    private DatabaseReference mDatabase;
    private String userId;
    private FirebaseAuth mAuth;

    public RequestListAdapter(Context context, ArrayList<RequestClass> requestList) {
        mContext = context;
        mRequestList = requestList;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public RequestListAdapter.RequestListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_request_view, viewGroup, false);

        return new RequestListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestListAdapter.RequestListViewHolder requestListViewHolder, int i) {
        RequestClass object = mRequestList.get(i);

        requestListViewHolder.requestStatus.setText(object.getRequestStatus());
        requestListViewHolder.requestDate.setText(object.getRequestDate());
        mDatabase.child("teams").child(object.getGroupId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestListViewHolder.groupName.setText(dataSnapshot.child("groupName").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (object.getRequestStatus().equals("waiting")) {
            requestListViewHolder.itemView.setBackgroundColor(Color.parseColor("#ffff00"));
        } else if (object.getRequestStatus().equals("accepted")) {
            requestListViewHolder.itemView.setBackgroundColor(Color.parseColor("#008000"));
        } else {
            requestListViewHolder.itemView.setBackgroundColor(Color.parseColor("#FF0000"));
        }

    }

    @Override
    public int getItemCount() {
        return mRequestList.size();
    }

    public class RequestListViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public TextView requestDate;
        public TextView requestStatus;

        public RequestListViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            requestDate = itemView.findViewById(R.id.requestDate);
            requestStatus = itemView.findViewById(R.id.requestStatus);
        }
    }
}
