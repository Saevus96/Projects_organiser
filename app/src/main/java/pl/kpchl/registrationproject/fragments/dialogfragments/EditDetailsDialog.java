package pl.kpchl.registrationproject.fragments.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;

public class EditDetailsDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private TextView detailName;
    private EditText detailEdit;
    private Button saveButton;
    private Button cancelButton;
    private ArrayList<String> detailType;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            detailType = bundle.getStringArrayList("detailType");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_user_details_dialog, null);

        builder.setView(view);
        setupDatabase();
        components(view);
        setInformations();

        return builder.create();
    }

    private void components(View v) {
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
        }
        detailEdit.setHint(detailType.get(1));
    }

    private void saveInformations() {
        mDatabase.child("users").child(getUser()).child(detailType.get(0)).setValue(detailEdit.getText().toString());
        getDialog().cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                saveInformations();

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
