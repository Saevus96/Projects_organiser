package pl.kpchl.registrationproject.fragments.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;

public class TaskInfoDialog extends AppCompatDialogFragment {
    private TextView taskName;
    private TextView taskDescription;
    private TextView taskDateStart;
    private TextView taskDateEnd;
    private TextView taskGroup;
    private ArrayList<String> taskDetailsList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            taskDetailsList = bundle.getStringArrayList("taskObject");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.task_information_dialog_layout, null);
        builder.setView(view);
        components(view);
        setDetails();
        return builder.create();

    }

    private void components(View view) {
        taskName = view.findViewById(R.id.taskName);
        taskDescription = view.findViewById(R.id.taskDescription);
        taskDateStart = view.findViewById(R.id.taskDateStart);
        taskDateEnd = view.findViewById(R.id.taskDateEnd);
        taskGroup = view.findViewById(R.id.taskGroup);
    }

    private void setDetails() {
        taskName.setText(taskDetailsList.get(0));
        taskDescription.setText(taskDetailsList.get(1));
        taskDateStart.setText(taskDetailsList.get(2));
        taskDateEnd.setText(taskDetailsList.get(3));
        taskGroup.setText(taskDetailsList.get(4));
    }
}
