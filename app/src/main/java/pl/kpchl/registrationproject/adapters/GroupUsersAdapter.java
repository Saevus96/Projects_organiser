package pl.kpchl.registrationproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.models.UserClass;
import pl.kpchl.registrationproject.underactivities.GroupManagmentActivity;

public class GroupUsersAdapter extends RecyclerView.Adapter<GroupUsersAdapter.GroupUsersViewHolder> {
    private Context mContext;
    private ArrayList<UserClass> mUsers;
    private ArrayList<String> mUsersId;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private int mPage;

    public GroupUsersAdapter(Context context, ArrayList<UserClass> users, ArrayList<String> usersId, int page) {
        mContext = context;
        mUsers = users;
        mUsersId = usersId;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @NonNull
    @Override
    public GroupUsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_user_view, viewGroup, false);
        return new GroupUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupUsersViewHolder groupUsersViewHolder, int i) {

        if (i % 2 != 0) {
            groupUsersViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            groupUsersViewHolder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }

        final UserClass object1 = mUsers.get(i);


        groupUsersViewHolder.userFirstName.setText(object1.getFirstName());
        groupUsersViewHolder.userLastName.setText(object1.getLastName());
        groupUsersViewHolder.userEmail.setText(object1.getEmail());
        if(mPage ==1){
            groupUsersViewHolder.userAge.setText(object1.getAge());
        }else{
            groupUsersViewHolder.userAge.setVisibility(View.GONE);
        }


        Glide.with(mContext)
                .load(storageReference.child("user_images").child(mUsersId.get(i)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(groupUsersViewHolder.userImage);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class GroupUsersViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userFirstName;
        TextView userLastName;
        TextView userAge;
        TextView userEmail;

        public GroupUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userFirstName = itemView.findViewById(R.id.userFirstName);
            userLastName = itemView.findViewById(R.id.userLastName);
            userAge = itemView.findViewById(R.id.userAge);
            userEmail = itemView.findViewById(R.id.userEmail);

        }
    }

    public void removeItem(int position) {
        GroupManagmentActivity.mDatabase.child("teams")
                .child(GroupManagmentActivity.groupId)
                .child("groupMembers")
                .child(mUsersId.get(position)).removeValue();

        mUsersId.remove(position);
        mUsers.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
