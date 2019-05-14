package pl.kpchl.registrationproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import pl.kpchl.registrationproject.ExampleProjectsActivity;
import pl.kpchl.registrationproject.R;


public class DescriptionFragment extends BaseFragment implements View.OnClickListener {
    private TextView textDescription;
    private TextView textShow;
    private ImageView descriptionImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.description_fragment, container, false);
        components(view);
        animation();
        return view;
    }

    private void animation() {
        Animation animBounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_anim);
        Animation animFromUp = AnimationUtils.loadAnimation(getContext(), R.anim.from_up);

        textDescription.startAnimation(animFromUp);
        descriptionImage.startAnimation(animFromUp);
        textShow.startAnimation(animFromUp);
        textShow.startAnimation(animBounce);
    }

    private void components(View v) {
        textDescription = v.findViewById(R.id.descriptionText);
        textShow = v.findViewById(R.id.showProjects);
        descriptionImage = v.findViewById(R.id.descriptionImage);
        textDescription.setOnClickListener(this);
        textShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showProjects:
                startActivity(new Intent(getActivity(), ExampleProjectsActivity.class));
                break;

        }
    }

}
