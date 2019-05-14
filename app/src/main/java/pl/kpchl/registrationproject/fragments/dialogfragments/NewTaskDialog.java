package pl.kpchl.registrationproject.fragments.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import pl.kpchl.registrationproject.models.TaskClass;
import pl.kpchl.registrationproject.underactivities.ProjectManagmentActivity;
import pl.kpchl.registrationproject.useractivities.CreateProjectActivity;


public class NewTaskDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private TextInputLayout taskNameET;
    private TextInputLayout taskDescriptionET;
    private TextInputLayout taskStartET;
    private TextInputLayout taskEndET;
    private Spinner taskGroupId;
    private DatabaseReference mDatabase;
    private ArrayList<String> groupArrayList = new ArrayList<>();
    private ArrayAdapter groupAdapter;
    private String groupName;
    private int spinnerActivePosition;
    private FirebaseAuth mAuth;
    private String currentUser;
    private Button saveButton;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*Bundle bundle = this.getArguments();
        if (bundle != null) {
            detailType = bundle.getStringArrayList("detailType");
        }*/

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_task_dialog, null);

        builder.setView(view);
        setupDatabase();
        components(view);
        //setInformations();
        setupSpinner();
        return builder.create();
    }

    private void setInformations() {
    }

    //setup Firebase Database
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void components(View view) {
        taskNameET = view.findViewById(R.id.taskName);
        taskDescriptionET = view.findViewById(R.id.taskDescription);
        taskStartET = view.findViewById(R.id.taskDateStart);
        taskEndET = view.findViewById(R.id.taskDateEnd);
        taskGroupId = view.findViewById(R.id.groupSpinner);
        saveButton = view.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(this);

    }
    private void getAndSetData(){
        String taskName = taskNameET.getEditText().getText().toString();
        String taskDescription = taskDescriptionET.getEditText().getText().toString();
        String taskDateStart = taskStartET.getEditText().getText().toString();
        String taskDateEnd = taskEndET.getEditText().getText().toString();
        String taskGroup = groupArrayList.get(spinnerActivePosition);

        mDatabase.child("projects").child(ProjectManagmentActivity.projectId).child("tasks")
                .child(mDatabase.push().getKey())
                .setValue(new TaskClass(taskName,taskDescription,taskDateStart,taskDateEnd,taskGroup));

    }

    private void setupSpinner(){
        mDatabase.child("teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("groupAdmin").getValue(String.class).equals(getUser())){
                    groupArrayList.add(dataSnapshot.child("groupName").getValue(String.class));
                }
                groupAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, groupArrayList);
                taskGroupId.setAdapter(groupAdapter);
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
        taskGroupId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTextSize(20);
                spinnerActivePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSave:
                getAndSetData();
                getDialog().cancel();
                break;
        }
    }
}
