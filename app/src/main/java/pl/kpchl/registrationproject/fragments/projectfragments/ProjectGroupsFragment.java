package pl.kpchl.registrationproject.fragments.projectfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.fragments.BaseFragment;

public class ProjectGroupsFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_groups_fragment, container, false);
        components(view);

        return view;
    }

    private void components(View v) {

    }
}
