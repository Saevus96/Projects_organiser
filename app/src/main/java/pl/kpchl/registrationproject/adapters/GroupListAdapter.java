package pl.kpchl.registrationproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.models.GroupClass;


public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupListViewHolder> {
    private Context mContext;
    private ArrayList<GroupClass> mGroupList;

    public GroupListAdapter(Context context, ArrayList<GroupClass> groupList) {
        mContext = context;
        mGroupList = groupList;
    }

    @NonNull
    @Override
    public GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_group_view, viewGroup, false);

        return new GroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListViewHolder groupListViewHolder, int i) {
        final GroupClass object = mGroupList.get(i);
        groupListViewHolder.groupName.setText(object.getGroupName());
        groupListViewHolder.groupSpeciality.setText(object.getGroupSpeciality());
        if (i % 2 != 0) {
            groupListViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        } else {
            groupListViewHolder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    public class GroupListViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        TextView groupSpeciality;

        public GroupListViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupNameTX);
            groupSpeciality = itemView.findViewById(R.id.groupSpecialityTX);

        }
    }
    public void removeItem(int position) {
        mGroupList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(GroupClass item, int position) {
        mGroupList.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<GroupClass> getData() {
        return mGroupList;
    }
}
