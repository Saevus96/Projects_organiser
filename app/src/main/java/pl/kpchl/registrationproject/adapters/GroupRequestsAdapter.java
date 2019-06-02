package pl.kpchl.registrationproject.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.models.RequestClass;
import pl.kpchl.registrationproject.models.UserClass;
import pl.kpchl.registrationproject.underactivities.GroupManagmentActivity;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class GroupRequestsAdapter extends RecyclerView.Adapter<GroupRequestsAdapter.GroupRequestsViewHolder> {
    private Context mContext;
    private ArrayList<RequestClass> mRequestsArray;
    private ArrayList<UserClass> mUsers;
    private ArrayList<String> mUsersId;
    private ArrayList<String> mRequestKeys;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public GroupRequestsAdapter(Context context, ArrayList<RequestClass> requestArray, ArrayList<UserClass> users, ArrayList<String> usersId, ArrayList<String> requestKeys) {
        mContext = context;
        mRequestsArray = requestArray;
        mUsers = users;
        mUsersId = usersId;
        mRequestKeys = requestKeys;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @NonNull
    @Override
    public GroupRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_group_request, viewGroup, false);
        return new GroupRequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRequestsViewHolder groupRequestsViewHolder, final int i) {
        if (i % 2 != 0) {
            groupRequestsViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            groupRequestsViewHolder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
        final UserClass object1 = mUsers.get(i);
        final RequestClass object2 = mRequestsArray.get(i);
        groupRequestsViewHolder.userFirstName.setText(object1.getFirstName());
        groupRequestsViewHolder.userLastName.setText(object1.getLastName());
        groupRequestsViewHolder.userAge.setText(object1.getAge());
        groupRequestsViewHolder.userEmail.setText(object1.getEmail());

        Glide.with(mContext)
                .load(storageReference.child("user_images").child(object2.getUserId()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(groupRequestsViewHolder.userImage);
        groupRequestsViewHolder.downloadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageReference.child("user_cv").child(object2.getUserId()).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DownloadManager downloadManager = (DownloadManager) mContext
                                        .getSystemService(Context.DOWNLOAD_SERVICE);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalFilesDir(mContext,DIRECTORY_DOWNLOADS, object1.getFirstName()+"_"+object1.getLastName()+".pdf");
                                downloadManager.enqueue(request);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRequestsArray.size();
    }

    public class GroupRequestsViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        ImageView downloadCV;
        TextView userFirstName;
        TextView userLastName;
        TextView userAge;
        TextView userEmail;

        public GroupRequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            downloadCV = itemView.findViewById(R.id.downloadCvButton);
            userFirstName = itemView.findViewById(R.id.userFirstName);
            userLastName = itemView.findViewById(R.id.userLastName);
            userAge = itemView.findViewById(R.id.userAge);
            userEmail = itemView.findViewById(R.id.userEmail);

        }
    }

    public void removeItem(int position) {
        GroupManagmentActivity.mDatabase
                .child("teams")
                .child(GroupManagmentActivity.groupId)
                .child("requests")
                .child(mRequestKeys.get(position)).removeValue();

        GroupManagmentActivity.mDatabase.child("users")
                .child(mUsersId.get(position))
                .child("requests")
                .child(mRequestKeys.get(position))
                .child("requestStatus").setValue("declined");

        Log.d("requestKey", mRequestKeys.get(position));
        Log.d("userID", mUsersId.get(position));
        mUsersId.remove(position);
        mRequestKeys.remove(position);
        mRequestsArray.remove(position);
        mUsers.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void addItem(int position) {
        GroupManagmentActivity.mDatabase
                .child("teams")
                .child(GroupManagmentActivity.groupId)
                .child("requests")
                .child(mRequestKeys.get(position)).removeValue();

        GroupManagmentActivity.mDatabase
                .child("teams")
                .child(GroupManagmentActivity.groupId)
                .child("groupMembers")
                .child(mUsersId.get(position))
                .setValue(mUsers.get(position));

        GroupManagmentActivity.mDatabase.child("users")
                .child(mUsersId.get(position))
                .child("requests")
                .child(mRequestKeys.get(position))
                .child("requestStatus").setValue("accepted");

        Log.d("requestKey", mRequestKeys.get(position));
        Log.d("userID", mUsersId.get(position));

        mUsersId.remove(position);
        mRequestsArray.remove(position);
        mUsers.remove(position);
        mRequestKeys.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
