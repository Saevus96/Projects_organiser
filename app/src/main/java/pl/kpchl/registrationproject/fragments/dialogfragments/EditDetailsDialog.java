package pl.kpchl.registrationproject.fragments.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.underactivities.ProjectManagmentActivity;
import pl.kpchl.registrationproject.useractivities.CreateProjectActivity;

public class EditDetailsDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private TextView detailName;
    private EditText detailEdit;
    private Button saveButton;
    private Button cancelButton;
    private ArrayList<String> detailType;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;
    private String dataType;
    private Spinner categorySpinner;
    private RelativeLayout relativeLayout;
    private ArrayAdapter categoryAdapter;
    private int spinnerActivePosition;
    private ArrayList<String> catergoryArrayList = new ArrayList<>();
    private String categoryName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            detailType = bundle.getStringArrayList("detailType");
            dataType = bundle.getString("dataType");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_user_details_dialog, null);

        builder.setView(view);
        setupDatabase();
        components(view);
        categorySpinner = new Spinner(getContext());
        if (detailType.get(0).equals("projectCategory")) {
            relativeLayout.removeView(detailEdit);
            ViewGroup.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            ((RelativeLayout.LayoutParams) params).setMargins(30,40,30,0);
            categorySpinner.setLayoutParams(params);
            relativeLayout.addView(categorySpinner);
            setupSpinner();
        }
        setInformations();
        return builder.create();
    }

    //Get iformation from firebase about categories
    private void setupSpinner() {
        mDatabase.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                categoryName = dataSnapshot.getValue(String.class);
                catergoryArrayList.add(categoryName);
                categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, catergoryArrayList);
                categorySpinner.setAdapter(categoryAdapter);
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
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    private void components(View v) {
        relativeLayout = v.findViewById(R.id.relativeLay);
        detailName = v.findViewById(R.id.detailName);
        detailEdit = v.findViewById(R.id.detailEdit);
        saveButton = v.findViewById(R.id.buttonSave);
        cancelButton = v.findViewById(R.id.buttonCancel);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void setInformations() {
        if (detailType.get(0).equals("firstName")) {
            detailName.setText("Edit first name");
        } else if (detailType.get(0).equals("lastName")) {
            detailName.setText("Edit last name");
        } else if (detailType.get(0).equals("age")) {
            detailName.setText("Edit age");
        } else if (detailType.get(0).equals("education")) {
            detailName.setText("Edit education");
        } else if (detailType.get(0).equals("experience")) {
            detailName.setText("Edit experience");
        } else if (detailType.get(0).equals("organisation")) {
            detailName.setText("Edit organisation");
        } else if (detailType.get(0).equals("interests")) {
            detailName.setText("Edit interests");
        } else if (detailType.get(0).equals("email")) {
            detailName.setText("Edit email");
        } else if (detailType.get(0).equals("phone")) {
            detailName.setText("Edit phone number");
        } else if (detailType.get(0).equals("projectName")) {
            detailName.setText("Edit project name");
        } else if (detailType.get(0).equals("projectCategory")) {

        } else if (detailType.get(0).equals("projectDescription")) {
            detailName.setText("Edit project description");
        } else if (detailType.get(0).equals("projectCustomers")) {
            detailName.setText("Edit project customers");
        } else if (detailType.get(0).equals("projectOrganisation")) {
            detailName.setText("Edit project organisation");
        }
        if(!detailType.get(0).equals("projectCategory")){
            detailEdit.setHint(detailType.get(1));
        }

    }

    private void saveInformationsUser() {
        mDatabase.child("users").child(getUser())
                .child(detailType.get(0)).setValue(detailEdit.getText().toString());
        getDialog().cancel();
    }

    private void saveInformationsProject() {
        if(!detailType.get(0).equals("projectCategory")){
        mDatabase.child("projects").child(ProjectManagmentActivity.projectId)
                .child(detailType.get(0)).setValue(detailEdit.getText().toString());
        getDialog().cancel();
        }else{
            mDatabase.child("projects").child(ProjectManagmentActivity.projectId)
                    .child(detailType.get(0)).setValue("Category"+spinnerActivePosition);
            getDialog().cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                if (dataType.equals("userInfo")) {
                    saveInformationsUser();
                } else {
                    saveInformationsProject();
                }

                break;
            case R.id.buttonCancel:
                getDialog().cancel();
                break;
        }
    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    //setup Firebase Database
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
