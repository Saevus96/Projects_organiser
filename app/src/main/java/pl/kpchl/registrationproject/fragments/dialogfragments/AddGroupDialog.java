package pl.kpchl.registrationproject.fragments.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.underactivities.ProjectManagmentActivity;

public class AddGroupDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private Spinner groupSpinner;
    private DatabaseReference mDatabase;
    private ArrayList<String> groupList = new ArrayList<>();
    private ArrayList<String> groupListId = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String currentUser;
    private ArrayAdapter adapter;
    public int spinnerPosition;
    public int activeGroupsCounter = 0;
    private Button saveButton;
    private Button cancelButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_group_dialog, null);
        builder.setView(view);
        components(view);
        setupDatabase();
        setDetails();
        countProjectGroups();
        setSpinner();
        return builder.create();

    }

    private void addToDataBase() {
        if(!groupListId.isEmpty()) {
            mDatabase.child("projects")
                    .child(ProjectManagmentActivity.projectId)
                    .child("projectGroups")
                    .child(groupListId.get(groupSpinner.getSelectedItemPosition()))
                    .setValue("1");
        }else{
            createToast("You add all yours groups", R.drawable.ic_alert);
        }
    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    //setup components
    private void components(View view) {
        groupSpinner = view.findViewById(R.id.groupSpinner);
        saveButton = view.findViewById(R.id.buttonSave);
        cancelButton = view.findViewById(R.id.buttonCancel);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

    }

    private void setAdapter() {
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, groupList);
        groupSpinner.setAdapter(adapter);

    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void countProjectGroups() {
        mDatabase.child("projects")
                .child(ProjectManagmentActivity.projectId)
                .child("projectGroups").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                activeGroupsCounter++;
                for (int i = 0; i < groupListId.size(); i++) {

                    if (dataSnapshot.getKey().equals(groupListId.get(i))) {
                        groupList.remove(i);
                        groupListId.remove(i);
                        adapter.notifyDataSetChanged();
                    }
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

    }

    private void setDetails() {
        mDatabase.child("teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("groupAdmin").getValue(String.class).equals(getUser())) {
                    String singleGroupName = dataSnapshot.child("groupName").getValue(String.class);
                    groupList.add(singleGroupName);
                    groupListId.add(dataSnapshot.getKey().toString());
                    setAdapter();
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


    }

    private void setSpinner() {
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                    ((TextView) view).setTextSize(20);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                addToDataBase();
                break;
            case R.id.buttonCancel:
                getDialog().dismiss();
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
