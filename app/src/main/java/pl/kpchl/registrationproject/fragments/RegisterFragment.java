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

import pl.kpchl.registrationproject.HomePageActivity;
import pl.kpchl.registrationproject.R;

public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private TextView registrationText;
    private ImageView registrationImage;
    private TextView showRegistrationText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        components(view);
        animation();
        return view;
    }

    //Components animations
    private void animation() {
        Animation animBounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_anim);
        Animation animFromUp = AnimationUtils.loadAnimation(getContext(), R.anim.from_up);

        registrationText.startAnimation(animFromUp);
        registrationImage.startAnimation(animFromUp);
        showRegistrationText.startAnimation(animFromUp);
        showRegistrationText.startAnimation(animBounce);
    }

    //Function to find components
    public void components(View v) {
        registrationText = v.findViewById(R.id.registrationText);
        registrationImage = v.findViewById(R.id.registrationImage);
        showRegistrationText = v.findViewById(R.id.showRegistration);
        showRegistrationText.setOnClickListener(this);
    }

    //Method to find clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showRegistration:
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                intent.putExtra("startPage", "1");
                startActivity(intent);
                //startActivity(new Intent(getActivity(), HomePageActivity.class));
                break;
        }
    }
}
