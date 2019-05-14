package pl.kpchl.registrationproject.fragments.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;

public class ProjectInfoDialog extends AppCompatDialogFragment implements View.OnClickListener{
    private TextView projectName;
    private TextView projectDescription;
    private TextView projectCategory;
    private TextView projectOrganisation;
    private Button cancelButton;
    private ArrayList<String> projectList;

    public ProjectInfoDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            projectList = bundle.getStringArrayList("projectObject");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.project_information_dialog, null);

        builder.setView(view);

        components(view);
        setInformations();

        return builder.create();
    }

    private void components(View v) {
        projectName = v.findViewById(R.id.titleText);
        projectDescription = v.findViewById(R.id.descriptionText);
        projectCategory = v.findViewById(R.id.categoryText);
        projectOrganisation = v.findViewById(R.id.organisationText);
        cancelButton = v.findViewById(R.id.buttonCancel);
    }

    private void setInformations() {
        projectName.setText(projectList.get(0));
        projectDescription.setText(projectList.get(1));
        projectOrganisation.setText(projectList.get(2));
        projectCategory.setText(projectList.get(3));
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonCancel){
            getDialog().cancel();
        }
    }
}

